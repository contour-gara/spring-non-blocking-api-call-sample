package org.contourgara;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class WebfluxApplicationTest {
    @Autowired
    WebfluxController webfluxController;

    @Test
    void contextLoads() {
        assertThat(webfluxController).isNotNull();
    }
}
