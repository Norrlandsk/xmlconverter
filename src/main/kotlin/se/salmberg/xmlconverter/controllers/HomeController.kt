package se.salmberg.xmlconverter.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun getHome(): String {
        return "index"
    }
}