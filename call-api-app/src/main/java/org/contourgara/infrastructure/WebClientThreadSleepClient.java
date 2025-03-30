package org.contourgara.infrastructure;

import org.contourgara.common.AppConfig;
import org.contourgara.common.RequestId;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

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
        Flux<String> responses = Flux.empty();

        for (int i = 1; i <= 3; i++) {
            responses = responses.mergeWith(
                    webClient.get()
                            .uri("/thread-sleep")
                            .header("X-Request-Id", "%s-%s".formatted(requestId.getRequestId(), i))
                            .retrieve()
                            .bodyToFlux(String.class)
            );
        }

        return responses.collectList().block();
    }
}
