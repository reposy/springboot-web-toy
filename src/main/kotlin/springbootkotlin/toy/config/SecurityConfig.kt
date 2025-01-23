package springbootkotlin.toy.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import springbootkotlin.toy.filter.JwtAuthenticationFilter
import springbootkotlin.toy.filter.password_encrypt.PasswordEncryptingFilter
import springbootkotlin.toy.util.JwtUtil

@Configuration
@EnableWebSecurity
class SecurityConfig{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 비밀번호 암호화
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity, jwtUtil: JwtUtil, passwordEncoder: PasswordEncoder
    ): SecurityFilterChain {
        val jwtAuthenticationFilter = JwtAuthenticationFilter(jwtUtil) // JWT 필터 생성
        val passwordEncryptionFilter = PasswordEncryptingFilter(passwordEncoder) // PasswordEncryptionFilter 생성

        http.csrf { it.disable() } // CSRF 비활성화
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                }
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/users/auth/login_form",
//                        "/users/auth/login_form",
//                        "/admins/auth/login_form"
                    ).permitAll() // 로그인 페이지 접근 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            }
            // PasswordEncryptionFilter를 먼저 실행
            .addFilterBefore(passwordEncryptionFilter, UsernamePasswordAuthenticationFilter::class.java)
            // JwtAuthenticationFilter를 PasswordEncryptionFilter 이후 실행
            .addFilterAfter(jwtAuthenticationFilter, PasswordEncryptingFilter::class.java)

        return http.build()
    }
}
