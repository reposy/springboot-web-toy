package springbootkotlin.toy.filter.password_encrypt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.filter.OncePerRequestFilter
import springbootkotlin.toy.filter.password_encrypt.wrapper.PasswordEncryptingWrapper

class PasswordEncryptingFilter(
    private val passwordEncoder: PasswordEncoder
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // JSON 데이터만 처리
        if (request.contentType == "application/json") {
            val wrappedRequest = PasswordEncryptingWrapper(request, passwordEncoder)
            filterChain.doFilter(wrappedRequest, response)
        } else
            filterChain.doFilter(request, response)
    }
}