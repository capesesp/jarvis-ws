package br.com.capesesp.enviodesenhas;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobExecutionContext;

import com.google.gson.Gson;

import br.com.capesesp.opus.OpusAdapter;

public class EnviaSenhas {

	public static final Logger logger = Logger.getLogger("br.com.capesesp.enviodesenhas");
	
	public EnviaSenhas(){
	}
	/**
	 * Metodo que envia as senhas para o serviço da Tempro. 
	 * Ele também grava o protocolo enviado pela Tempro no Sistema Central.
	 * Qualquer problema no envio das Senhas será aberto um caso e o tempo para reinicio do envio é de 1 hora
	 * @param context
	 * @param nomeJob
	 * @param nomeTrigger
	 */
	public void executa(JobExecutionContext context, String nomeJob, String nomeTrigger){

		EnviaSenhasTempro enviarSenhasTempro = new EnviaSenhasTempro();
		Gson gson = new Gson();
		String senha;
		RetornoSistemaCental retornoSistemaCental = new RetornoSistemaCental();
		Long protocolo;
		String retornoJsonSistemaCentral = "";
		try{

			Date data = DateUtils.parseDate(System.getProperty("data.corte.senhas"), new String[]{"dd/MM/yyyy"});
			int numSenha = 0;
			retornoJsonSistemaCentral = OpusAdapter.fsnj04(data);
			Senhas listaSenhas = gson.fromJson(retornoJsonSistemaCentral, Senhas.class);
			
			if(listaSenhas.statusExecucao.executadoCorretamente == true){
				
				logger.info("Encontradas " + listaSenhas.senhas.length + " senhas");
				
				for (int i = 0; i < listaSenhas.senhas.length; i++) {
					numSenha++;
					senha = listaSenhas.senhas[i].numero;
					logger.info("Enviando...");
					protocolo = enviarSenhasTempro.insereSenhaTempro(senha);
					logger.info("senha "+ numSenha + "/"+ listaSenhas.senhas.length + ": " + senha);
					if (protocolo != null) {
						try {
							//atualizando o Sistema Central com o envio das senhas
							retornoSistemaCental = gson.fromJson(OpusAdapter.fsnj05(senha, protocolo),RetornoSistemaCental.class);
							logger.info("Protocolo "+ protocolo +" da senha " + senha +" gravado no sistema central em " + new Date());
						} catch (RuntimeException e) {
							logger.log(Level.SEVERE, "Erro no sistema central: " + e.getMessage(), e);
							Thread.sleep(EnviaSenhasConstantes.INTERVALO_ERRO);
							break;
						}
						/**
						 * Verifica se o protocolo da senha foi salvo corretamente no sistema central
						 */
						if (!retornoSistemaCental.statusExecucao.executadoCorretamente.booleanValue()) {
							logger.log(Level.SEVERE, "Erro ao salvar protocolo no sistema central: " + retornoSistemaCental.statusExecucao.mensagens.mensagem[0].mensagem);
							Thread.sleep(EnviaSenhasConstantes.INTERVALO_ERRO);
							break;
						}
					} else {
						/**
						 * A abertura do caso está sendo realizada no método "insereSenhaTempro"
						 */
						logger.log(Level.SEVERE, "Erro na tempro, protocolo inválido " + new Date());
						Thread.sleep(EnviaSenhasConstantes.INTERVALO_ERRO);
						break;
					}
				}
			}else{
				logger.log(Level.SEVERE, "Erro ao executar o programa fsnj04 " + new Date());
				Thread.sleep(EnviaSenhasConstantes.INTERVALO_ERRO);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao recuperar senhas no sistema central: " + e.getMessage() + "\n " + retornoJsonSistemaCentral, e);
			try {
				Thread.sleep(EnviaSenhasConstantes.INTERVALO_ERRO);
			} catch (InterruptedException e1) {
				logger.log(Level.SEVERE, "Erro ao colocar a Thread em Sleep: " + e1.getMessage(), e1);
			}
		}
	}


}
