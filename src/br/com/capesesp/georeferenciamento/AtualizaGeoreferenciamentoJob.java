package br.com.capesesp.georeferenciamento;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Classe Job para atualização do Endereço do Credenciado.
 */
@DisallowConcurrentExecution
public class AtualizaGeoreferenciamentoJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		AtualizaGeoreferenciamento atualizaGeoreferenciamento = new AtualizaGeoreferenciamento();
		atualizaGeoreferenciamento.execute(context, "AtualizaGeoreferenciamentoJob", "atualizaGeoreferenciamentoTrigger");
		atualizaGeoreferenciamento = null;
	}
}
