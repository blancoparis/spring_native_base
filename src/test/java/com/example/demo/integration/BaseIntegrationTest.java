package com.example.demo.integration;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

@Tag("Integracion")
@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class BaseIntegrationTest {

    @Test
     void test001HolaMundo(){
        RestAssured.reset();
        RestAssured.requestSpecification = new RequestSpecBuilder().
                    setBaseUri("http://localhost")
                    .setPort(8090)
                    .setBasePath("")
                    .build();
        RestAssured.given()
                .config(RestAssured.config())
                    .log().all()
                .when()
                    .get("")
                .then()
                    .log().all()
                    .assertThat().statusCode(200)
                .body(
                        Matchers.equalTo("hola mundo 2")
                );
    }
}
