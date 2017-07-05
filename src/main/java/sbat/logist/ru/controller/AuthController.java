package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {
    @RequestMapping("/")
	public String root() {
		return "redirect:/index";
	}

	@RequestMapping("/react")
	public String react() {
    	return "redirect:/react/";
    }

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/user/index")
	public String userIndex() {
		return "user/index";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String postLogin() {
		// TODO Enable form login with Spring Security (trigger error for now)
		return "redirect:/login-error";
	}

    @RequestMapping(value = "/login-jquery")
    public String loginJquery() {
        return "login-jquery";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
