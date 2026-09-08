package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        Salario salario = salarioRepository.findTopByOrderByDataInicioVigenciaDesc();

        if (salario == null) {
            salario = new Salario();
        }

        mv.addObject("salario", salario);

        return mv;
    }
    
    @PostMapping("/salario")
    public String Salvar(Salario salario) {
    	salarioRepository.save(salario);
    	return "redirect:/salario";
    }
    
    @GetMapping("/salario/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("administration/salario");
        Salario salario = salarioRepository.findById(id).orElseThrow();
        mv.addObject("salario", salario);
        return mv;
    }
    
    @GetMapping("/salario/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        salarioRepository.deleteById(id);
        return "redirect:/salario";
    }
}