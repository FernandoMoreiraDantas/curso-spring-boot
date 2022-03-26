package com.fdantas.minhasFinancas.api.resource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Lancamento;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.enuns.StatusLancamento;
import com.fdantas.minhasFinancas.model.repository.LancamentoRepository;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.LancamentoService;
import com.fdantas.minhasFinancas.util.Mensagem;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/lancamentos")
@Log4j2
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
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException(mensagem.getMensagem("lancamento.nao-encontrado")));
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
	public ResponseEntity<?> buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario", required = false) Long idUsuario) {

		List<Lancamento> listaLancamentos = null;

		Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);
		if (optionalUsuario.isPresent()) {
			Lancamento lancamentoFiltro = Lancamento.builder().descricao(descricao).mes(mes).ano(ano)
					.usuario(optionalUsuario.get()).build();
			listaLancamentos = lancamentoService.listar(lancamentoFiltro);

		} else {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagem.getMensagem("usuario.nao-encontrado"));
		}

		return ResponseEntity.ok(listaLancamentos);
	}

	@PutMapping("{id}/atualizar-status")
	public ResponseEntity<Lancamento> atualizarStatus(@PathVariable("id") Long id, @RequestBody Lancamento lancamento){
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException((mensagem.getMensagem("lancamento.nao-encontrado"))));
		StatusLancamento statusLancamento = StatusLancamento.valueOf(lancamento.getStatus().toString());
		if(Objects.nonNull(statusLancamento)) {
			lancamentoSalvo.setStatus(statusLancamento);
			lancamentoService.atualizar(lancamentoSalvo);
		}else {
			throw new RegraNegocioException(mensagem.getMensagem("lancamento.status-invalido"));
		} 
		return ResponseEntity.status(HttpStatus.OK).body(lancamentoService.atualizar(lancamentoSalvo));
	}
	
}
