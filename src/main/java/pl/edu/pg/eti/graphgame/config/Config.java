package pl.edu.pg.eti.graphgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class Config {

    private final String springMailHost;
    private final String springMailPort;
    private final String springMailUsername;
    private final String springMailPassword;

    @Autowired
    public Config(
            @Value("${spring.mail.host}") String springMailHost,
            @Value("${spring.mail.port}") String springMailPort,
            @Value("${spring.mail.username}") String springMailUsername,
            @Value("${spring.mail.password}") String springMailPassword
    ) {
        this.springMailHost = springMailHost;
        this.springMailPort = springMailPort;
        this.springMailUsername = springMailUsername;
        this.springMailPassword = springMailPassword;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(springMailHost);
        mailSender.setPort(Integer.parseInt(springMailPort));

        mailSender.setUsername(springMailUsername);
        mailSender.setPassword(springMailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
