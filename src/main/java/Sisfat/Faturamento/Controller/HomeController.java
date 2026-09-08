package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.Salario;
import Sisfat.Faturamento.Repository.SalarioRepository;

@Controller
public class HomeController {

    private final SalarioRepository salarioRepository;

    public HomeController(SalarioRepository salarioRepository) {
        this.salarioRepository = salarioRepository;
    }

    @GetMapping("administration")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("administration/home.html");

        Salario salario = salarioRepository.findTopByOrderByDataInicioVigenciaDesc();

        mv.addObject("salario", salario);

        return mv;
    }
}