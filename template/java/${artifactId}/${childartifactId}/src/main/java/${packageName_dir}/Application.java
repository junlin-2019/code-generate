package $ import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

{packageName};
/**
 * @author seed
 *
 */
@SpringBootApplication(scanBasePackages = { "com.example" })
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

}
