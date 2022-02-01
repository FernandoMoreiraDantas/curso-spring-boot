package com.fdantas.minhasFinancas.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Mensagem {
	
	@Autowired
	private MessageSource messageSource;
	
	public String  getMensagem(String mensagen) {
		return messageSource.getMessage(mensagen,null,LocaleContextHolder.getLocale());
	}
	
	public String  getMensagem(String mensagen, String valor) {
		return messageSource.getMessage(mensagen,valor == null ? null: new Object [] {valor}, LocaleContextHolder.getLocale());
	}
}
