package springbootkotlin.toy.util.jwt.config

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
class JwtKeyConfig {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Bean
    fun jwtSigningKey(): SecretKey {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }

    @Bean
    fun jwtExpiration(@Value("\${jwt.expiration}") expiration: Long): Long {
        return expiration
    }
}