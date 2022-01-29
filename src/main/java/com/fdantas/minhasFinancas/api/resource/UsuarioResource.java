package com.fdantas.minhasFinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdantas.minhasFinancas.api.dto.UsuarioDTO;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.UsuarioService;
import com.fdantas.minhasFinancas.util.Mensagem;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mensagem mensagem;

	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		return ResponseEntity.ok(usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha()));
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuario = Usuario.builder().nome(usuarioDTO.getNome()).email(usuarioDTO.getEmail()).senha(usuarioDTO.getSenha()).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(usuario));
	}
	
	@GetMapping
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> consultar(@PathVariable Long codigo ){
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(codigo);
		return optionalUsuario.isPresent() ? ResponseEntity.ok(optionalUsuario.get()):ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagem.getMensagem("usuario.nao-encontrado"));
	}

}