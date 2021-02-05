package com.dbonari.qatest.rest;

import com.dbonari.qatest.rest.actions.RestActions;
import com.dbonari.qatest.rest.utils.ValidationUtils;
import com.github.dbonari.qatest.model.Comment;
import com.github.dbonari.qatest.model.Post;
import com.github.dbonari.qatest.model.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelsTest {

    private final RestActions restActions = new RestActions();
    private List<String> emailsList = new ArrayList<>();
    private List<Executable> executables = new ArrayList<>();

    @Test
    public void testGetUser() {
        Response userResponse = restActions.getUserByUsername("Delphine");
        User user = userResponse.as(User[].class)[0];

        Assertions.assertEquals(user.getId(), 9);
        Assertions.assertEquals(user.getUsername(), "Delphine");
    }

    @Test
    public void testUserDoesntExist() {
        Response userByUsername = restActions.getUserByUsername("Denis");
        User[] users = userByUsername.as(User[].class);

        Assertions.assertEquals(users.length, 0, "Users' list should be empty");
    }

    @Test
    public void testInvalidUserId() {
        Response postsByUserId = restActions.getPostsByUserId("-1");
        Post[] posts = postsByUserId.as(Post[].class);

        Assertions.assertEquals(posts.length, 0, "Posts' list should be empty");
    }

    @Test
    public void testInvalidPostId() {
        Response commentsByPostId = restActions.getCommentsByPostId(-1);
        Comment[] comments = commentsByPostId.as(Comment[].class);

        Assertions.assertEquals(comments.length, 0, "Comments' list should be empty");
    }

    @Test
    public void testValidateEmails() {
        Response userResponse = restActions.getUserByUsername("Delphine");
        User[] users = userResponse.as(User[].class);
        Assertions.assertEquals(users.length, 1, "There should be exactly 1 user with username Delphine");

        Response postsByUserId = restActions.getPostsByUserId(Integer.toString(users[0].getId()));
        Post[] posts = postsByUserId.as(Post[].class);
        Assertions.assertTrue(posts.length > 0, "There should be at least 1 post");

        List<Integer> postIds = Arrays.stream(posts).map(Post::getId).collect(Collectors.toList());

        postIds.forEach(pid -> {
            Response commentsByPostId = restActions.getCommentsByPostId(pid);
            Comment[] commentsForPost = commentsByPostId.as(Comment[].class);
            emailsList.addAll(Arrays.stream(commentsForPost).map(Comment::getEmail).collect(Collectors.toList()));
        });

        emailsList.forEach(email -> {
            boolean isValid = ValidationUtils.isValidEmail(email);
            executables.add(() -> Assertions.assertTrue(isValid, email + " is invalid"));
        });

        Assertions.assertAll(executables);
    }
}
