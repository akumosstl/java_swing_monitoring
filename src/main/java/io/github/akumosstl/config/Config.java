package io.github.akumosstl.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Config {

    private static final Map<String, String> properties = new HashMap<>();

    public static synchronized void setProperties(Properties prop) {
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            properties.put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public static synchronized String getProperty(String key) {
        return properties.get(key);
    }
}

