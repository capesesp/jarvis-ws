package br.com.capesesp.email;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class EnviaEmailJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		
		EnviaEmail enviaEmail = new EnviaEmail();
		enviaEmail.enviaEmail(context, "EnviaEmailJob", "EnviaEmailTrigger");
		
		enviaEmail = null;
	}
	
}
