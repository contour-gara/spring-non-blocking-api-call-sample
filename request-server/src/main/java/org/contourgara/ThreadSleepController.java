package org.contourgara;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@Slf4j
public class ThreadSleepController {
    @GetMapping("/thread-sleep")
    Map<String, String> threadSleep(@RequestHeader("X-Request-Id") String requestId) throws InterruptedException {
        log.info("Request ID: {}", requestId);
        Thread.sleep(10000);
        return Map.of("requestId", requestId);
    }
}
