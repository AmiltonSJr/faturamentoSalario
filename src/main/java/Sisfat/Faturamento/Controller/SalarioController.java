package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.Salario;
import Sisfat.Faturamento.Repository.SalarioRepository;

@Controller
public class SalarioController {

    private final SalarioRepository salarioRepository;

    public SalarioController(SalarioRepository salarioRepository) {
        this.salarioRepository = salarioRepository;
    }

    @GetMapping("/salario")
    public ModelAndView salario() {
        ModelAndView mv = new ModelAndView("administration/salario");
        mv.addObject("salario", new Salario());
        return mv;
    }
    
    @PostMapping("/salario")
    public String Salvar(Salario salario) {
    	salarioRepository.save(salario);
    	return "redirect:/salario";
    }
    
}