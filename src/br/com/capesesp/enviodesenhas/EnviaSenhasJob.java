package br.com.capesesp.enviodesenhas;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class EnviaSenhasJob implements Job {

	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		EnviaSenhas envia = new EnviaSenhas();
		envia.executa(context, "EnviaSenhasReagendadasJob", "erroEnviaSenhaTrigger");
		envia = null;
	}
}
