package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	@Qualifier("accessDeniedHandler") 
	OAuth2AccessDeniedHandler accessDeniedHandler;
	

	@Autowired
	@Qualifier("authenticationEntryPoint")
	OAuth2AuthenticationEntryPoint authenticationEntryPoint;

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}
		
		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			security.checkTokenAccess("isAuthenticated()");
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
		 	clients.inMemory()
		        .withClient("my-client-with-secret")
		            .authorizedGrantTypes("client_credentials", "password")
		            .authorities("ROLE_CLIENT")
		            .scopes("read")
		            .resourceIds("oauth2-resource")
		            .secret("secret");
		// @formatter:on
		}
		
		@Bean
		public OAuth2AccessDeniedHandler accessDeniedHandler(){
			final OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
			//accessDeniedHandler.setExceptionTranslator(exceptionTranslator());
			return accessDeniedHandler;
		}
		
		@Bean
		public OAuth2AuthenticationEntryPoint authenticationEntryPoint(){
			final OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
			//entryPoint.setExceptionTranslator(exceptionTranslator());
			return entryPoint;		
		}

	}
	
	@Configuration
	@EnableResourceServer
	protected static class ApiSecurityConfig extends ResourceServerConfigurerAdapter{
		@Autowired
		@Qualifier("accessDeniedHandler") 
		OAuth2AccessDeniedHandler accessDeniedHandler;
		

		@Autowired
		@Qualifier("authenticationEntryPoint")
		OAuth2AuthenticationEntryPoint authenticationEntryPoint;
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
		}
	}

}
