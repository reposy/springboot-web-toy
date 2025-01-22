package springbootkotlin.toy.home.controller.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeViewController {
    @GetMapping("")
    fun home(model: Model): String {
        model.addAttribute("title", "Welcome to Home Page")
        return "home" // home.html 템플릿 렌더링
    }
}