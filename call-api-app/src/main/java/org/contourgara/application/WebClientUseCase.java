package org.contourgara.application;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.ThreadSleepClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebClientUseCase {
    private final ThreadSleepClient threadSleepClient;

    public WebClientUseCase(@Qualifier("webclient") ThreadSleepClient threadSleepClient) {
        this.threadSleepClient = threadSleepClient;
    }

    /**
     * 2025-03-31 01:09:22 call-api-app-1    | 2025-03-30T16:09:22.736Z  INFO 1 --- [nio-8080-exec-8] o.c.presentation.CallApiController       : RequestId: webclient-2025-03-30T16:09:22.736322941
     * 2025-03-31 01:09:22 request-server-1  | 2025-03-30T16:09:22.742Z  INFO 1 --- [nio-8080-exec-7] org.contourgara.ThreadSleepController    : Request ID: webclient-2025-03-30T16:09:22.736322941-2
     * 2025-03-31 01:09:22 request-server-1  | 2025-03-30T16:09:22.742Z  INFO 1 --- [nio-8080-exec-8] org.contourgara.ThreadSleepController    : Request ID: webclient-2025-03-30T16:09:22.736322941-1
     * 2025-03-31 01:09:22 request-server-1  | 2025-03-30T16:09:22.742Z  INFO 1 --- [nio-8080-exec-9] org.contourgara.ThreadSleepController    : Request ID: webclient-2025-03-30T16:09:22.736322941-3
     * 2025-03-31 01:09:32 call-api-app-1    | 2025-03-30T16:09:32.747Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"webclient-2025-03-30T16:09:22.736322941-3"}
     * 2025-03-31 01:09:32 call-api-app-1    | 2025-03-30T16:09:32.747Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"webclient-2025-03-30T16:09:22.736322941-2"}
     * 2025-03-31 01:09:32 call-api-app-1    | 2025-03-30T16:09:32.747Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"webclient-2025-03-30T16:09:22.736322941-1"}
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
