package org.contourgara;

import org.contourgara.presentation.CallApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CallApiApplicationTest {
    @Autowired
    CallApiController callApiController;

    @Test
    void contextLoads() {
        assertThat(callApiController).isNotNull();
    }
}
