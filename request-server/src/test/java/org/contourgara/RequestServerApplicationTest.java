package org.contourgara;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RequestServerApplicationTest {
    @Autowired
    Temp temp;

    @Test
    void contextLoads() {
        // assert
        assertThat(temp).isNotNull();
    }
}
