package com.alto.id.bifast.simulator.security.jwt;

import com.alto.id.bifast.simulator.config.SecurityConfiguration;
import com.alto.id.bifast.simulator.config.SecurityJwtConfiguration;
import com.alto.id.bifast.simulator.config.WebConfigurer;
import com.alto.id.bifast.simulator.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}