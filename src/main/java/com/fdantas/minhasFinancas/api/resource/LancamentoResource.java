package com.fdantas.minhasFinancas.api.resource;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Lancamento;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.LancamentoRepository;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.LancamentoService;
import com.fdantas.minhasFinancas.util.Mensagem;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mensagem mensagem;
	
	
	
	@PostMapping
	public ResponseEntity<?> incluir(@RequestBody Lancamento lancamento){
		Usuario usuario = usuarioRepository.findById(lancamento.getUsuario().getId()).orElseThrow(() -> new RegraNegocioException(mensagem.getMensagem("usuario.nao-encontrado")));
		lancamento.setUsuario(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoService.salvar(lancamento));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody Lancamento lancamento){
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("lancamento.nao-encontrado"));
		usuarioRepository.findById(lancamento.getUsuario().getId()).orElseThrow(() -> new RegraNegocioException(mensagem.getMensagem("usuario.nao-encontrado")));
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");
		return ResponseEntity.status(HttpStatus.OK).body(lancamentoService.salvar(lancamentoSalvo));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException(mensagem.getMensagem("lancamento.nao-encontrado")));
		lancamentoRepository.delete(lancamentoSalvo); 
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping
	public List<Lancamento> listar(@RequestBody Lancamento lancamentoFiltro){
		return lancamentoService.listar(lancamentoFiltro);
	}
	
}
