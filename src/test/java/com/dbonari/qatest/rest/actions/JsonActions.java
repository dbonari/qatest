package com.dbonari.qatest.rest.actions;

import io.restassured.response.Response;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonActions {

    public String getUserId(Response userResponse) {
        String id = userResponse.jsonPath().getString("id");
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(id);
        matcher.find();
        return matcher.group();
    }

    public List<Integer> getPostIds(Response postsResponse) {
        return postsResponse.jsonPath().getList("id");
    }

    public List<String> getEmails(Response commentsResponse) {
        return commentsResponse.jsonPath().getList("email");
    }
}
