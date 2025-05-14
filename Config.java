import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String getApiKey() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            return props.getProperty("api_key");
        } catch (IOException e) {
            throw new RuntimeException("API key could not be loaded: " + e.getMessage());
        }
    }
}
