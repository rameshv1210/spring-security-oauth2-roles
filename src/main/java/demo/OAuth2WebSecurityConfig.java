package demo;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = "pass";
        String user = "user";
        auth.inMemoryAuthentication()
            .withUser(user).password(password).roles("USER")
            .and().withUser("admin").password("admin").roles("ADMIN");
        
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/reg/**").hasRole("ADMIN");
    }
}
