package com.fdantas.minhasFinancas.exception;

public class ErroAutenticacaoException extends RuntimeException {
	
	private static final long serialVersionUID = -4746406264114135500L;

	public ErroAutenticacaoException(String mensagem) {
		super(mensagem);
	}

}
