package Sisfat.Faturamento.Controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.RendaExtraFixa;
import Sisfat.Faturamento.Model.Salario;
import Sisfat.Faturamento.Repository.RendaExtraFixaRepository;
import Sisfat.Faturamento.Repository.SalarioRepository;

@Controller
public class HomeController {

    private final SalarioRepository salarioRepository;
    private final RendaExtraFixaRepository rendaExtraFixaRepository;

    public HomeController(SalarioRepository salarioRepository, RendaExtraFixaRepository rendaExtraFixaRepository) {
        this.salarioRepository = salarioRepository;
        this.rendaExtraFixaRepository = rendaExtraFixaRepository;
    }

    @GetMapping("administration")
    public ModelAndView home() {

        ModelAndView mv = new ModelAndView("administration/home.html");

        Salario salario = salarioRepository.findTopByOrderByDataInicioVigenciaDesc();

        BigDecimal totalRendasExtras = rendaExtraFixaRepository.findAll().stream()
                .map(RendaExtraFixa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        mv.addObject("salario", salario);
        mv.addObject("totalRendasExtras", totalRendasExtras);

        return mv;
    }
}