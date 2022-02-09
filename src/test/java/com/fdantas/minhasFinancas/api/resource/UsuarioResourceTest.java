package com.fdantas.minhasFinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdantas.minhasFinancas.api.dto.UsuarioDTO;
import com.fdantas.minhasFinancas.exception.ErroAutenticacaoException;
import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.LancamentoService;
import com.fdantas.minhasFinancas.service.UsuarioService;
import com.fdantas.minhasFinancas.util.Mensagem;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {
	static final String API = "/api/usuarios";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService usuarioService;
	
	@MockBean
	UsuarioRepository usuarioRepository;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@MockBean
	Mensagem mensagem;
	
	@Test
	public void deveAutenticarUsuario() throws Exception {
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		// execução e verificação
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(API.concat("/autenticar"))
				                                                             .accept(MediaType.APPLICATION_JSON)
				                                                             .contentType(MediaType.APPLICATION_JSON)
				                                                             .content(json);
		
		mvc.perform(requestBuilder)
		   .andExpect( MockMvcResultMatchers.status().isOk())
		   .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		   .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		   .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())); 
		
	}
	
	@Test
	public void deveRetornarBadRequestAofalharAutenticacao() throws Exception {
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacaoException.class);
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		// execução e verificação
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(API.concat("/autenticar"))
				                                                             .accept(MediaType.APPLICATION_JSON)
				                                                             .contentType(MediaType.APPLICATION_JSON)
				                                                             .content(json);
		
		mvc.perform(requestBuilder)
		   .andExpect( MockMvcResultMatchers.status().isBadRequest()); 
	}
	
	@Test
	public void deveSalvarUsuario() throws Exception {
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		// execução e verificação
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(API)
				                                                             .accept(MediaType.APPLICATION_JSON)
				                                                             .contentType(MediaType.APPLICATION_JSON)
				                                                             .content(json);
		
		mvc.perform(requestBuilder)
		   .andExpect( MockMvcResultMatchers.status().isCreated())
		   .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		   .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		   .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())); 
		
	}
	
	@Test
	public void deveRetornarBadRequestSalvarUsuario() throws Exception {
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		// execução e verificação
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(API)
				                                                             .accept(MediaType.APPLICATION_JSON)
				                                                             .contentType(MediaType.APPLICATION_JSON)
				                                                             .content(json);
		
		mvc.perform(requestBuilder)
		   .andExpect( MockMvcResultMatchers.status().isBadRequest());
		
	}
	
	
}
