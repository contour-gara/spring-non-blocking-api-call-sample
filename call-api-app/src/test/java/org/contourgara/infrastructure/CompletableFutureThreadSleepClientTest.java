package org.contourgara.infrastructure;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.contourgara.common.RequestId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.concurrent.CompletionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@WireMockTest(httpPort = 8081)
class CompletableFutureThreadSleepClientTest {
    @Autowired
    @Qualifier("completable-future")
    ThreadSleepClient sut;

    @MockitoBean
    RequestId requestId;

    @Test
    void APIが3回非同期で呼び出される() {
        // setup
        doReturn("completable-future").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"completable-future-1\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"completable-future-2\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"completable-future-3\"}")
                        )
        );

        // execute
        List<String> actual = sut.fetch();

        // assert
        List<String> expected = List.of("{\"requestId\":\"completable-future-1\"}", "{\"requestId\":\"completable-future-2\"}", "{\"requestId\":\"completable-future-3\"}");
        assertThat(actual).isEqualTo(expected);
     }

    @Test
    void _2回目のAPI呼び出しで400が返った場合例外を投げる() {
        // setup
        doReturn("completable-future").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"completable-future-1\"}")
                                        .withFixedDelay(10000)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("completable-future-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"completable-future-3\"}")
                        )
        );

        // execute & assert
        assertThatThrownBy(() -> sut.fetch())
                .isInstanceOf(RuntimeException.class)
                .isExactlyInstanceOf(CompletionException.class);
    }
}
