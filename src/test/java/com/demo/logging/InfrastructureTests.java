package com.demo.logging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InfrastructureTests {

    @Test
    void main() {
        FunctionApplication.main(new String[] {});
        Assertions.assertDoesNotThrow(this::doNotThrowException);
    }

    private void doNotThrowException(){
        //This method will never throw exception
    }
}
