package org.contourgara.application;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.ThreadSleepClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncUseCase {
    private final ThreadSleepClient threadSleepClient;

    public AsyncUseCase(@Qualifier("async") ThreadSleepClient threadSleepClient) {
        this.threadSleepClient = threadSleepClient;
    }

    /**
     * 2025-03-31 03:08:03 call-api-app-1    | 2025-03-30T18:08:03.961Z  INFO 1 --- [nio-8080-exec-2] o.c.presentation.CallApiController       : Request ID: async-2025-03-30T18:08:03.958036356
     * 2025-03-31 03:08:04 request-server-1  | 2025-03-30T18:08:04.337Z  INFO 1 --- [io-8080-exec-10] org.contourgara.ThreadSleepController    : Request ID: async-2025-03-30T18:08:03.958036356-2
     * 2025-03-31 03:08:04 request-server-1  | 2025-03-30T18:08:04.337Z  INFO 1 --- [nio-8080-exec-1] org.contourgara.ThreadSleepController    : Request ID: async-2025-03-30T18:08:03.958036356-3
     * 2025-03-31 03:08:04 request-server-1  | 2025-03-30T18:08:04.337Z  INFO 1 --- [nio-8080-exec-9] org.contourgara.ThreadSleepController    : Request ID: async-2025-03-30T18:08:03.958036356-1
     * 2025-03-31 03:08:09 call-api-app-1    | 2025-03-30T18:08:09.367Z  WARN 1 --- [nio-8080-exec-2] o.contourgara.application.AsyncUseCase   : {"requestId":"async-2025-03-30T18:08:03.958036356-1"}
     * 2025-03-31 03:08:09 call-api-app-1    | 2025-03-30T18:08:09.368Z  WARN 1 --- [nio-8080-exec-2] o.contourgara.application.AsyncUseCase   : {"requestId":"async-2025-03-30T18:08:03.958036356-2"}
     * 2025-03-31 03:08:09 call-api-app-1    | 2025-03-30T18:08:09.368Z  WARN 1 --- [nio-8080-exec-2] o.contourgara.application.AsyncUseCase   : {"requestId":"async-2025-03-30T18:08:03.958036356-3"}
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
