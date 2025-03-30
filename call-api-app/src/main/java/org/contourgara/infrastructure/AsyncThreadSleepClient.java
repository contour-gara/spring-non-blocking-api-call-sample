package org.contourgara.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Repository("async")
@RequiredArgsConstructor
public class AsyncThreadSleepClient implements ThreadSleepClient {
    private final AsyncApiCaller asyncApiCaller;

    @Override
    public List<String> fetch() {
        List<CompletableFuture<String>> futures = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> asyncApiCaller.callApi(String.valueOf(i)))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
