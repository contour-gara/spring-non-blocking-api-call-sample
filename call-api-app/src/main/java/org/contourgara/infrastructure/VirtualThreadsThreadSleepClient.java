package org.contourgara.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.common.AppConfig;
import org.contourgara.common.RequestId;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Repository("virtual-threads")
@Slf4j
public class VirtualThreadsThreadSleepClient implements ThreadSleepClient {
    private final RequestId requestId;
    private final RestClient restClient;

    public VirtualThreadsThreadSleepClient(RequestId requestId, AppConfig appConfig) {
        this.requestId = requestId;

        restClient = RestClient.builder()
                .baseUrl("http://%s:8081".formatted(appConfig.getRequestServerDomain()))
                .build();
    }

    @Override
    public List<String> fetch() {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = IntStream.rangeClosed(1, 3)
                    .mapToObj(
                            i -> CompletableFuture.supplyAsync(
                                    () -> callApi(requestId.getRequestId(), String.valueOf(i)),
                                    executorService
                            )
                    )
                    .toList();  // List ではないと非同期で実行されない

            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }
    }

    private String callApi(String requestId, String count) {
        return restClient.get()
                .uri("/thread-sleep")
                .header("X-Request-Id", "%s-%s".formatted(requestId, count))
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, (request, response) -> log.info("{}: リクエスト成功", "%s-%s".formatted(requestId, count)))
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException(response.getStatusText());
                })
                .body(String.class);
    }
}
