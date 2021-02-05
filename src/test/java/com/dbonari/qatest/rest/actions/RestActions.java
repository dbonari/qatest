package com.dbonari.qatest.rest.actions;

import com.dbonari.qatest.rest.utils.RestAssuredUtils;
import com.github.dbonari.qatest.PropertiesReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.github.dbonari.qatest.PropertiesReader.Property.GET_COMMENTS;
import static com.github.dbonari.qatest.PropertiesReader.Property.GET_POSTS;
import static com.github.dbonari.qatest.PropertiesReader.Property.GET_USER;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class RestActions {

    private Map<String, Object> acceptHeaders = new HashMap<String, Object>() {{put("Accept", ContentType.JSON);}};
    private PropertiesReader propertiesReader = new PropertiesReader();

    public Response getUserByUsername(String username) {
        String url = format(propertiesReader.getProperty(GET_USER), username);
        return given().spec(RestAssuredUtils.getRequestSpec()).headers(acceptHeaders)
            .when().get(url)
            .then().spec(RestAssuredUtils.getResponseSpec()).extract().response();
    }

    public Response getPostsByUserId(String userId) {
        String url = format(propertiesReader.getProperty(GET_POSTS), userId);
        return given().spec(RestAssuredUtils.getRequestSpec()).headers(acceptHeaders)
            .when().get(url)
            .then().spec(RestAssuredUtils.getResponseSpec()).extract().response();
    }

    public Response getCommentsByPostId(Integer postId) {
        String url = format(propertiesReader.getProperty(GET_COMMENTS), postId);
        return given().spec(RestAssuredUtils.getRequestSpec()).headers(acceptHeaders)
            .when().get(url)
            .then().spec(RestAssuredUtils.getResponseSpec()).extract().response();
    }
}
