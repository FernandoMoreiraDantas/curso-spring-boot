package com.fdantas.minhasFinancas.service;

import com.fdantas.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {
	public Usuario autenticar(String email, String senha);
	
	public Usuario salvarUsuario(Usuario usuario);
	
	public void validarEmail(String email);

}
