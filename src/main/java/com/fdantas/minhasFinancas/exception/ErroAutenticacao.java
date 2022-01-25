package com.fdantas.minhasFinancas.exception;

public class ErroAutenticacao extends RuntimeException {
	
	private static final long serialVersionUID = -4746406264114135500L;

	public ErroAutenticacao(String mensagem) {
		super(mensagem);
	}

}
