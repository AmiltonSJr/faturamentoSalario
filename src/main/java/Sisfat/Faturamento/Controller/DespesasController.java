package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.Despesas;
import Sisfat.Faturamento.Repository.DespesasRepository;

@Controller
public class DespesasController {
	private final DespesasRepository despesasRepository;
	
	public DespesasController(DespesasRepository despesasRepository) {
		this.despesasRepository = despesasRepository;
	}
	
	@GetMapping("/despesas")
	public ModelAndView despesas(){
		ModelAndView mv = new ModelAndView("administration/despesas");
		
		mv.addObject("despesa", new Despesas());
		mv.addObject("despesas", despesasRepository.findAll());
		
		return mv;
	}
	
	@PostMapping("/despesas")
	public String salvar(Despesas despesas) {
		despesasRepository.save(despesas);
		return "redirect:/despesas";
	}
	
	
	
}
