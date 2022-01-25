package com.fdantas.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdantas.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}