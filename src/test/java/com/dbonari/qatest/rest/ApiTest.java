package com.dbonari.qatest.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class ApiTest {

    private static final String GET_USER = "https://jsonplaceholder.typicode.com/users?username=Delphine";
    private static final String GET_POSTS = "https://jsonplaceholder.typicode.com/users/%s/posts";
    private static final String GET_COMMENTS = "https://jsonplaceholder.typicode.com/posts/%s/comments";
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Test
    public void testValidateEmails() {
        RestAssured.defaultParser = Parser.JSON;

        Map<String, Object> acceptHeader = new HashMap<>();
        acceptHeader.put("Accept", ContentType.JSON);

        Response userResponse = given().headers(acceptHeader).when().get(GET_USER)
            .then().statusCode(200).contentType(ContentType.JSON).extract().response();

        String id = userResponse.jsonPath().getString("id").replaceAll("\\[", "").replaceAll("\\]", "");
        System.out.println(id);

        Response postsResponse = given().headers(acceptHeader).when().get(String.format(GET_POSTS, id))
            .then().statusCode(200).contentType(ContentType.JSON).extract().response();

        List<Integer> postIds = postsResponse.jsonPath().getList("id");
        System.out.println(postIds);

        List<String> emailsList = new ArrayList<>();
        for (Integer postId : postIds) {
            Response commentsResponse = given().headers(acceptHeader).when().get(String.format(GET_COMMENTS, postId))
                .then().statusCode(200).contentType(ContentType.JSON).extract().response();

            emailsList.addAll(commentsResponse.jsonPath().getList("email"));
        }

        for (String email : emailsList) {
            Matcher matcher = pattern.matcher(email);
            boolean matches = matcher.matches();
            Assertions.assertTrue(matches, email + " is not valid");
        }
    }
}
