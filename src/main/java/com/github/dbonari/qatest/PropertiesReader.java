package com.github.dbonari.qatest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String PATH = "src/main/resources/application.properties";
    private boolean loaded = false;
    private Properties properties = new Properties();

    private void init() {
        if (!loaded) {
            try {
                properties.load(new FileInputStream(PATH));
                loaded = true;
            } catch (IOException e) {
                System.out.println("Could not read file application.properties");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public String getProperty(Property property) {
        init();
        return properties.getProperty(property.getValue());
    }

    public enum Property {
        GET_USER("url.get.user"),
        GET_POSTS("url.get.posts"),
        GET_COMMENTS("url.get.comments"),
        REGEX_EMAIL("regex.email");

        private final String value;

        Property(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
