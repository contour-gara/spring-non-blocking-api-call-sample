package org.contourgara;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ThreadSleepController.class)
class ThreadSleepControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @ParameterizedTest
    @MethodSource("provider")
    void リクエストIDのJSONを返す(String requestId) {
        // execute & assert
        given()
                .header("X-Request-Id", requestId)
                .when()
                .get("/threadSleep")
                .then()
                .statusCode(200)
                .body("requestId", equalTo(requestId));
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("a")
        );
    }
}
