package br.com.capesesp.ws;

public class GenericSaida {

	protected StatusExecucao statusExecucao;

	public GenericSaida() {
		statusExecucao = new StatusExecucao();
	}

	public void setStatusExecucao(StatusExecucao statusExecucao) {
		this.statusExecucao = statusExecucao;
	}

	public StatusExecucao getStatusExecucao() {
		return statusExecucao;
	}
}
