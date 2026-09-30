package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	    var rendas = rendaExtraFixaRepository.findAll();
	    mv.addObject("rendasExtras", rendas);
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
	
	@GetMapping("/renda-extra/excluir/{id}")
	public String excluir(@PathVariable Long id) {
	    rendaExtraFixaRepository.deleteById(id);
	    return "redirect:/renda-extra";
	}
	
	@GetMapping("/renda-extra/editar/{id}")
	public ModelAndView editar(@PathVariable Long id){
		 ModelAndView mv = new ModelAndView("administration/rendaExtra");

		    RendaExtraFixa rendaExtra = rendaExtraFixaRepository.findById(id).orElseThrow();

		    mv.addObject("rendaExtra", rendaExtra);
		    mv.addObject("rendasExtras", rendaExtraFixaRepository.findAll());

		    return mv;
	}
	

}
