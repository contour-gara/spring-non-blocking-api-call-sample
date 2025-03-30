package org.contourgara.infrastructure;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.contourgara.common.RequestId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@WireMockTest(httpPort = 8081)
class AsyncThreadSleepClientTest {
    @Autowired
    @Qualifier("async")
    ThreadSleepClient sut;

    @MockitoBean
    RequestId requestId;

    @Test
    void apiが3回非同期で呼び出される() {
        // setup
        doReturn("async").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("async-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"async-1\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("async-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"async-2\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("async-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"async-3\"}")
                        )
        );

        // execute
        List<String> actual = sut.fetch();

        // assert
        List<String> expected = List.of("{\"requestId\":\"async-1\"}", "{\"requestId\":\"async-2\"}", "{\"requestId\":\"async-3\"}");
        assertThat(actual).isEqualTo(expected);
    }
}
