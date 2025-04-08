package org.contourgara.infrastructure;

import org.contourgara.common.AppConfig;
import org.contourgara.common.RequestId;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

@Repository("web-client")
public class WebClientThreadSleepClient implements ThreadSleepClient {
    private final RequestId requestId;
    private final WebClient webClient;

    public WebClientThreadSleepClient(RequestId requestId, AppConfig appConfig) {
        this.requestId = requestId;

        this.webClient = WebClient.builder()
                .baseUrl("http://%s:8081".formatted(appConfig.getRequestServerDomain()))
                .build();
    }

    @Override
    public List<String> fetch() {
        return Flux.mergeSequential( // merge では順番保証されない
                Flux.fromStream(
                        IntStream.rangeClosed(1, 3)
                                .mapToObj(i -> webClient.get()
                                        .uri("/thread-sleep")
                                        .header("X-Request-Id", "%s-%s".formatted(requestId.getRequestId(), i))
                                        .retrieve()
                                        .bodyToMono(String.class))
                )
        )
                .collectList()
                .block();
    }
}
