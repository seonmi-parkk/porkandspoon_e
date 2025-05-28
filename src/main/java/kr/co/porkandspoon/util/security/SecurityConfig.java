package kr.co.porkandspoon.util.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
						  CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
		this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
		this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
	}

	// 암호화
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public AuthenticationProvider authenticationProvider(CustomUserDetailService userDetailService, PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder);

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider, CustomUserDetailService customUserDetailService) throws Exception {


		http.authorizeHttpRequests()
				.antMatchers("/resources/**", "/sidebar").permitAll() // resources 하위 폴더 허용
				.antMatchers("/", "/joinView", "/joinWrite", "/pageListCall", "/findUserId", "/sendMail", "/chackAuthCode", "/displayUserId/**", "/findPassword", "/changePassword", "/changePassword/**", "/timeoutAction").permitAll() // joinView, joinWrite 추후 제거
				.antMatchers("/sa/**").hasRole("SUPERADMIN")
				.antMatchers("/ad/**").hasAnyRole("SUPERADMIN", "ADMIN")
				.antMatchers("/ma/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.antMatchers("/us/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER", "USER")

				.anyRequest().authenticated(); // 그 외 요청은 검토

//		http.formLogin()
//        .loginPage("/") // 메인 페이지를 로그인 페이지로 설정
//        .loginProcessingUrl("/login") // 로그인 요청 처리 URL
//        .successHandler(new CustomAuthenticationSuccessHandler())
//    //    .failureHandler(new CustomAuthenticationSuccessHandler())
//        .failureUrl("/?error=true") // 로그인 실패 시 이동할 페이지
//        .usernameParameter("username")
//        .passwordParameter("password")
//        .and()
//        .logout()
//        .logoutSuccessUrl("/")
//        .invalidateHttpSession(true)
//        .deleteCookies("JSESSIONID");

		http.formLogin()
				.loginPage("/") // 메인 페이지를 로그인 페이지로 설정
				.loginProcessingUrl("/login") // 로그인 요청 처리 URL
				.successHandler(customAuthenticationSuccessHandler) // 주입된 성공 핸들러 사용
				.failureHandler(customAuthenticationFailureHandler) // 주입된 실패 핸들러 사용
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");

		http.csrf().ignoringAntMatchers("/wsConnect/**");
		// WebSocket 엔드포인트 제외
		// 개발 시 비활성화, 운영 시 활성화 검토
		http.httpBasic().disable().csrf().disable();


		http.authenticationProvider(authenticationProvider);

		return http.build();
	}


}
