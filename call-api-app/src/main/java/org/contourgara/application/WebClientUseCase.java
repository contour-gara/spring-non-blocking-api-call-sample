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
     * 2025-03-31 03:25:46 call-api-app-1    | 2025-03-30T18:25:46.280Z  INFO 1 --- [nio-8080-exec-7] o.c.presentation.CallApiController       : Request ID: web-client-2025-03-30T18:25:46.280634825
     * 2025-03-31 03:25:46 request-server-1  | 2025-03-30T18:25:46.286Z  INFO 1 --- [io-8080-exec-10] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T18:25:46.280634825-2
     * 2025-03-31 03:25:46 request-server-1  | 2025-03-30T18:25:46.286Z  INFO 1 --- [nio-8080-exec-3] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T18:25:46.280634825-3
     * 2025-03-31 03:25:46 request-server-1  | 2025-03-30T18:25:46.286Z  INFO 1 --- [nio-8080-exec-1] org.contourgara.ThreadSleepController    : Request ID: web-client-2025-03-30T18:25:46.280634825-1
     * 2025-03-31 03:25:51 call-api-app-1    | 2025-03-30T18:25:51.291Z  WARN 1 --- [nio-8080-exec-7] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T18:25:46.280634825-1"}
     * 2025-03-31 03:25:51 call-api-app-1    | 2025-03-30T18:25:51.291Z  WARN 1 --- [nio-8080-exec-7] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T18:25:46.280634825-2"}
     * 2025-03-31 03:25:51 call-api-app-1    | 2025-03-30T18:25:51.291Z  WARN 1 --- [nio-8080-exec-7] o.c.application.WebClientUseCase         : {"requestId":"web-client-2025-03-30T18:25:46.280634825-3"}
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
