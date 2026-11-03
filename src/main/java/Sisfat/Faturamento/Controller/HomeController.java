package Sisfat.Faturamento.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import Sisfat.Faturamento.Model.Despesas;
import Sisfat.Faturamento.Repository.DespesasRepository;
import Sisfat.Faturamento.Model.RendaExtraFixa;
import Sisfat.Faturamento.Model.Salario;
import Sisfat.Faturamento.Repository.RendaExtraFixaRepository;
import Sisfat.Faturamento.Repository.SalarioRepository;

@Controller
public class HomeController {
	
	private final DespesasRepository despesasRepository;
    private final SalarioRepository salarioRepository;
    private final RendaExtraFixaRepository rendaExtraFixaRepository;

    public HomeController(SalarioRepository salarioRepository,
            RendaExtraFixaRepository rendaExtraFixaRepository,
            DespesasRepository despesasRepository) {

        this.salarioRepository = salarioRepository;
        this.rendaExtraFixaRepository = rendaExtraFixaRepository;
        this.despesasRepository = despesasRepository;
    }

    @GetMapping("administration")
    public ModelAndView home() {

        ModelAndView mv = new ModelAndView("administration/home.html");

        Salario salario = salarioRepository.findTopByOrderByDataInicioVigenciaDesc();

        BigDecimal totalRendasExtras = rendaExtraFixaRepository.findAll().stream()
                .map(RendaExtraFixa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
     // Soma de todas as despesas cadastradas
        BigDecimal totalGeralDespesas = despesasRepository.findAll().stream()
                .map(Despesas::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Soma somente das despesas do mês atual
        LocalDate hoje = LocalDate.now();

        BigDecimal totalDespesas = despesasRepository.findAll().stream()
                .filter(despesa -> despesa.getData() != null
                        && despesa.getData().getMonth() == hoje.getMonth()
                        && despesa.getData().getYear() == hoje.getYear())
                .map(Despesas::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        mv.addObject("totalGeralDespesas", totalGeralDespesas);
        mv.addObject("totalDespesas", totalDespesas);
        mv.addObject("salario", salario);
        mv.addObject("totalRendasExtras", totalRendasExtras);

        // Valor total restante.
        BigDecimal valorSalario = salario != null
                ? salario.getValor()
                : BigDecimal.ZERO;

        BigDecimal saldoDisponivel = valorSalario
                .add(totalRendasExtras)
                .subtract(totalDespesas);

        mv.addObject("saldoDisponivel", saldoDisponivel);
        
        return mv;
    }
}