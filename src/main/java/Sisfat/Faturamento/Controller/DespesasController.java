package Sisfat.Faturamento.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import Sisfat.Faturamento.Model.DespesaExibicao;
import Sisfat.Faturamento.Model.Despesas;
import Sisfat.Faturamento.Repository.DespesasRepository;

@Controller
public class DespesasController {
	private final DespesasRepository despesasRepository;

	public DespesasController(DespesasRepository despesasRepository) {
		this.despesasRepository = despesasRepository;
	}

	@GetMapping("/despesas")
	public ModelAndView despesas() {

		ModelAndView mv = new ModelAndView("administration/despesas");

		mv.addObject("despesa", new Despesas());

		var todasDespesas = despesasRepository.findAll();

		Set<String> gruposAdicionados = new HashSet<>();
		var despesasExibidas = new ArrayList<DespesaExibicao>();

		for (Despesas despesa : todasDespesas) {

		    if (despesa.getGrupoParcelamento() == null) {

		        DespesaExibicao exibicao = new DespesaExibicao();

		        exibicao.setDescricao(despesa.getDescricao());
		        exibicao.setCategoria(despesa.getCategoria());
		        exibicao.setValor(despesa.getValor());
		        exibicao.setParcelada(false);
		        exibicao.setQuantidadeParcelas(0);
		        exibicao.setDataInicio(despesa.getData());
		        exibicao.setPeriodo(formatarMes(despesa.getData()));

		        despesasExibidas.add(exibicao);

		    } else if (!gruposAdicionados.contains(despesa.getGrupoParcelamento())) {

		        var parcelas = todasDespesas.stream()
		                .filter(d -> despesa.getGrupoParcelamento().equals(d.getGrupoParcelamento()))
		                .toList();

		        DespesaExibicao exibicao = new DespesaExibicao();

		        exibicao.setDescricao(despesa.getDescricao());
		        exibicao.setCategoria(despesa.getCategoria());
		        exibicao.setValor(despesa.getValor());
		        exibicao.setParcelada(true);
		        exibicao.setQuantidadeParcelas(despesa.getQuantidadeParcelas());
		        exibicao.setDataInicio(parcelas.get(0).getData());
		        exibicao.setDataFim(parcelas.get(parcelas.size() - 1).getData());

		        exibicao.setPeriodo(
		                formatarMes(parcelas.get(0).getData())
		                + " até "
		                + formatarMes(parcelas.get(parcelas.size() - 1).getData())
		        );

		        despesasExibidas.add(exibicao);

		        gruposAdicionados.add(despesa.getGrupoParcelamento());
		    }
		}

		mv.addObject("despesas", despesasExibidas);

		return mv;
	}

	@PostMapping("/despesas")
	public String salvar(Despesas despesas) {

		if (despesas.isParcelada()) {

			String grupoParcelamento = UUID.randomUUID().toString();
			for (int i = 0; i < despesas.getQuantidadeParcelas(); i++) {
				Despesas parcela = new Despesas();

				parcela.setDescricao(despesas.getDescricao());
				parcela.setCategoria(despesas.getCategoria());
				parcela.setValor(despesas.getValor());
				parcela.setData(despesas.getData().plusMonths(i));

				parcela.setParcelada(true);
				parcela.setQuantidadeParcelas(despesas.getQuantidadeParcelas());
				parcela.setParcelaAtual(i + 1);

				parcela.setGrupoParcelamento(grupoParcelamento);
				despesasRepository.save(parcela);
			}
		} else {
			despesas.setGrupoParcelamento(null);
			despesasRepository.save(despesas);
		}
		return "redirect:/despesas";
	}

	private String formatarMes(LocalDate data) {
		return data.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, new java.util.Locale("pt", "BR")) + "/"
				+ data.getYear();
	}

}
