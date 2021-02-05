package com.dbonari.qatest.rest;

import com.dbonari.qatest.rest.actions.JsonActions;
import com.dbonari.qatest.rest.actions.RestActions;
import com.dbonari.qatest.rest.utils.ValidationUtils;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

public class ApiTest {

    private static final Logger log = LogManager.getLogger(ApiTest.class);
    private final RestActions restActions = new RestActions();
    private final JsonActions jsonActions = new JsonActions();
    private List<String> emailsList = new ArrayList<>();

    @BeforeAll
    static void setup() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void testValidateEmails() {
        Response userResponse = restActions.getUserByUsername("Delphine");
        String userId = jsonActions.getUserId(userResponse);
        log.debug("userId={}", userId);

        Response postsResponse = restActions.getPostsByUserId(userId);
        List<Integer> postIds = jsonActions.getPostIds(postsResponse);
        log.debug("postIds={}", postIds);

        for (Integer postId : postIds) {
            Response commentsResponse = restActions.getCommentsByPostId(postId);
            emailsList.addAll(jsonActions.getEmails(commentsResponse));
        }

        List<Executable> executables = new ArrayList<>();

        for (String email : emailsList) {
            boolean isValid = ValidationUtils.isValidEmail(email);
            executables.add(() -> Assertions.assertTrue(isValid, email + " is not valid"));
            log.debug("{}: {}", email, isValid ? "is valid" : "is invalid");
        }

        Assertions.assertAll(executables.stream());
    }
}