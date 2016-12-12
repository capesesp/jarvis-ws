package br.com.capesesp.cadastrofuncional.domain;


public class Associado {

	public String sequencial = "";
	public String nome = "";
	public String sexo = "";
	public String cpfDependente = "";
	public String dataNascimento = "";
	public String email = "";
	public String autorizaContato = "";
	public Telefone telefone = new Telefone();
	public Endereco endereco = new Endereco();
	public Endereco enderecoEntrega = new Endereco();

}
