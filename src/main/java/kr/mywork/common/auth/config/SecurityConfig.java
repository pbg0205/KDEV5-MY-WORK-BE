package kr.mywork.common.auth.config;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.mywork.common.api.components.filter.HttpLoggingFilter;
import kr.mywork.common.api.components.filter.RequestTrackingIdFilter;
import kr.mywork.domain.auth.service.JwtTokenProvider;
import kr.mywork.domain.auth.service.TokenAuthenticationService;
import kr.mywork.domain.member.model.MemberRole;
import kr.mywork.interfaces.auth.filter.JwtAuthenticationFilter;
import kr.mywork.interfaces.auth.filter.JwtLoginFilter;
import kr.mywork.interfaces.auth.handler.error.JwtAccessDeniedHandler;
import kr.mywork.interfaces.auth.handler.error.JwtAuthenticationEntryPoint;
import kr.mywork.interfaces.auth.handler.error.LoginFailureHandler;
import kr.mywork.interfaces.auth.handler.success.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

	private final JwtProperties jwtProperties;
	private final ObjectMapper objectMapper;
	private final AuthenticationProvider loginAuthenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.cors(httpSecurityCorsConfigurer ->
				httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
			.sessionManagement(sessionManagementConfigurer ->
				sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(authorize -> {
				authorize
					.requestMatchers("/api/login", "/api/reissue", "/swagger-ui/**", "/v3/api-docs/**",
						"/swagger-ui.html", "/swagger/**", "/actuator/**").permitAll()
					.requestMatchers("/api/reviews/**").hasAnyRole(
						MemberRole.SYSTEM_ADMIN.name(),
						MemberRole.USER.name(),
						MemberRole.DEV_ADMIN.name(),
						MemberRole.CLIENT_ADMIN.name())
					.requestMatchers(HttpMethod.POST, "/api/projects/steps").hasAnyRole(
						MemberRole.SYSTEM_ADMIN.name(), MemberRole.DEV_ADMIN.name())
					.requestMatchers(HttpMethod.PUT, "/api/projects/*/steps").hasAnyRole(
						MemberRole.SYSTEM_ADMIN.name(), MemberRole.DEV_ADMIN.name())
					.requestMatchers("/api/member/**").hasAnyRole(
						MemberRole.DEV_ADMIN.name(),
						MemberRole.CLIENT_ADMIN.name(),
						MemberRole.SYSTEM_ADMIN.name(),
						MemberRole.USER.name())
					.requestMatchers("/api/member/company/**").hasAnyRole(
						MemberRole.DEV_ADMIN.name(),
						MemberRole.CLIENT_ADMIN.name(),
						MemberRole.SYSTEM_ADMIN.name())
					.requestMatchers(HttpMethod.POST, "/api/companies/**").hasRole(MemberRole.SYSTEM_ADMIN.name())
					.requestMatchers(HttpMethod.DELETE, "/api/companies/**").hasRole(MemberRole.SYSTEM_ADMIN.name())
					.requestMatchers(HttpMethod.PUT, "/api/companies/**").hasAnyRole(
						MemberRole.SYSTEM_ADMIN.name(),
						MemberRole.DEV_ADMIN.name(),
						MemberRole.CLIENT_ADMIN.name())
					.requestMatchers(HttpMethod.GET, "/api/company/**")
					.hasAnyRole(
						MemberRole.SYSTEM_ADMIN.name(),
						MemberRole.DEV_ADMIN.name(),
						MemberRole.CLIENT_ADMIN.name())
					.anyRequest().authenticated();
			})
			.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new RequestTrackingIdFilter(), JwtAuthenticationFilter.class)
			.addFilterAfter(new HttpLoggingFilter(), RequestTrackingIdFilter.class)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(jwtAuthenticationEntryPoint())
				.accessDeniedHandler(jwtAccessDeniedHandler())
			);

		return http.build();
	}

	@Bean
	public JwtLoginFilter loginFilter() throws Exception {
		final JwtLoginFilter jwtLoginFilter = new JwtLoginFilter("/api/login", objectMapper);
		jwtLoginFilter.setAuthenticationSuccessHandler(jwtLoginAuthenticationSuccessHandler());
		jwtLoginFilter.setAuthenticationFailureHandler(jwtLoginAuthenticationFailureHandler());
		jwtLoginFilter.setAuthenticationManager(authenticationManager());
		return jwtLoginFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(List.of(loginAuthenticationProvider));
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwtProperties);
	}

	@Bean
	public AuthenticationSuccessHandler jwtLoginAuthenticationSuccessHandler() {
		return new LoginSuccessHandler(jwtTokenProvider(), objectMapper);
	}

	@Bean
	public AuthenticationFailureHandler jwtLoginAuthenticationFailureHandler() {
		return new LoginFailureHandler(objectMapper);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider(), tokenAuthenticationService(), objectMapper);
	}

	@Bean
	public TokenAuthenticationService tokenAuthenticationService() {
		return new TokenAuthenticationService(jwtTokenProvider());
	}

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint(objectMapper);
	}

	@Bean
	public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
		return new JwtAccessDeniedHandler(objectMapper);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(List.of("http://localhost:3000", "https://d16zykr4498a0c.cloudfront.net",
			"https://kbe-mywork.com", "https://www.kbe-mywork.com"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true); // 쿠키, 인증 정보 포함 시 필수

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);

		return source;
	}
}
