package com.fdantas.minhasFinancas.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdantas.minhasFinancas.exception.ErroAutenticacaoException;
import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	private PasswordEncoder encoder;
	
	
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.encoder = encoder;
	} 

	@Override
	public Usuario autenticar(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if(usuario == null) {
			throw new ErroAutenticacaoException("Usuário não encontrado para o e-mail informado.");
		}
		
		if(!encoder.matches(senha, usuario.getSenha())) {
			throw new ErroAutenticacaoException("Senha Inválida.");
		}
		
		return usuario;
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		usuario.setSenha(encoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		if(usuarioRepository.existsByEmail(email)){
			throw new RegraNegocioException("e-mail já cadastrado.");
		}
		
	}

}