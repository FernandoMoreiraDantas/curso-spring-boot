package com.fdantas.minhasFinancas.service;

import java.util.List;

import com.fdantas.minhasFinancas.model.entity.Lancamento;
import com.fdantas.minhasFinancas.model.enuns.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar (Lancamento lancamento);
	List<Lancamento> listar(Lancamento lancamentoFiltro);
	void atualizarStatus(Lancamento a, StatusLancamento status);
	void validar(Lancamento lancamento);
}
