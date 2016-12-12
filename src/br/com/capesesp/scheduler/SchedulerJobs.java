package br.com.capesesp.scheduler;

import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import br.com.capesesp.cadastrofuncional.CadastroFuncionalConstantes;
import br.com.capesesp.cadastrofuncional.EnviaCadastroJob;
import br.com.capesesp.email.EnviaEmailJob;
import br.com.capesesp.enviodesenhas.EnviaSenhasConstantes;
import br.com.capesesp.enviodesenhas.EnviaSenhasJob;
import br.com.capesesp.georeferenciamento.AtualizaGeoreferenciamentoJob;

@Singleton
@Startup
@DisallowConcurrentExecution
public class SchedulerJobs {

	@PostConstruct
	public void scheduleJobs() throws InterruptedException {

		JobDetail jobSenhas = JobBuilder.newJob(EnviaSenhasJob.class)
				.withIdentity("EnviaSenhaJob", "senha").build();
		
		JobDetail jobEnviaLogFuncional = JobBuilder.newJob(EnviaEmailJob.class)
				.withIdentity("EnviaEmailJob", "email").build();

		JobDetail jobGeoReferenciamento = JobBuilder.newJob(AtualizaGeoreferenciamentoJob.class)
				.withIdentity("AtualizaGeoreferenciamentoJob","GeoReferenciamento").build();
		
		JobDetail jobCadastroFuncional = JobBuilder.newJob(EnviaCadastroJob.class)
				.withIdentity("EnviaCadastroJob", "cadastro").build();

		
		CronExpression cronExpressionGeoReferenciamento = null;
		CronExpression cronExpressionEnviaLogFuncional = null;
		
		CronScheduleBuilder geoReferenciamentoScheduler = null;
		CronScheduleBuilder enviaLogFuncionalScheduler = null;
		
		try{	
			//Cron para execução do job todos os dias as 21 hr
			cronExpressionGeoReferenciamento = new CronExpression("0 0 21 ? * *");
			cronExpressionEnviaLogFuncional = new CronExpression("0 0 16 ? * *");
			
			geoReferenciamentoScheduler = CronScheduleBuilder.cronSchedule(cronExpressionGeoReferenciamento);
			enviaLogFuncionalScheduler = CronScheduleBuilder.cronSchedule(cronExpressionEnviaLogFuncional);

			// Trigger the job to run on the next round minute
			Trigger triggerSenha = TriggerBuilder
					.newTrigger()
					.withIdentity("enviaSenhaTrigger", "senha")
					.withSchedule(
							SimpleScheduleBuilder
							.simpleSchedule()
							.withIntervalInSeconds(
									EnviaSenhasConstantes.INTERVALO_PADRAO)
									.repeatForever()).startNow().build();

			Trigger triggerGeoReferenciamento = TriggerBuilder
					.newTrigger()
					.withIdentity("geoReferenciamnetoTrigger", "GeoReferenciamneto")
					.withSchedule(geoReferenciamentoScheduler).startNow().build();

			Trigger triggerCadastroFuncional = TriggerBuilder
					.newTrigger()
					.withIdentity("cadastroFuncionalTrigger", "cadastro")
					.withSchedule(SimpleScheduleBuilder
							.simpleSchedule()
							.withIntervalInSeconds(
									CadastroFuncionalConstantes.INTERVALO_PADRAO)
									.repeatForever()).startNow().build();
			
			Trigger triggerEnviaEmail = TriggerBuilder
					.newTrigger()
					.withIdentity("enviaEmailTrigger", "email")
					.withSchedule(enviaLogFuncionalScheduler).startNow().build();
			
			Scheduler scheduler;

			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.scheduleJob(jobSenhas, triggerSenha);
			scheduler.scheduleJob(jobGeoReferenciamento,triggerGeoReferenciamento);
			scheduler.scheduleJob(jobEnviaLogFuncional,triggerEnviaEmail);
			scheduler.scheduleJob(jobCadastroFuncional, triggerCadastroFuncional);
			
			scheduler.start();
			
			System.out.println("Criação de Jobs");
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
