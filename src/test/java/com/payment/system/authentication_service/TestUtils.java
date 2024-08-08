package com.payment.system.authentication_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AuthenticationApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class TestUtils {

    protected final ObjectMapper objectMapper;

    public TestUtils() {
        this.objectMapper = new ObjectMapper();

    }

    protected File getMock(String path) {
        return new File(path);
    }

    protected void assertJsonEquals(String expected, String actual) throws JSONException {
        JSONAssert.assertEquals(
                expected,
                actual,
                false);
    }
}