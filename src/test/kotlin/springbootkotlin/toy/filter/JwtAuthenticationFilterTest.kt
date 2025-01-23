package springbootkotlin.toy.filter

import io.jsonwebtoken.Jwts
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder
import springbootkotlin.toy.util.JwtUtil

class JwtAuthenticationFilterTest {

    private lateinit var jwtUtil: JwtUtil
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @BeforeEach
    fun setUp() {
        jwtUtil = mockk()
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtUtil)

        // Reset SecurityContext before each test
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should set authentication when token is valid`() {
        // Arrange
        val token = "validToken"

        // Use `JwtClaimsBuilder` to create claims
        val claims = Jwts.claims().apply {
            this["userId"] = "testuser"
            this["roles"] = listOf("ROLE_USER")
        }

        every { jwtUtil.validateToken(token) } returns true
        every { jwtUtil.getClaimsFromToken(token) } returns claims

        val request = mockk<HttpServletRequest>(relaxed = true) {
            every { getHeader("Authorization") } returns "Bearer $token"
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val filterChain = mockk<FilterChain>(relaxed = true)

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // Assert
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNotNull
        assertThat(authentication.name).isEqualTo("testuser")
        assertThat(authentication.authorities).extracting("authority").contains("ROLE_USER")

        verify { filterChain.doFilter(request, response) }
    }

    @Test
    fun `should not set authentication when token is invalid`() {
        // Arrange
        val token = "invalidToken"

        every { jwtUtil.validateToken(token) } returns false

        val request = mockk<HttpServletRequest>(relaxed = true) {
            every { getHeader("Authorization") } returns "Bearer $token"
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val filterChain = mockk<FilterChain>(relaxed = true)

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // Assert
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNull()

        verify { filterChain.doFilter(request, response) }
    }

    @Test
    fun `should handle missing token gracefully`() {
        // Arrange
        val request = mockk<HttpServletRequest>(relaxed = true) {
            every { getHeader("Authorization") } returns null
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val filterChain = mockk<FilterChain>(relaxed = true)

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // Assert
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNull()

        verify { filterChain.doFilter(request, response) }
    }
}