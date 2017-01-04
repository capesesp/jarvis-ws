package br.com.capesesp.cadastrofuncional;

public interface CadastroFuncionalConstantes {

	/**
	 * Intervalo, em segundos, de execução da trigger do scheduler.
	 */
	Integer INTERVALO_PADRAO = 300;
	
	/**
	 * Intervalo, em milisegundos, de execução da trigger do schedule quando ocorre algum erro no envio dos dados cadastrais.
	 */
	Integer INTERVALO_ERRO = 3600000;
	
	/** 
	 * Usuario na Funcional
	 */
	String LOGIN = System.getProperty("funcional.usuario");
	;
	
	/** 
	 * Usuario na Funcional
	 */
	String SENHA = System.getProperty("funcional.senha");	
	
	/**
	 * Código da unidade da Capesesp cadastrada na Funcional
	 */
	Integer COD_CLIENTE = 1603;
	
	/**
	 * Código da Capesesp na Funcional
	 */
	Integer COD_GRC = 115;
	
	/**
	 * Setor onde o funcionário está alocado.
	 */
	String SETOR = "CAP";
}
