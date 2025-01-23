package springbootkotlin.toy.users.auth.api

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springbootkotlin.toy.users.auth.api.req.UserAuthApiLoginRequest
import springbootkotlin.toy.users.auth.api.res.UserAuthApiLoginResponse
import springbootkotlin.toy.users.auth.service.UsersAuthService

@RestController
@RequestMapping("/users/auth/api")
class UserLoginApiController(
    private val userAuthService: UsersAuthService
) {

    // JWT Token 로그인
    @PostMapping("/login/token")
    fun loginWithToken(
        @RequestBody request: UserAuthApiLoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<UserAuthApiLoginResponse> {
        // Call service to handle login and generate token
        val token = userAuthService.generateUserJwt(request.userId, request.password)

        // Add token to Authorization header
        response.addHeader("Authorization", "Bearer $token")

        // Return response
        return ResponseEntity.ok(
            UserAuthApiLoginResponse(
                message = "Login successful",
                token = token
            )
        )
    }

    // Session 로그인
    @PostMapping("/login/session")
    fun loginWithSession(
        @RequestBody request: UserAuthApiLoginRequest,
        session: HttpSession
    ): ResponseEntity<UserAuthApiLoginResponse> {
        val user = userAuthService.findUser(request.userId, request.password)
        // Session에 사용자 정보 저장
        session.setAttribute("USER", user)
        return ResponseEntity.ok( UserAuthApiLoginResponse("Logged in with Session") )
    }


}