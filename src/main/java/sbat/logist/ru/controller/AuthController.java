package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {
	@RequestMapping(value = "/main", params = {"invoiceNumber", "clientId"})
	public String getBarBySimplePathWithRequestParam(){
		return "/requestHistoryPage";
	}

    @RequestMapping("/")
	public String root() {
		return "redirect:/index";
	}

	@RequestMapping("/routeLists")
	public String routeLists(){return "/routeLists";}

	@RequestMapping("/routeList")
	public String routeList(){return "/routeList";}

	@RequestMapping("/routeListHistory")
	public String routeListHistory(){return "/routeListHistory";}

	@RequestMapping("/requestHistory")
	public String requestHistory(){return "/requestHistory";}

	@RequestMapping("/react")
	public String react() {
    	return "redirect:/react/";
    }

	@RequestMapping("/index")
	public String index() {
		return "redirect:/login";
	}

	@RequestMapping("/adminPage")
	public String adminPage() {
		return "/adminPage";
	}

	@RequestMapping("/main")
	public String main(){return "/main";}

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

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
