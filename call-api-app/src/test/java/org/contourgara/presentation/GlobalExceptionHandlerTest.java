package org.contourgara.presentation;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.contourgara.common.RequestId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // 子スレッドの例外を検証したいので、全結合でテスト
@WireMockTest(httpPort = 8081)
class GlobalExceptionHandlerTest {
    @LocalServerPort
    int port;

    @MockitoBean
    RequestId requestId;

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:" + port;
    }

    @Test
    void WebClientのエンドポイントで400の場合400を返す() {
        // setup
        doReturn("web-client").when(requestId).getRequestId();

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("web-client-1"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"web-client-1\"}")
                                        .withFixedDelay(10000)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("web-client-2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                        )
        );

        stubFor(
                get(urlEqualTo("/thread-sleep"))
                        .withHeader("X-Request-Id", equalTo("web-client-3"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"requestId\":\"web-client-3\"}")
                        )
        );

        // execute & assert
        given()
                .when()
                .get("/web-client")
                .then()
                .statusCode(400);
    }

    @Test
    void CompletableFutureのエンドポイントで400の場合400を返す() {
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
        given()
                .when()
                .get("/completable-future")
                .then()
                .statusCode(400);
    }

    @Test
    void VirtualThreadsのエンドポイントで400の場合400を返す() {
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
        given()
                .when()
                .get("/virtual-threads")
                .then()
                .statusCode(400);
    }
}
