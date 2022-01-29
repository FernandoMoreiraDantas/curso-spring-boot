package com.fdantas.minhasFinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdantas.minhasFinancas.exception.ErroAutenticacaoException;
import com.fdantas.minhasFinancas.exception.RegraNegocioException;
import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;
import com.fdantas.minhasFinancas.service.UsuarioService;
import com.fdantas.minhasFinancas.util.Mensagem;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mensagem mensagem;
	
	
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	} 

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(usuario.isEmpty()) {
			throw new ErroAutenticacaoException(mensagem.getMensagem("usuario.nao-encontrado"));
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacaoException(mensagem.getMensagem("usuario.senha-invalida"));
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		if(usuarioRepository.existsByEmail(email)){
			throw new RegraNegocioException(mensagem.getMensagem("usuario.email-ja-cadastrado"));
		}
		
	}

}