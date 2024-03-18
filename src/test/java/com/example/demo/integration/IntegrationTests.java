package com.example.demo.integration;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
@Tag("Integracion")
@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTests {

    @Test
    public void test001HolaMundo(){

        RestAssured.reset();
        RestAssured.requestSpecification = new RequestSpecBuilder().
                    setBaseUri("http://localhost")
                    .setPort(8090)
                    .setBasePath("")
                    .build();
        given()
                .config(RestAssured.config())
                    .log().all()
                .when()
                    .get("")
                .then()
                    .log().all()
                    .assertThat().statusCode(200)
                .body(equalTo("hola mundo 2"));



    }
}
