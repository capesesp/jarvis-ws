package br.com.capesesp.enviodesenhas;

public interface EnviaSenhasConstantes {

	/**
	 * Intervalo, em segundos, de execução da trigger do scheduler.
	 */
	Integer INTERVALO_PADRAO = 300;
	
	/**
	 * Intervalo, em milisegundos, de execução da trigger do schedule quando ocorre algum erro no envio de senhas.
	 */
	Integer INTERVALO_ERRO = 3600000;
	
	/**
	 *  Registro da CAPESESP na ANS 
	 */
	String REGISTRO_ANS = "324477";
	
	/** 
	 * Senha da CAPESESP na TEMPRO
	 */
	String CHAVE_DE_ACESSO = "TmprCAp13";
	
	/**
	 * Dias a serem subtreaidos para que o programa JSON no Opus retorne as senhas dos três dias anteriores a data atual 
	 */
	int DIAS_A_SEREM_SUBTRAIDOS = -3;
}
