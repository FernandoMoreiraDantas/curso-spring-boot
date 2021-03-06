package com.fdantas.minhasFinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Lancamento;
import com.fdantas.minhasFinancas.model.enuns.StatusLancamento;
import com.fdantas.minhasFinancas.model.enuns.TipoLancamento;
import com.fdantas.minhasFinancas.model.repository.LancamentoRepository;
import com.fdantas.minhasFinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		this.validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		lancamentoRepository.delete(lancamento);
	}

	@Override
	public List<Lancamento> listar(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return lancamentoRepository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		this.atualizar(lancamento);
	}
	
	@Override
	public BigDecimal obterSaldoPorTipoLancamentoEUsuario(Long id) {
		BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		if(receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		if(despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}
	

	@Override
	public void validar(Lancamento lancamento) {
		if(StringUtils.isNullOrEmpty(lancamento.getDescricao())){
			throw new RegraNegocioException("Informe a Descri????o");
		}
		
		if(Objects.isNull(lancamento.getMes()) || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um M??s v??lido");
		}
		
		if(Objects.isNull(lancamento.getAno()) || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um ano v??lido.");
		}
		
		if(Objects.isNull(lancamento.getUsuario()) || Objects.isNull(lancamento.getUsuario().getId())){
			throw new RegraNegocioException("Informe o usuario");
			
		}
		
		if(Objects.isNull(lancamento.getValor()) || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor.");
		}
		
		if(Objects.isNull(lancamento.getTipo())) {
			throw new RegraNegocioException("Informe o Tipo.");
		}
		
	}
	
}
