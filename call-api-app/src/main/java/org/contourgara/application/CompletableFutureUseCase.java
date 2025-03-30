package org.contourgara.application;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.ThreadSleepClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompletableFutureUseCase {
    private final ThreadSleepClient threadSleepClient;

    public CompletableFutureUseCase(@Qualifier("completable-future") ThreadSleepClient threadSleepClient) {
        this.threadSleepClient = threadSleepClient;
    }

    /**
     * 2025-03-31 01:59:15 call-api-app-1    | 2025-03-30T16:59:15.447Z  INFO 1 --- [nio-8080-exec-5] o.c.presentation.CallApiController       : Request ID: completable-future-2025-03-30T16:59:15.447138198
     * 2025-03-31 01:59:15 request-server-1  | 2025-03-30T16:59:15.455Z  INFO 1 --- [nio-8080-exec-5] org.contourgara.ThreadSleepController    : Request ID: completable-future-2025-03-30T16:59:15.447138198-1
     * 2025-03-31 01:59:15 request-server-1  | 2025-03-30T16:59:15.455Z  INFO 1 --- [nio-8080-exec-6] org.contourgara.ThreadSleepController    : Request ID: completable-future-2025-03-30T16:59:15.447138198-3
     * 2025-03-31 01:59:15 request-server-1  | 2025-03-30T16:59:15.455Z  INFO 1 --- [nio-8080-exec-4] org.contourgara.ThreadSleepController    : Request ID: completable-future-2025-03-30T16:59:15.447138198-2
     * 2025-03-31 01:59:20 call-api-app-1    | 2025-03-30T16:59:20.461Z  WARN 1 --- [nio-8080-exec-5] o.c.a.CompletableFutureUseCase           : {"requestId":"completable-future-2025-03-30T16:59:15.447138198-1"}
     * 2025-03-31 01:59:20 call-api-app-1    | 2025-03-30T16:59:20.461Z  WARN 1 --- [nio-8080-exec-5] o.c.a.CompletableFutureUseCase           : {"requestId":"completable-future-2025-03-30T16:59:15.447138198-2"}
     * 2025-03-31 01:59:20 call-api-app-1    | 2025-03-30T16:59:20.461Z  WARN 1 --- [nio-8080-exec-5] o.c.a.CompletableFutureUseCase           : {"requestId":"completable-future-2025-03-30T16:59:15.447138198-3"}
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
