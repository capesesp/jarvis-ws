package br.com.capesesp.cadastrofuncional.domain;

import java.util.ArrayList;
import java.util.List;

import br.com.capesesp.ws.StatusExecucao;


/**
 * Lista de associados que precisam ser enviados para a Funcional. Essa lista possui apenas matricula e senha.
 * @author uswesl
 *
 */
public class AssociadoAtualizadoLista {

	public List<AssociadoAtualizado> associados = new ArrayList<AssociadoAtualizado>();
	public StatusExecucao statusExecucao = new StatusExecucao();
}
