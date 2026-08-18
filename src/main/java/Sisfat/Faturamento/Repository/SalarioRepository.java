package Sisfat.Faturamento.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Sisfat.Faturamento.Model.Salario;

public interface SalarioRepository extends JpaRepository<Salario, Long> {

}