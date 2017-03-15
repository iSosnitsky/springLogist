package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
