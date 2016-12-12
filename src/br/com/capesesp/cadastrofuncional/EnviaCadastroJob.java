package br.com.capesesp.cadastrofuncional;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class EnviaCadastroJob implements Job{

	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		
		EnviaCadastro enviaCadastro = new EnviaCadastro();
		enviaCadastro.executa(context, "EnviaCadastroJob", "erroEnviaCadastroTrigger");
		
		enviaCadastro = null;
	}
}