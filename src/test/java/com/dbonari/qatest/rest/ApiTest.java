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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiTest {

    private static final Logger log = LogManager.getLogger(ApiTest.class);
    private final RestActions restActions = new RestActions();
    private final JsonActions jsonActions = new JsonActions();
    private List<String> emailsList = new ArrayList<>();
    private List<Executable> executables = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void beforeEach() {
        emailsList.clear();
        executables.clear();
    }

    @Test
    public void testValidateEmails() {
        Response userResponse = restActions.getUserByUsername("Delphine");
        String userId = jsonActions.getUserId(userResponse);
        Assertions.assertNotNull(userId);
        log.debug("userId={}", userId);

        Response postsResponse = restActions.getPostsByUserId(userId);
        List<Integer> postIds = jsonActions.getPostIds(postsResponse);
        log.debug("postIds={}", postIds);

        for (Integer postId : postIds) {
            Response commentsResponse = restActions.getCommentsByPostId(postId);
            emailsList.addAll(jsonActions.getEmails(commentsResponse));
        }

        for (String email : emailsList) {
            boolean isValid = ValidationUtils.isValidEmail(email);
            executables.add(() -> Assertions.assertTrue(isValid, email + " is not valid"));
            log.debug("{}: {}", email, isValid ? "is valid" : "is invalid");
        }

        Assertions.assertAll(executables.stream());
    }

    @Test
    public void testUserDoesNotExist() {
        Response userResponse = restActions.getUserByUsername("Denis");

        String userId = jsonActions.getUserId(userResponse);
        Assertions.assertNull(userId, "There should not be username Denis");
    }

    @Test
    public void testPostsDontExist() {
        Response postsResponse = restActions.getPostsByUserId("900");
        List<Integer> postIds = jsonActions.getPostIds(postsResponse);

        Assertions.assertEquals(postIds.size(), 0, "List of post ids should be empty for user id 900");
    }

    @Test
    public void testCommentsDontExist() {
        Response commentsResponse = restActions.getCommentsByPostId(Integer.MAX_VALUE);
        List<String> emails = jsonActions.getEmails(commentsResponse);

        Assertions.assertEquals(emails.size(), 0, "List of comments and emails should be empty for invalid post id");
    }

    @Test
    public void testInvalidPattern() {
        Response postsResponse = restActions.getPostsByUserId("1");
        List<Integer> postIds = jsonActions.getPostIds(postsResponse);

        postIds.forEach(pid -> {
            Response commentsByPostId = restActions.getCommentsByPostId(pid);
            emailsList.addAll(jsonActions.getEmails(commentsByPostId));
        });

        Pattern pattern = Pattern.compile("\\d@test.123");
        emailsList.forEach(email -> {
            Matcher matcher = pattern.matcher(email);
            executables.add(() -> Assertions.assertFalse(matcher.matches(), "@test.123 should be invalid domain"));
        });

        Assertions.assertAll(executables);
    }

}