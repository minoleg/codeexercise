package com.ff.codeexercise;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CodeexerciseApplicationTests {

    
    @MockBean
    private Parser parser;

    @Test
    void contextLoads() {
    }

}
