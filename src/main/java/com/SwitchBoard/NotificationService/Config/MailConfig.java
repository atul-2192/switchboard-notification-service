package com.SwitchBoard.NotificationService.Config;

import org.springframework.context.annotation.Configuration;

/**
 * Main Mail Configuration
 * 
 * This configuration is replaced by profile-specific configurations:
 * - GmailConfig.java for "local" and "dev" profiles (Gmail SMTP)
 * - SESConfig.java for "prod" profile (AWS SES)
 * 
 * The appropriate mail sender bean will be automatically loaded based on the active profile.
 */
@Configuration
public class MailConfig {
    // Configuration beans are now defined in profile-specific config classes
    // GmailConfig for local/dev environments
    // SESConfig for prod environment
}


