package org.contourgara.application;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.ThreadSleepClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebClientUseCase {
    private final ThreadSleepClient threadSleepClient;

    public WebClientUseCase(@Qualifier("web-client") ThreadSleepClient threadSleepClient) {
        this.threadSleepClient = threadSleepClient;
    }

    /**
     * 2025-03-31 01:15:42 call-api-app-1    | 2025-03-30T16:15:42.674Z  INFO 1 --- [nio-8080-exec-8] o.c.presentation.CallApiController       : Request ID: web-client-2025-03-30T16:15:42.674347762
     * 2025-03-31 01:15:42 request-server-1  | 2025-03-30T16:15:42.683Z  INFO 1 --- [nio-8080-exec-4] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T16:15:42.674347762-2
     * 2025-03-31 01:15:42 request-server-1  | 2025-03-30T16:15:42.683Z  INFO 1 --- [nio-8080-exec-6] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T16:15:42.674347762-3
     * 2025-03-31 01:15:42 request-server-1  | 2025-03-30T16:15:42.683Z  INFO 1 --- [nio-8080-exec-5] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T16:15:42.674347762-1
     * 2025-03-31 01:15:52 call-api-app-1    | 2025-03-30T16:15:52.689Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T16:15:42.674347762-2"}
     * 2025-03-31 01:15:52 call-api-app-1    | 2025-03-30T16:15:52.689Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T16:15:42.674347762-1"}
     * 2025-03-31 01:15:52 call-api-app-1    | 2025-03-30T16:15:52.689Z  WARN 1 --- [nio-8080-exec-8] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T16:15:42.674347762-3"}
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
