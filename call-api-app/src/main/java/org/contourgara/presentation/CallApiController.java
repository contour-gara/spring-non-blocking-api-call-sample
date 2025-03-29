package org.contourgara.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.contourgara.bean.RequestId;
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

    @GetMapping("/webclient")
    @ResponseStatus(HttpStatus.OK)
    public void callApiByWebClient() {
        requestId.setRequestId("webclient-%s".formatted(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        log.info("RequestId: {}", requestId.getRequestId());
        webClientUseCase.execute();
    }
}
