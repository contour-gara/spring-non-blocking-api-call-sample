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
class WebClientThreadSleepClientTest {
    @Autowired
    @Qualifier("webclient")
    ThreadSleepClient sut;

    @MockitoBean
    RequestId requestId;

    @Test
    void apiが3回非同期で呼び出される() {
        // setup
        doReturn("webclient").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("webclient-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"webclient-1\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("webclient-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"webclient-2\"}")
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("webclient-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"webclient-3\"}")
                        )
        );

        // execute
        List<String> actual = sut.fetch();

        // assert
        List<String> expected = List.of("{\"requestId\":\"webclient-1\"}", "{\"requestId\":\"webclient-2\"}", "{\"requestId\":\"webclient-3\"}");
        assertThat(actual).isEqualTo(expected);
    }
}
