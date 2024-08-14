package com.study.mysite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .authorizeHttpRequests((requests) -> requests //requests:authorizeHttpRequests
            .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
         //h2 들어갈 수 있도록 설정
         .csrf((csrf)->csrf
         .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
         //XFrameOptionsHeader값으로 SAMEORIGIN을 설정하면 프레임에 포함된 그 페이지가 동일한 사이트에서 제공할때만 사용이 허락된다.
         .headers((headers)-> 
         headers.addHeaderWriter
         (new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			.formLogin((form) -> form
					.loginPage("/user/login")
					.defaultSuccessUrl("/"))
				.logout((logout) -> logout.permitAll());
      
      return http.build();
   }
   
   @Bean
   PasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
   }
   
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
		//사용자 인증과 권한 부여 프로세스를 내부적으로 처리한다.
	}
}