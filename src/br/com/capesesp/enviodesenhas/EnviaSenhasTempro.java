package br.com.capesesp.enviodesenhas;

import java.util.GregorianCalendar;

import org.jboss.logging.Logger;
import org.tempuri.Service;

public class EnviaSenhasTempro{

	public static final Logger logger = Logger.getLogger("br.com.capesesp.enviodesenhas");

	/**
	 * Método responsável por enviar a senha para TEMPRO através de webservice.
	 * @param senha - Senha a ser inserida na base de dados da TEMPRO
	 * @return
	 */
	public Long insereSenhaTempro(String senha) {
		Long contraSenha;
		Long protocolo;
		GregorianCalendar data = (GregorianCalendar) GregorianCalendar.getInstance();

		contraSenha = this.calculaContraSenha(Long.valueOf(senha),Long.valueOf(data.get(GregorianCalendar.DATE)));

		Service service = new Service();

		try {
			protocolo = service.getServiceSoap().incluirSenha(EnviaSenhasConstantes.REGISTRO_ANS, Long.valueOf(senha), contraSenha,EnviaSenhasConstantes.CHAVE_DE_ACESSO).getProtocolo();
			return protocolo;
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Cria um código verificador utilizado pela TEMPRO para validar o envio.
	 * @param Senha que será enviada para a TEMPRO
	 * @param Dia atual
	 * @return Contra senha
	 */
	private Long calculaContraSenha(Long senha, Long dia) {

		String senhaString = Long.toString(senha);
		Long contraSenhaResult;
		Long codigo = Long.valueOf(retornaNove(senhaString));

		contraSenhaResult = codigo - senha;
		contraSenhaResult = contraSenhaResult + dia;

		return contraSenhaResult;
	}

	/**
	 * Substitui qualquer caracter numérico de uma string por 9
	 * @param Senha que será enviada para a TEMPRO
	 * @return todos os caracteres numéricos substituídos por 9
	 */
	private String retornaNove(String senha) {
		return senha.replaceAll("[0-9]", "9");
	}





}
