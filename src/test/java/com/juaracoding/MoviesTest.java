package com.juaracoding;
/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author Lenovo Gk a.k.a. Anna Syabilla
Java Developer
Created on 10/27/2024 5:49 PM
@Last Modified 10/27/2024 5:49 PM
Version 1.0
*/

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MoviesTest {
    String baseUrl = "https://api.themoviedb.org/3/movie/";
    String myToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMDkxOWE3YTI5Y2M2YTg2OTcxNGUwODZkNWNiZWU2YiIsIm5iZiI6MTcyOTg4OTk3Ny43NjU4NzMsInN1YiI6IjY3MWEyM2ZjNWJlOWU4NzU5ZGE2ZTk4YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.jObqflaP482fLLQ5vrOWXoW-AX2STDTNeo5o88g79rk";

    @Test
    public void testDetails(){
        int movieId = 912649; // ID film yang ingin diberi rating
        given()
                .pathParams("movie_id", movieId)
                .queryParam("language", "en-US")
                .header("Authorization", "Bearer " + myToken)
                .when()
                .get(baseUrl+"{movie_id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Venom: The Last Dance"))
                .body("release_date", equalTo("2024-10-22"))
                .body("status", equalTo("Released"))
                .body("tagline", equalTo("'Til death do they part."))
                .log().all();
    }

    @Test
    public void testDetailsNotFound(){
        int movieId = 1;
        given()
                .pathParams("movie_id", movieId)
                .queryParam("language", "en-US")
                .header("Authorization", "Bearer " + myToken)
                .when()
                .get(baseUrl+"{movie_id}")
                .then()
                .statusCode(404)
                .body("status_message", equalTo("The resource you requested could not be found."))
                .log().all();
    }



    @Test
    public void testAddRating() {
        int movieId = 912649;
        double ratingValue = 8.5;

        given()
                .pathParams("movie_id", movieId)
                .header("Authorization", "Bearer " + myToken)
                .contentType("application/json;charset=utf-8")
                .body("{\"value\":" + ratingValue + "}")
                .when()
                .post(baseUrl + "{movie_id}/rating")
                .then()
                .statusCode(201)
                .body("status_message", equalTo("The item/record was updated successfully."))
                .log().all();
    }

    @Test
    public void testAddRatingNegative() {
        int movieId = 912649;
        double ratingValue = -8.5;

        given()
                .pathParams("movie_id", movieId)
                .header("Authorization", "Bearer " + myToken)
                .contentType("application/json;charset=utf-8")
                .body("{\"value\":" + ratingValue + "}")
                .when()
                .post(baseUrl + "{movie_id}/rating")
                .then()
                .statusCode(400)
                .body("status_message", equalTo("Value too low: Value must be greater than 0.0."))
                .log().all();
    }
}
