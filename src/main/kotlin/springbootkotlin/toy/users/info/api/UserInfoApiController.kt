package springbootkotlin.toy.users.info.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserInfoApiController {

    @GetMapping("")
    fun getUserInfo(): ResponseEntity<Map<String, String>> {
        // 예제 사용자 정보 반환
        val userInfo = mapOf(
            "userId" to "testuser",
            "email" to "testuser@example.com",
            "roles" to "ROLE_USER"
        )
        return ResponseEntity.ok(userInfo)
    }
}