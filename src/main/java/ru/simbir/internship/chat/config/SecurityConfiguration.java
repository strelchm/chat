package ru.simbir.internship.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.simbir.internship.chat.service.JwtTokenService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private JwtTokenService tokenService;

    @Autowired
    public SecurityConfiguration(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/v3/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                "/swagger-ui/index.html", "/swagger-ui/**", "/webjars/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers("/*", "/webjars/**","/ws/**",
                        "/api/login", "/api/login/*","/api/users", "/api/users/*").permitAll() // todo
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowCredentials(true)
                .allowedMethods(CorsConfiguration.ALL);
    }
}
