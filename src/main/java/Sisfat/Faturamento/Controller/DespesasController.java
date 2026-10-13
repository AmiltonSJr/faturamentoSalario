
package Sisfat.Faturamento.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
		mv.addObject("despesas", montarListaDespesas());

		return mv;
	}


@PostMapping("/despesas")
public String salvar(Despesas despesas) {

    // Edição de uma despesa existente
    if (despesas.getId() != 0) {

        Despesas existente = despesasRepository.findById(despesas.getId())
                .orElseThrow();

        // Edição de uma compra parcelada
        if (existente.getGrupoParcelamento() != null) {

            var parcelas = despesasRepository.findByGrupoParcelamento(
                    existente.getGrupoParcelamento());

            int quantidadeAntiga = parcelas.size();
            int quantidadeNova = despesas.getQuantidadeParcelas();

            if (quantidadeNova < 1) {
                return "redirect:/despesas";
            }

            // Atualiza os dados de todas as parcelas existentes
            for (Despesas parcela : parcelas) {
                parcela.setDescricao(despesas.getDescricao());
                parcela.setCategoria(despesas.getCategoria());
                parcela.setValor(despesas.getValor());
                parcela.setParcelada(true);
                parcela.setQuantidadeParcelas(quantidadeNova);
            }

            // Ajusta a quantidade de parcelas, se necessário
            if (quantidadeNova < quantidadeAntiga) {

                parcelas.sort(java.util.Comparator.comparing(Despesas::getData));

                for (int i = quantidadeNova; i < quantidadeAntiga; i++) {
                    despesasRepository.delete(parcelas.get(i));
                }

            } else if (quantidadeNova > quantidadeAntiga) {

                parcelas.sort(java.util.Comparator.comparing(Despesas::getData));

                Despesas ultimaParcela = parcelas.get(parcelas.size() - 1);

                for (int i = quantidadeAntiga; i < quantidadeNova; i++) {
                    Despesas novaParcela = new Despesas();

                    novaParcela.setDescricao(despesas.getDescricao());
                    novaParcela.setCategoria(despesas.getCategoria());
                    novaParcela.setValor(despesas.getValor());
                    novaParcela.setData(ultimaParcela.getData()
                            .plusMonths(i - quantidadeAntiga + 1));
                    novaParcela.setParcelada(true);
                    novaParcela.setQuantidadeParcelas(quantidadeNova);
                    novaParcela.setParcelaAtual(i + 1);
                    novaParcela.setGrupoParcelamento(
                            existente.getGrupoParcelamento());

                    despesasRepository.save(novaParcela);
                }
            }

            // Corrige a numeração das parcelas restantes
            var parcelasAtualizadas = despesasRepository.findByGrupoParcelamento(
                    existente.getGrupoParcelamento());

            parcelasAtualizadas.sort(java.util.Comparator.comparing(Despesas::getData));

            for (int i = 0; i < parcelasAtualizadas.size(); i++) {
                parcelasAtualizadas.get(i).setParcelaAtual(i + 1);
                parcelasAtualizadas.get(i).setQuantidadeParcelas(quantidadeNova);
            }

            despesasRepository.saveAll(parcelasAtualizadas);

            return "redirect:/despesas";
        }

        // Edição de uma despesa comum
        existente.setDescricao(despesas.getDescricao());
        existente.setCategoria(despesas.getCategoria());
        existente.setValor(despesas.getValor());
        existente.setData(despesas.getData());

        despesasRepository.save(existente);

        return "redirect:/despesas";
    }

    // Cadastro de uma nova compra parcelada
    if (despesas.isParcelada()) {

        if (despesas.getQuantidadeParcelas() < 1) {
            return "redirect:/despesas";
        }

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
        // Cadastro de uma despesa comum
        despesas.setGrupoParcelamento(null);
        despesas.setParcelada(false);
        despesasRepository.save(despesas);
    }

    return "redirect:/despesas";
}

	@GetMapping("/despesas/editar/{id}")
	public ModelAndView editar(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("administration/despesas");

		Despesas despesa = despesasRepository.findById(id).orElseThrow();

		mv.addObject("despesa", despesa);
		mv.addObject("despesas", montarListaDespesas());

		return mv;
	}

	@GetMapping("/despesas/excluir/{id}")
	public String excluir(@PathVariable Long id) {
		Despesas despesa = despesasRepository.findById(id).orElseThrow();

		if (despesa.getGrupoParcelamento() != null) {
			var parcelas = despesasRepository.findByGrupoParcelamento(despesa.getGrupoParcelamento());

			despesasRepository.deleteAll(parcelas);
		} else {
			despesasRepository.delete(despesa);
		}

		return "redirect:/despesas";
	}

	private ArrayList<DespesaExibicao> montarListaDespesas() {
		var todasDespesas = despesasRepository.findAll();

		Set<String> gruposAdicionados = new HashSet<>();
		var despesasExibidas = new ArrayList<DespesaExibicao>();

		for (Despesas despesa : todasDespesas) {

			if (despesa.getGrupoParcelamento() == null) {

				DespesaExibicao exibicao = new DespesaExibicao();

				exibicao.setId(despesa.getId());
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
						.sorted(Comparator.comparing(Despesas::getData)).toList();

				DespesaExibicao exibicao = new DespesaExibicao();

				exibicao.setId(despesa.getId());
				exibicao.setDescricao(despesa.getDescricao());
				exibicao.setCategoria(despesa.getCategoria());
				exibicao.setValor(despesa.getValor());
				exibicao.setParcelada(true);
				exibicao.setQuantidadeParcelas(despesa.getQuantidadeParcelas());
				exibicao.setDataInicio(parcelas.get(0).getData());
				exibicao.setDataFim(parcelas.get(parcelas.size() - 1).getData());

				exibicao.setPeriodo(formatarMes(parcelas.get(0).getData()) + " até "
						+ formatarMes(parcelas.get(parcelas.size() - 1).getData()));

				despesasExibidas.add(exibicao);
				gruposAdicionados.add(despesa.getGrupoParcelamento());
			}
		}

		return despesasExibidas;
	}

	private String formatarMes(LocalDate data) {
		return data.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, new java.util.Locale("pt", "BR")) + "/"
				+ data.getYear();
	}
}
