package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import Sisfat.Faturamento.Model.RendaExtraFixa;
import Sisfat.Faturamento.Repository.RendaExtraFixaRepository;

@Controller
public class RendaExtraFixaController {

	@GetMapping("/renda-extra")
	public ModelAndView rendaExtra() {
		ModelAndView mv = new ModelAndView("administration/rendaExtra");
		mv.addObject("rendaExtra", new RendaExtraFixa());
		return mv;
	}
	@PostMapping("/renda-extra")
	public String salvar(RendaExtraFixa rendaExtra) {
		rendaExtraFixaRepository.save(rendaExtra);
		return "redirect:/renda-extra";
	}
	
	private final RendaExtraFixaRepository rendaExtraFixaRepository;
	public RendaExtraFixaController(RendaExtraFixaRepository rendaExtraFixaRepository) {
		this.rendaExtraFixaRepository = rendaExtraFixaRepository;
	}
}
