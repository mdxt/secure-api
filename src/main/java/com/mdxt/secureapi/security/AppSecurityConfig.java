package com.mdxt.secureapi.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mdxt.secureapi.security.jwt.JwtConfig;
import com.mdxt.secureapi.security.jwt.JwtTokenVerifier;
import com.mdxt.secureapi.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		//the following works as a pipe. so look at the top to bottom flow
		//the situations/URLs that aren't "caught" at a level are passed down to the next
		http.cors()
			.and()
			.csrf().disable() 		//TODO : delete
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
			.addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
			.authorizeRequests()	//allows restricting access based on URL patterns
			
			.antMatchers("/api/public/**")	//matches URLs for public tests
				.permitAll()		//allows all URLs matching previous pattern
			
			.antMatchers("/api/user/**")
				.hasRole(RolesEnum.USER.name())
			
			.antMatchers("/api/underwriter/**")
				.hasRole(RolesEnum.UNDERWRITER.name())
				
			.antMatchers("/api/admin/**")
				.hasRole(RolesEnum.ADMIN.name())	//only allows admin role
			
			.anyRequest()			//for the URLs not handled by previous steps, 
									//maps any request, i.e. any URL pattern
			.authenticated();		//allows only authenticated users for the mapped URLs
			
//			.and()
//			.httpBasic();			// Basic auth- base64 encoded uname and pwd sent with every request
//			//.formLogin();
	}

//	@Override
//	@Bean
//	protected UserDetailsService userDetailsService() {
//		// Spring will use the returned service only to retrieve user details
//		 
////		//building a sample user
////		UserDetails testUser =	User.builder()
////									.username("username")
////									.password(passwordEncoder().encode("password")) 
////										//if no encoder used, accessing with password wont work
////										//must have a PasswordEncoder mapped
////									.roles(RolesEnum.USER.name())
////									.build();
////		
////		UserDetails testAdminUser =	User.builder()
////									.username("admin")
////									.password(passwordEncoder().encode("admin")) 
////										//if no encoder used, accessing with password wont work
////										//must have a PasswordEncoder mapped
////									.roles(RolesEnum.ADMIN.name())
////									.build();
////		
////		//we need a class that implements the UserDetailsService interface and add our user to it
////		return new InMemoryUserDetailsManager(testUser, testAdminUser);
//	
//		//for custom users, implement the UserDetails interface
//	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    List<String> allowOrigins = Arrays.asList("*");//http://localhost:4200");
	    List<String> allowAll = Arrays.asList("*");
	    configuration.setAllowedOriginPatterns(allowOrigins);
	    configuration.setAllowedMethods(allowAll);
	    configuration.setAllowedHeaders(allowAll);
	    //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
	    configuration.setAllowCredentials(true);
	    configuration.setExposedHeaders(allowAll);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

	
}
