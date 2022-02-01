package com.fdantas.minhasFinancas.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdantas.minhasFinancas.model.entity.Lancamento;
import com.fdantas.minhasFinancas.service.LancamentoService;

@RestController
@RequestMapping("api/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@PostMapping
	public ResponseEntity<?> incluir(@RequestBody Lancamento lancamento){
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoService.salvar(lancamento));
	}
	
	@GetMapping
	public List<Lancamento> listar(@RequestBody Lancamento lancamentoFiltro){
		return lancamentoService.listar(lancamentoFiltro);
	}
	
}
