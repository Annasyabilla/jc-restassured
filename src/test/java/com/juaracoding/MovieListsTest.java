package com.juaracoding;
/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author Lenovo Gk a.k.a. Anna Syabilla
Java Developer
Created on 10/27/2024 3:45 PM
@Last Modified 10/27/2024 3:45 PM
Version 1.0
*/

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieListsTest {
    String baseUrl = "https://api.themoviedb.org/3/movie/";
    String myToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMDkxOWE3YTI5Y2M2YTg2OTcxNGUwODZkNWNiZWU2YiIsIm5iZiI6MTcyOTg4OTk3Ny43NjU4NzMsInN1YiI6IjY3MWEyM2ZjNWJlOWU4NzU5ZGE2ZTk4YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.jObqflaP482fLLQ5vrOWXoW-AX2STDTNeo5o88g79rk";


    @Test
    public void testNowPlaying() {
        given()
                .queryParam("language", "en-US")
                .queryParam("page", "1")
                .header("Authorization", "Bearer " + myToken)
                .when()
                .get(baseUrl + "now_playing")
                .then()
                .statusCode(200)
                .body("results.title[0]",equalTo("Venom: The Last Dance"))
                .body("results.title", everyItem(notNullValue()))
                .log().all();

    }

    @Test
    public void testMoviePopular() {
        given()
                .queryParam("language", "en-US")
                .queryParam("page", "1")
                .header("Authorization", "Bearer " + myToken)
                .when()
                .get(baseUrl + "popular")
                .then()
                .statusCode(200)
                .body("results.title[0]",equalTo("Venom: The Last Dance"))
                .body("results", notNullValue())
                .body("results.title", everyItem(notNullValue()))
                .log().all();
    }


    //negative test
    @Test
    public void testWithoutToken() {
        given()
                .queryParam("language", "en-US")
                .queryParam("page", "1")
                .when()
                .get(baseUrl + "popular")
                .then()
                .statusCode(401)
                .body("status_message", equalTo("Invalid API key: You must be granted a valid key."))
                .log().all();
    }

    @Test
    public void testMoviePopularNotFound() {
        given()
                .queryParam("language", "en-US")
                .queryParam("page", "-2")
                .header("Authorization", "Bearer " + myToken)
                .when()
                .get(baseUrl + "popular")
                .then()
                .statusCode(400)
                .body("status_message", equalTo("Invalid page: Pages start at 1 and max at 500. They are expected to be an integer."))
                .log().all();
    }
}
