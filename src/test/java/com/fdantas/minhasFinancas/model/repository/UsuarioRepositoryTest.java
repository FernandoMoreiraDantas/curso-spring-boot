package com.fdantas.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdantas.minhasFinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired UsuarioRepository usuarioRepository;
	
	@Test
	public void deveVerificarExistenciaEmail() {
		// Cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		// ação, execução
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");
		// verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoExisteUsuarioParaEmailInformado() {
			// Cenario
			// ação, execução
			boolean result = usuarioRepository.existsByEmail("usuario@email.com");
			// verificação
			Assertions.assertThat(result).isFalse();
	}
	
	
	@Test
	public void devePersistirUsuarioNaBase() {
		// cenario
		Usuario usuario = criarUsuario();
		// ação
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		// verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		// cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		//ação
		Usuario result = usuarioRepository.findByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result != null).isTrue();
	}
	
	
	@Test
	public void deveRetornarVazioQuandoUsuarioNaoExistrePorEmail() {
		//ação
		Usuario result = usuarioRepository.findByEmail("usuario@email.com");
		// verificação
		Assertions.assertThat(result != null).isFalse();
	}
	
	

	private Usuario criarUsuario() {
		return  Usuario.
				builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
	
}