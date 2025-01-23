package springbootkotlin.toy.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import springbootkotlin.toy.util.JwtUtil
import java.util.*

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil // JwtUtil 주입
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        logger.debug("token :: $token")

        if (token != null) {
            try {
                // JWT 디코딩
                val parts = token.split(".")
                if (parts.size == 3) {
                    val header = String(Base64.getUrlDecoder().decode(parts[0]))
                    val payload = String(Base64.getUrlDecoder().decode(parts[1]))
                    val signature = parts[2] // Header, Payload, Secret Key를 조합한 결과. HMAC 알고리즘(단방향) 암호화로, 복호화 불가

                    logger.debug("JWT Header: $header")
                    logger.debug("JWT Payload: $payload")
                    logger.debug("JWT signature: ${parts[2]}")
                } else {
                    logger.warn("JWT Token 형식이 올바르지 않습니다.")
                }

                // JWT 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    val claims = jwtUtil.getClaimsFromToken(token)
                    val userId = claims["userId"] as String
                    val authorities = (claims["roles"] as? List<String>)?.map { SimpleGrantedAuthority(it) } ?: emptyList()

                    val authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                logger.error("JWT 처리 중 오류 발생: ${e.message}")
            }
        }
        // 다음 필터로 이동
        filterChain.doFilter(request, response)
    }
}