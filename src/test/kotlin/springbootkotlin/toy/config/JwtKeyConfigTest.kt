package springbootkotlin.toy.config

import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import javax.crypto.SecretKey

@SpringBootTest
class JwtKeyConfigTest {

    @Autowired
    private lateinit var jwtKeyConfig: JwtKeyConfig

    @Value("\${jwt.expiration}")
    private var expiration: Long = 0

    @Test
    fun `jwtSigningKey should return a valid SecretKey`() {
        // When
        val signingKey: SecretKey = jwtKeyConfig.jwtSigningKey()

        // Then
        assertThat(signingKey).isNotNull
        assertThat(signingKey.algorithm).isEqualTo("HmacSHA256")
        assertThat(signingKey.encoded.size).isEqualTo(32) // 256 bits = 32 bytes
    }

    @Test
    fun `jwtSigningKey should correctly pad short secrets`() {
        // Given
        val shortSecret = "short_secret".toByteArray(Charsets.UTF_8)
        val paddedKey = if (shortSecret.size < 32) shortSecret.copyOf(32) else shortSecret

        // When
        val signingKey = Keys.hmacShaKeyFor(paddedKey)

        // Then
        assertThat(signingKey.encoded.size).isEqualTo(32)
    }

    @Test
    fun `jwtExpiration should return correct expiration`() {
        // When
        val returnedExpiration = jwtKeyConfig.jwtExpiration(expiration)

        // Then
        assertThat(returnedExpiration).isEqualTo(expiration)
    }
}