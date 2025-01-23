package springbootkotlin.toy.users.auth.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/users/auth")
class UsersAuthViewController {

    @GetMapping("/login_form")
    fun loginPage(): String {
        return "login_form"
    }
}