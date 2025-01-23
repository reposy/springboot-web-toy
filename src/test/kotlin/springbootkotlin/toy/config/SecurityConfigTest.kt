package springbootkotlin.toy.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import springbootkotlin.toy.util.JwtUtil

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val jwtUtil: JwtUtil
) {

    @Test
    fun `PasswordEncoder should be available`() {
        // Check if the password encoder is not null
        assertThat(passwordEncoder).isNotNull

        // Check password encoding
        val rawPassword = "testpassword"
        val encodedPassword = passwordEncoder.encode(rawPassword)
        assertThat(encodedPassword).isNotNull
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue
    }

    @Test
    fun `JWT token generation and validation`() {
        // Generate a token
        val claims = mapOf("userId" to "testuser", "roles" to listOf("ROLE_USER"))
        val token = jwtUtil.generateToken("testuser", claims)

        // Validate token
        assertThat(token).isNotNull
        assertThat(jwtUtil.validateToken(token)).isTrue

        // Extract claims
        val extractedClaims = jwtUtil.getClaimsFromToken(token)
        assertThat(extractedClaims).isNotNull
        assertThat(extractedClaims["userId"]).isEqualTo("testuser")
        assertThat(extractedClaims["roles"]).isEqualTo(listOf("ROLE_USER"))
    }

    @Test
    fun `Public endpoints should be accessible without authentication`() {
        // Access public endpoint
        val response: ResponseEntity<String> = restTemplate.getForEntity("/users/auth/login_form", String::class.java)
        // Assert status
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Protected endpoints should require authentication`() {
        // Access protected endpoint without token
        val response: ResponseEntity<String> = restTemplate.getForEntity("/protected-endpoint", String::class.java)

        // Assert unauthorized
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `JWT-protected endpoints should work with valid tokens`() {
        // Generate a valid token
        val claims = mapOf("userId" to "testuser", "roles" to listOf("ROLE_USER"))
        val token = jwtUtil.generateToken("testuser", claims)

        // Set the Authorization header with the token
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $token")
        }
        val entity = org.springframework.http.HttpEntity<String>(headers)

        // Access protected endpoint with token
        val response: ResponseEntity<String> =
            restTemplate.exchange("/users", HttpMethod.GET, entity, String::class.java)

        // Assert status
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
