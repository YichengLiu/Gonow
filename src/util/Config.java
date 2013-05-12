package util;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties = null;
    public static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
