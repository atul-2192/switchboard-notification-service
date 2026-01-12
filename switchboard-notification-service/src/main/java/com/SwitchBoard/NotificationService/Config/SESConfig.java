package com.SwitchBoard.NotificationService.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * AWS SES Configuration for production environment
 * This configuration is active when the profile is "prod"
 */
@Configuration
@Profile("prod")
public class SESConfig {

    @Value("${aws.ses.region:us-east-1}")
    private String awsRegion;

    @Value("${ses.access-key}")
    private String awsAccessKey;

    @Value("${ses.secret-key}")
    private String awsSecretKey;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    /**
     * Creates and configures AWS SES client
     * @return Configured SesClient instance
     */
    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                awsAccessKey,
                awsSecretKey
        );

        return SesClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    /**
     * Bean to provide the configured sender email address
     * @return The email address to use as sender
     */
    @Bean
    public String sesFromEmail() {
        return fromEmail;
    }
}
