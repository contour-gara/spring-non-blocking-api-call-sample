package org.contourgara.infrastructure;

import org.contourgara.common.AppConfig;
import org.contourgara.common.RequestId;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Repository("completable-future")
public class CompletableFutureThreadSleepClient implements ThreadSleepClient {
    private final RequestId requestId;
    private final RestClient restClient;

    public CompletableFutureThreadSleepClient(RequestId requestId, AppConfig appConfig) {
        this.requestId = requestId;

        restClient = RestClient.builder()
                .baseUrl("http://%s:8081".formatted(appConfig.getRequestServerDomain()))
                .build();
    }

    @Override
    public List<String> fetch() {
        String requestId = this.requestId.getRequestId();  // CompletableFuture 内では呼び出せないので事前に呼び出しておく

        List<CompletableFuture<String>> futures = IntStream.rangeClosed(1, 3)
                .mapToObj(
                        i -> CompletableFuture.supplyAsync(
                                () -> callApi(requestId, String.valueOf(i))
                        )
                )
                .toList();  // List ではないと非同期で実行されない

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private String callApi(String requestId, String count) {
        return restClient.get()
                .uri("/thread-sleep")
                .header("X-Request-Id", "%s-%s".formatted(requestId, count))
                .retrieve()
                .body(String.class);
    }
}
