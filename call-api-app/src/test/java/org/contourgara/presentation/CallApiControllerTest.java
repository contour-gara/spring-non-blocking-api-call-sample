package org.contourgara.presentation;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.contourgara.application.AsyncUseCase;
import org.contourgara.application.CompletableFutureUseCase;
import org.contourgara.application.WebClientUseCase;
import org.contourgara.common.RequestId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.*;

@WebMvcTest(CallApiController.class)
class CallApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    WebClientUseCase webClientUseCase;
    @MockitoBean
    CompletableFutureUseCase completableFutureUseCase;
    @MockitoBean
    AsyncUseCase asyncUseCase;
    @MockitoBean
    RequestId requestId;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        doReturn("test").when(requestId).getRequestId();
    }

    @Test
    void webclientエンドポイントにアクセスした場合200が返る() {
        // execute & assert
        given()
                .when()
                .get("/web-client")
                .then()
                .statusCode(200);
    }

//    @Test
//    void completablefutureエンドポイントにアクセスした場合200が返る() {
//        // execute & assert
//        given()
//                .when()
//                .get("/completable-future")
//                .then()
//                .statusCode(200);
//    }
}
