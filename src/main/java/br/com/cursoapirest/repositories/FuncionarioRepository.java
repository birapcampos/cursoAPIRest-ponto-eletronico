package br.com.cursoapirest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursoapirest.entities.Funcionario;


@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	Funcionario findByCpf(String cpf);
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpfOrEmail(String cpf, String email);
	
	Optional<Funcionario> findById(Long id);
}
