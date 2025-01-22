package springbootkotlin.toy.user.login.controller.api

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springbootkotlin.toy.user.login.controller.api.req.UserLoginApiRequest
import springbootkotlin.toy.user.login.controller.api.res.UserLoginApiResponse
import springbootkotlin.toy.user.login.service.UserAuthService
import springbootkotlin.toy.util.jwt.JwtUtil

@RestController
@RequestMapping("/api/login")
class UserLoginApiController(
    private val userAuthService: UserAuthService,
    private val jwtUtil: JwtUtil
) {
    // Session 로그인
    @PostMapping("/session")
    fun loginWithSession(
        @RequestBody request: UserLoginApiRequest,
        session: HttpSession
    ): ResponseEntity<UserLoginApiResponse> {
        val user = userAuthService.findUser(request.userId, request.password)
        // Session에 사용자 정보 저장
        session.setAttribute("USER", user)
        return ResponseEntity.ok( UserLoginApiResponse("Logged in with Session") )
    }

    // JWT Token 로그인
    @PostMapping("/token")
    fun loginWithToken(
        @RequestBody request: UserLoginApiRequest,
        response: HttpServletResponse
    ): ResponseEntity<UserLoginApiResponse> {
        // Call service to handle login and generate token
        val token = userAuthService.generateUserJwt(request.userId, request.password)

        // Add token to Authorization header
        response.addHeader("Authorization", "Bearer $token")

        // Return response
        return ResponseEntity.ok(
            UserLoginApiResponse(
                message = "Login successful",
                token = token
            )
        )
    }
}