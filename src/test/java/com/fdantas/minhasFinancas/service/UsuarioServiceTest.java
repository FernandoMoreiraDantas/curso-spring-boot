package com.fdantas.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdantas.minhasFinancas.exception.ErroAutenticacaoException;
import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.impl.UsuarioServiceImpl;
import com.fdantas.minhasFinancas.util.Mensagem;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	private UsuarioServiceImpl usuarioService;

	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@MockBean
	private Mensagem mensagem;
	
	

	@Test
	public void deveSalvarUmUsuario() {
		// cenário
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha")
				.build();

		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		// ação
		Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

		// verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}

	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		// cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);

		// ação
		Throwable exception = Assertions.catchThrowable(() -> usuarioService.salvarUsuario(usuario));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class);

		// verificação
		Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
	}

	@Test
	public void deveAutenticarUsuarioComSucesso() {
		// cenario
		String email = "email@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		// ação/verificação
		assertDoesNotThrow(() -> usuarioService.autenticar(email, senha));
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrouUsuario() {
		// cenario
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(mensagem.getMensagem(Mockito.anyString())).thenReturn("Usuário não encontrado para o e-mail informado.");
		// ação
		Throwable exception = Assertions.catchThrowable(() -> usuarioService.autenticar("email@email.com", "senha"));

		// verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class)
				.hasMessage("Usuário não encontrado para o e-mail informado.");
	}

	@Test
	public void develancarErroQuandoSenhaErrada() {
		// cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		Mockito.when(mensagem.getMensagem(Mockito.anyString())).thenReturn("Senha Inválida.");
		// ação
		Throwable exception = Assertions.catchThrowable(() -> usuarioService.autenticar("email@email.com", "123"));

		// verifiação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha Inválida.");
	}

	@Test
	public void deveValidarEmail() {
		// cenario
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		// ação
		assertDoesNotThrow(() -> usuarioService.validarEmail("email@email.com"));
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// cenario
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// ação/verificação
		assertThrows(RegraNegocioException.class, () -> usuarioService.validarEmail("email@email.com"));

	}

}
