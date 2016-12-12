package br.com.capesesp.ws;


public class Mensagens {
	
	private transient Colecao colecao;
	
	public Mensagem[] mensagem;
	
	public Mensagens(){
		mensagem = new Mensagem[1];
		colecao = new Colecao();
	}
	
	private void _add(Object instancia, Object[] array){
		colecao.add(instancia, array);
	}

	public void add(Mensagem mensagem){
		_add(mensagem, this.mensagem);
	}
	
	public void add(SeveridadeEnum tipoSeveridade, String texto, String detalhes, Integer codigo){
		Mensagem mensagem = new Mensagem(tipoSeveridade, texto, codigo);
		_add(mensagem, this.mensagem);
	}

}
