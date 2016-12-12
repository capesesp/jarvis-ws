package br.com.capesesp.enviodesenhas;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EnviaSenhasReagendadaJob implements Job{
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		EnviaSenhas envia = new EnviaSenhas();
		envia.executa(context, "EnviaSenhasJob", "sucessoEnviaSenhaTrigger");
		envia = null;
	}
}
