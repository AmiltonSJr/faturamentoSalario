package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.Salario;

@Controller
public class SalarioController {
	@GetMapping("/salario")
	public ModelAndView salario() {
		ModelAndView mv= new ModelAndView("administration/salario");
		mv.addObject("salario",new Salario());
		return mv;
	}
	
	
}
