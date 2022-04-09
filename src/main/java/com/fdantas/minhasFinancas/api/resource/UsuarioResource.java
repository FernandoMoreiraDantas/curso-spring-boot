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

import com.fdantas.minhasFinancas.api.dto.TokenDTO;
import com.fdantas.minhasFinancas.api.dto.UsuarioDTO;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.JwtService;
import com.fdantas.minhasFinancas.service.LancamentoService;
import com.fdantas.minhasFinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private JwtService jwtService;
	

	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
		String token = jwtService.gerarToken(usuarioAutenticado);
		TokenDTO tokenDTO = new TokenDTO(usuarioAutenticado.getNome(), token,usuarioAutenticado.getId());
		return ResponseEntity.ok(tokenDTO);
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
		return optionalUsuario.isPresent() ? ResponseEntity.ok(optionalUsuario.get()):ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não encontrado");
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity<?> obterSaldo(@PathVariable Long id){
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
		return optionalUsuario.isPresent() ? ResponseEntity.ok(lancamentoService.obterSaldoPorTipoLancamentoEUsuario(id)):ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não encontrado");
	}
}