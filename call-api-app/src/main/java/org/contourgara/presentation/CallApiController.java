package org.contourgara.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.contourgara.common.RequestId;
import org.contourgara.application.AsyncUseCase;
import org.contourgara.application.CompletableFutureUseCase;
import org.contourgara.application.WebClientUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CallApiController {
    private final WebClientUseCase webClientUseCase;
    private final CompletableFutureUseCase completableFutureUseCase;
    private final AsyncUseCase asyncUseCase;
    private final RequestId requestId;

    @GetMapping("/web-client")
    @ResponseStatus(HttpStatus.OK)
    public void callApiByWebClient() {
        requestId.setRequestId("web-client-%s".formatted(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        log.info("Request ID: {}", requestId.getRequestId());
        webClientUseCase.execute();
    }

    @GetMapping("/completable-future")
    @ResponseStatus(HttpStatus.OK)
    public void callApiByCompletableFuture() {
        requestId.setRequestId("completable-future-%s".formatted(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        log.info("Request ID: {}", requestId.getRequestId());
        completableFutureUseCase.execute();
    }

    @GetMapping("/async")
    @ResponseStatus(HttpStatus.OK)
    public void callApiByAsync() {
        requestId.setRequestId("async-%s".formatted(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        log.info("Request ID: {}", requestId.getRequestId());
        asyncUseCase.execute();
    }
}
