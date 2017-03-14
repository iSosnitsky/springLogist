package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sbat.logist.ru.model.User;

@Controller
public class AuthController {
    @RequestMapping("/")
    public String index() {
        User user = new User();
        user.getClientID();

        return "index";
    }
}
