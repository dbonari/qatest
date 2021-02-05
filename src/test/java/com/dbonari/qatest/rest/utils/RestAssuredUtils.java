package com.dbonari.qatest.rest.utils;

import com.github.dbonari.qatest.PropertiesReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssuredUtils {

    private static final PropertiesReader propertiesReader = new PropertiesReader();
    private static RequestSpecBuilder requestSpecification = null;
    private static ResponseSpecBuilder responseSpecification = null;
    private static final boolean LOG_ALL = Boolean.parseBoolean(propertiesReader.getProperty(PropertiesReader.Property.LOG_ALL));

    public static RequestSpecification getRequestSpec() {
        if (null == requestSpecification) {
            requestSpecification = new RequestSpecBuilder()
                .setBaseUri(propertiesReader.getProperty(PropertiesReader.Property.BASE_URL))
                .setUrlEncodingEnabled(false);
        }
        if (LOG_ALL) {
            requestSpecification.log(LogDetail.ALL);
        }
        return requestSpecification.build();
    }

    public static ResponseSpecification getResponseSpec() {
        if (null == responseSpecification) {
            responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON);
        }
        if (LOG_ALL) {
            responseSpecification.log(LogDetail.ALL);
        }
        return responseSpecification.build();
    }
}
