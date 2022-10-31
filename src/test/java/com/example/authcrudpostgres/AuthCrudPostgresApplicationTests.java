package com.example.authcrudpostgres;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthCrudPostgresApplicationTests {

    @Autowired
    Environment environment;

    @Test
    public void whenConfiguredExcryptorUsed_ReturnCustomEncryptor() {
        //Environment environment = ApplicationContext..getBean(Environment.class);

        assertEquals(
                "root",
                environment.getProperty("spring.datasource.password"));
    }

}
