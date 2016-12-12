package br.com.capesesp.ws;

/**
 * Tipos de severidade de mensagem de retorno de execucao do servico
 */
public enum SeveridadeEnum {
	
	/** Alerta sobre algo que deva chamar a atencaoo do requisitante, possibilidade de erro */
	WARN("WARN"),
	/**	Mensagem de carater informativo, como por exemplo "incluido com sucesso" */
	INFO("INFO"),
	/** Erro no processamento da requisicaoo, tanto validacao de entrada como validacao de regra de negocio */
	FATAL("FATAL"),
	/** Erro grave, gera no consumo do servico notificacos adicionais ou parada de consumo/sincronizacao */
	ERROR("ERROR");
	
	private String severidade;
	
	private SeveridadeEnum(String tipoSeveridade){
		this.severidade = tipoSeveridade;
	}
	
	/**
	 * @return severidade
	 */
	public String getSeveridade() {
		return severidade;
	}
	
	/**
	 * @param severidade
	 */
	public void setSeveridade(String severidade) {
		this.severidade = severidade;
	}
}
