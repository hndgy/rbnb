package fr.orleans.univ.miage.m2.rbnbmonolithique.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true) // pour utiliser @RoleAllowed dans le controller
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
