package br.com.capesesp.ws;

/**
 * Status da execucao de um metodo.
 */
public class StatusExecucao {
		
	public Boolean executadoCorretamente;
	public Mensagens mensagens;
	
	/**
	 * Construtor
	 */
	public StatusExecucao() {
		executadoCorretamente = null;
		mensagens = new Mensagens();
	}

}