package com.fdantas.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdantas.minhasFinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
	
	boolean existsByEmail(String email);
	
	Usuario findByEmail(String email);
}