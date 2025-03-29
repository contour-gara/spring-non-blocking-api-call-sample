package org.contourgara;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ThreadSleepController {
    @GetMapping("/threadSleep")
    Map<String, String> threadSleep(@RequestHeader("X-Request-Id") String requestId) throws InterruptedException {
        Thread.sleep(10000);
        return Map.of("requestId", requestId);
    }
}
