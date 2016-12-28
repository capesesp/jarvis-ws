package br.com.capesesp.ws;

import javax.xml.bind.annotation.XmlType;


@XmlType
public class Mensagem {
	
	public SeveridadeEnum severidade;
	
	public String mensagem;
	
	public Integer codigo;
	
	public Mensagem(){
		severidade = SeveridadeEnum.INFO;
	}

	/**
	 * Construtor da mensagem de retorno 
	 * @param tipoSeveridade Severidade da mensagem. {INFO | WARN | ERROR | FATAL}
	 * @param mensagem codigo da mensagem
	 */
	public Mensagem(SeveridadeEnum tipoSeveridade, String mensagem, Integer codigo) {
		this.severidade = tipoSeveridade;
		this.mensagem = mensagem;
		this.codigo = codigo;
	}
	
}
