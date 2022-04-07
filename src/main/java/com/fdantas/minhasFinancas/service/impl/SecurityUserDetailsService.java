package com.fdantas.minhasFinancas.service.impl;

import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdantas.minhasFinancas.model.entity.Usuario;
import com.fdantas.minhasFinancas.model.repository.UsuarioRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	
	private UsuarioRepository usuarioRepository;
	
	public SecurityUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuarioLogado = usuarioRepository.findByEmail(username);
		if(Objects.isNull(usuarioLogado)) {
			throw new UsernameNotFoundException("Email não cadastrado.");
		}
		
		
		return User.builder()
						.username(usuarioLogado.getEmail())
						.password(usuarioLogado.getSenha())
						.roles("USER")
						.build();
	}

}
