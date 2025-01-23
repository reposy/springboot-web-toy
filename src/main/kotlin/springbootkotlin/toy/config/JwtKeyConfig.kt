package springbootkotlin.toy.config

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
class JwtKeyConfig {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Bean
    fun jwtSigningKey(): SecretKey {
            // 문자열 키를 256비트 SecretKey로 변환
            val keyBytes = secret.toByteArray(Charsets.UTF_8) // 문자열을 바이트 배열로 변환
            val paddedKey = if (keyBytes.size < 32) {
                // 키가 32바이트 미만인 경우, 길이를 맞추기 위해 padding
                keyBytes.copyOf(32)
            } else {
                // 이미 충분히 긴 키라면 그대로 사용
                keyBytes
            }
            return Keys.hmacShaKeyFor(paddedKey)
            /*
            // Base64 디코딩 및 키 생성
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
             */
    }

    @Bean
    fun jwtExpiration(@Value("\${jwt.expiration}") expiration: Long): Long {
        return expiration
    }
}