package Sisfat.Faturamento.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Despesas")
public class Despesas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String descricao;

	private String categoria;

	private BigDecimal valor;

	private LocalDate data;

	private boolean parcelada;

	private String grupoParcelamento;

	public String getGrupoParcelamento() {
		return grupoParcelamento;
	}

	public void setGrupoParcelamento(String grupoParcelamento) {
		this.grupoParcelamento = grupoParcelamento;
	}

	public boolean isParcelada() {
		return parcelada;
	}

	public void setParcelada(boolean parcelada) {
		this.parcelada = parcelada;
	}

	public int getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(int quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public int getParcelaAtual() {
		return parcelaAtual;
	}

	public void setParcelaAtual(int parcelaAtual) {
		this.parcelaAtual = parcelaAtual;
	}

	private int quantidadeParcelas;

	private int parcelaAtual;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
}