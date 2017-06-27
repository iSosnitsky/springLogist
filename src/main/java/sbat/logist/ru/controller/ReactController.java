package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "react/")
public class ReactController {
    @RequestMapping("/")
    public String index() {
        return "react/index";
    }
}
