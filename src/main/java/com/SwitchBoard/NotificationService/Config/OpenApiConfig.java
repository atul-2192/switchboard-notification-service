package com.SwitchBoard.NotificationService.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration using annotations (simpler and more reliable approach)
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SwitchBoard Notification Service api",
                description = "API documentation for the Switchboard notification Service - Notification Management System",
                version = "1.0.0",
                contact = @Contact(
                        name = "SwitchBoard",
                        email = "support@switchbiard.com",
                        url = "https://www.switchboard.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)
public class OpenApiConfig {
    // Using annotation-based configuration instead of bean-based
}
