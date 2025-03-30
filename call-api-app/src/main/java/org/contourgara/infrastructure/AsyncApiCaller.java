package org.contourgara.infrastructure;

import org.contourgara.common.AppConfig;
import org.contourgara.common.RequestId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncApiCaller {
    private final RestClient restClient;
    private final RequestId requestId;

    public AsyncApiCaller(AppConfig appConfig, RequestId requestId) {
        this.restClient = RestClient.builder()
                .baseUrl("http://%s:8081".formatted(appConfig.getRequestServerDomain()))
                .build();

        this.requestId = requestId;
    }

    /**
     * 別クラスから呼び出さないと非同期にならない
     *
     * @param count
     * @return
     */
    @Async
    public CompletableFuture<String> callApi(String count) {
        return CompletableFuture.completedFuture(
                restClient.get()
                        .uri("/thread-sleep")
                        .header("X-Request-Id", "%s-%s".formatted(requestId.getRequestId(), count))
                        .retrieve()
                        .body(String.class)
        );
    }
}
