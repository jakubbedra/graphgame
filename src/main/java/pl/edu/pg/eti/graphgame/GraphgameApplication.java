package pl.edu.pg.eti.graphgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.edu.pg.eti.graphgame.config.TestDataHardcoder;

@SpringBootApplication
public class GraphgameApplication {

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-tu")) {
                // generate test users
                TestDataHardcoder.enable();
            }
        }
        SpringApplication.run(GraphgameApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api").allowedOrigins("*");
            }
        };
    }
}
