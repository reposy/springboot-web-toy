package springbootkotlin.toy.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    private val jwtSigningKey: SecretKey,
    private val expiration: Long
) {

    fun generateToken(subject: String, claims: Map<String, Any>): String {
        val now = Date()
        return Jwts.builder()
            .subject(subject) // 주체
            .claims(claims) //
            .issuedAt(now)
            .expiration(Date(now.time + expiration * 1000)) // 만료 시간 설정
            .signWith(jwtSigningKey) // 서명 키 설정
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val parser = Jwts.parser() // parser() 메서드 사용
                .verifyWith(jwtSigningKey) // 서명 키 검증 설정
                .build()
            val claims = parser.parse(token).payload as Map<*, *>
            val expiration = claims["exp"] as Long
            expiration > System.currentTimeMillis() / 1000
        } catch (e: Exception) {
            false
        }
    }

    fun getClaimsFromToken(token: String): Claims {
        return try {
            Jwts.parser() // parser() 메서드 사용
                .verifyWith(jwtSigningKey) // 서명 키 검증 설정
                .build()
                .parseSignedClaims(token)
                //.parseUnsecuredClaims(token)
                //.parseClaimsJws(token)
                .payload
        }  catch (e: Exception) {
            throw IllegalArgumentException("Invalid or malformed token", e)
        }
    }
}