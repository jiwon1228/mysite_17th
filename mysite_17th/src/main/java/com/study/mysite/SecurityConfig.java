package com.study.mysite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
//@PreAuthorize("isAuthenticated()")를 사용하려면 @EnableMethodSecurity(prePostEnabled = true)이 꼭 필요하다
public class SecurityConfig {
   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .authorizeHttpRequests((requests) -> requests //requests:authorizeHttpRequests
            .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
         //h2 들어갈 수 있도록 설정
         .csrf((csrf)->csrf //보안과 관련된 부분을 처리
         .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
         .headers((headers)-> headers.addHeaderWriter
         (new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
         //XFrameOptionsHeader값으로 SAMEORIGIN을 설정하면 프레임에 포함된 그 페이지가 동일한 사이트에서 제공할때만 사용이 허락된다.
			.formLogin((form) -> form
					.loginPage("/user/login")
					.defaultSuccessUrl("/")) //로그인 성공 후 갈 페이지
				//로그아웃
				.logout((logout) -> logout
						.logoutRequestMatcher(new 
						AntPathRequestMatcher("/user/logout"))
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true));
      					//로그아웃 시 생성된 사용자 세션도 삭제처리하겠다.
      
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