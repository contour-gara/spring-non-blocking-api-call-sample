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
class VirtualThreadsThreadSleepClientTest {
    @Autowired
    @Qualifier("virtual-threads")
    ThreadSleepClient sut;

    @MockitoBean
    RequestId requestId;

    @Test
    void APIが3回非同期で呼び出される() {
        // setup
        doReturn("virtual-threads").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"virtual-threads-1\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"virtual-threads-2\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"virtual-threads-3\"}")
                        )
        );

        // execute
        List<String> actual = sut.fetch();

        // assert
        List<String> expected = List.of("{\"requestId\":\"virtual-threads-1\"}", "{\"requestId\":\"virtual-threads-2\"}", "{\"requestId\":\"virtual-threads-3\"}");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void _2回目のAPI呼び出しで400が返った場合例外を投げる() {
        // setup
        doReturn("virtual-threads").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"virtual-threads-1\"}")
                                        .withFixedDelay(10000)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("virtual-threads-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"virtual-threads-3\"}")
                        )
        );

        // execute & assert
        assertThatThrownBy(() -> sut.fetch())
                .isInstanceOf(RuntimeException.class)
                .isExactlyInstanceOf(CompletionException.class);
    }
}
