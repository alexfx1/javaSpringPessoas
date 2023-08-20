package io.github.alexfx1.domain.config;

import io.github.alexfx1.domain.security.JwtAuthFilter;
import io.github.alexfx1.domain.security.JwtService;
import io.github.alexfx1.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/pessoa/**").hasAnyRole("AMIN", "USER")
                .antMatchers("/api/endereco/**").hasAnyRole("AMIN", "USER")
                .antMatchers("/api/pedido/**").hasAnyRole("AMIN", "USER")
                .antMatchers("/api/produto/**").hasAnyRole("AMIN", "USER")
                .antMatchers(HttpMethod.POST,"/api/usuario/**").permitAll()
                .antMatchers(HttpMethod.PATCH,"/api/usuario/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/usuario/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/h2-console**",
                "/h2-console/**");
    }
}
