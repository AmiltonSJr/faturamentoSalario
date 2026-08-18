package Sisfat.Faturamento.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Sisfat.Faturamento.Model.Salario;
import Sisfat.Faturamento.Repository.SalarioRepository;

@Service
public class SalarioService {
	@Autowired
	private SalarioRepository salarioRepository;
	
	public Salario salvar(Salario salario) {
		return salarioRepository.save(salario);
	}
}
