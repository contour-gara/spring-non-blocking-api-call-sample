package org.contourgara.application;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.ThreadSleepClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VirtualThreadsUseCase {
    private final ThreadSleepClient threadSleepClient;

    public VirtualThreadsUseCase(@Qualifier("virtual-threads") ThreadSleepClient threadSleepClient) {
        this.threadSleepClient = threadSleepClient;
    }

    /**
     */
    public void execute() {
        threadSleepClient.fetch()
                .forEach(log::warn);
    }
}
