package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
		
	@GetMapping("administration")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("administration/home.html");
		return mv;
	}
}
