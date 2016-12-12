package br.com.capesesp.georeferenciamento.domain;

import java.util.ArrayList;
import java.util.List;

import br.com.capesesp.ws.GenericSaida;
import br.com.capesesp.ws.StatusExecucao;


public class SaidaListaCredenciados extends GenericSaida{
	
	public List<Credenciados> credenciados = new ArrayList<Credenciados>();
	
	public SaidaListaCredenciados(){
		statusExecucao = new StatusExecucao();
	}
}
