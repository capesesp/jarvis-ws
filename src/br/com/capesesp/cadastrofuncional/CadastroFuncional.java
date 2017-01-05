package br.com.capesesp.cadastrofuncional;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.capesesp.cadastrofuncional.domain.DadosCadastrais;
import https.ws_beneficiario_funcionalcard_com.Retorno;
import https.ws_beneficiario_funcionalcard_com.Services;

public class CadastroFuncional {

	public static final Logger logger = Logger.getLogger("br.com.capesesp.funcional");
	
	public Retorno cadastrar(DadosCadastrais dadosCadastrais) {
/*

 */
	  
	  
		Services servicesFuncional = new Services();
		Retorno retorno = new Retorno();	
		
		try {
			
			ajustarDadosCadastrais(dadosCadastrais);
			
			retorno = servicesFuncional.getServicesSoap().movimentacaoUsuario(
					CadastroFuncionalConstantes.LOGIN,                  // login
					CadastroFuncionalConstantes.SENHA,                  // password
					dadosCadastrais.titular.cpf,                        // cpf Titular
					dadosCadastrais.associado.sequencial,               // numdep
					CadastroFuncionalConstantes.COD_CLIENTE,            // codCli
					dadosCadastrais.capesesp.instrucao,                 // Instrução
					dadosCadastrais.titular.matricula,                  // Matricula
					dadosCadastrais.associado.nome,                     // Nome do associado
					dadosCadastrais.associado.sexo,                     // Sexo do associado
					dadosCadastrais.titular.amuc,                       // Amuc
					dadosCadastrais.associado.dataNascimento,           // Data de nascimento do associado
					dadosCadastrais.titular.filial,                     // Filial
					CadastroFuncionalConstantes.SETOR,                  // Setor
					Double.parseDouble(dadosCadastrais.titular.limite), // Limite Amuc
					dadosCadastrais.associado.endereco.tipoLogradouro,  // tipo logradouro
					dadosCadastrais.associado.endereco.logradouro,      // endereco
					dadosCadastrais.associado.endereco.numero,          // endereco_numero
					dadosCadastrais.associado.endereco.complemento,     // complemento
					dadosCadastrais.associado.endereco.bairro,          // bairro
					dadosCadastrais.associado.endereco.cidade,          // cidade
					dadosCadastrais.associado.endereco.uf,              // uf
					dadosCadastrais.associado.endereco.cep,             // cep
					dadosCadastrais.associado.telefone.fixo,            // telefone
					dadosCadastrais.associado.telefone.celular,         // celular
					dadosCadastrais.associado.email,                    // email
					dadosCadastrais.associado.autorizaContato,          // autoriza_contato
					dadosCadastrais.associado.cpfDependente,            // cpf_dependente
					dadosCadastrais.titular.debitoFolha,                // debito_folha
					dadosCadastrais.associado.enderecoEntrega.tipoLogradouro, // tipo_logradouro_entrega
					dadosCadastrais.associado.enderecoEntrega.logradouro,     // endereco_entrega
					dadosCadastrais.associado.enderecoEntrega.numero,         // endereco_numero_entrega
					dadosCadastrais.associado.enderecoEntrega.complemento,    // complemento_entrega
					dadosCadastrais.associado.enderecoEntrega.bairro,         // bairro_entrega
					dadosCadastrais.associado.enderecoEntrega.cidade,         // cidade_entrega
					dadosCadastrais.associado.enderecoEntrega.uf,             // uf_entrega
					dadosCadastrais.associado.enderecoEntrega.cep,            // cep_entrega
					CadastroFuncionalConstantes.COD_CLIENTE                  // codcli_destino
					);
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao enviar dados para a Funcional:" + e.getMessage());
		}

		return retorno;
	}

	/**
	 * Metodo criado para ajustar os dados para atender os requisitos para a utilizacao do WebSerice 
	 * disponibilizado pela Funcional Card para atualizacao cadastral dos beneficiarios.
	 * @param dadosCadastrais
	 * @return
	 */
	private void ajustarDadosCadastrais(DadosCadastrais dadosCadastrais) {
		
		preencheCampoVazio(dadosCadastrais.associado,
				dadosCadastrais.associado.endereco,
				dadosCadastrais.associado.enderecoEntrega,
				dadosCadastrais.associado.telefone,
				dadosCadastrais.titular);
		
		//ajustar cpf
		dadosCadastrais.titular.cpf = formatarCpf(dadosCadastrais.titular.cpf);
		dadosCadastrais.associado.cpfDependente = formatarCpf(dadosCadastrais.associado.cpfDependente);
		
		//ajustar tamanho dos campos
		dadosCadastrais.associado.nome = ajustarTamanhoCampo(dadosCadastrais.associado.nome, 30);
		dadosCadastrais.associado.endereco.tipoLogradouro = ajustarTamanhoCampo(dadosCadastrais.associado.endereco.tipoLogradouro, 14);
		dadosCadastrais.associado.endereco.logradouro = ajustarTamanhoCampo(dadosCadastrais.associado.endereco.logradouro, 59);
		dadosCadastrais.associado.endereco.complemento = ajustarTamanhoCampo(dadosCadastrais.associado.endereco.complemento, 19);
		dadosCadastrais.associado.endereco.bairro = ajustarTamanhoCampo(dadosCadastrais.associado.endereco.bairro, 29);
		dadosCadastrais.associado.endereco.cidade = ajustarTamanhoCampo(dadosCadastrais.associado.endereco.cidade, 29);
		dadosCadastrais.associado.enderecoEntrega.tipoLogradouro = ajustarTamanhoCampo(dadosCadastrais.associado.enderecoEntrega.tipoLogradouro, 14);
		dadosCadastrais.associado.enderecoEntrega.logradouro = ajustarTamanhoCampo(dadosCadastrais.associado.enderecoEntrega.logradouro, 59);
		dadosCadastrais.associado.enderecoEntrega.complemento = ajustarTamanhoCampo(dadosCadastrais.associado.enderecoEntrega.complemento, 19);
		dadosCadastrais.associado.enderecoEntrega.bairro = ajustarTamanhoCampo(dadosCadastrais.associado.enderecoEntrega.bairro, 29);
		dadosCadastrais.associado.enderecoEntrega.cidade = ajustarTamanhoCampo(dadosCadastrais.associado.enderecoEntrega.cidade, 29);
	
		//email
		dadosCadastrais.associado.email = dadosCadastrais.associado.email.trim();
		if(dadosCadastrais.associado.email.isEmpty()) {
		  dadosCadastrais.associado.email = "noreply@funcional.com.br";
		}
		
		//ajustar sequencial
		dadosCadastrais.associado.sequencial = String.format("%2s", dadosCadastrais.associado.sequencial).replace(" ", "0");
	}

	/**
	 * Metodo criado para verificar e preencher com "0" os campos que estão vazios
	 * Este é necessario por conta de uma limitacao da tecnologia .NET por nao aceitar campos nao obrigatorios no WebService 
	 * @param objectArray
	 */
	public void preencheCampoVazio(Object... objectArray) {

		for (Object object : objectArray) {

			Field[] fields = object.getClass().getFields();

			for (Field field : fields) {
				Object value = null;
				
				try {
					value = field.get(object);

					if (value.toString().isEmpty()) {
						field.set(object, "0");
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}

		}

	}
	
	/**
	 * Metodo criado para formatar o campo para o tamnho limite no WebService da Funcional
	 * @param campo
	 * @param tamanhoMaximo
	 * @return Retorna o campo ajustado para o limite permitido no WebService da Funcional
	 */
	public String ajustarTamanhoCampo(String campo, Integer tamanhoMaximo){
		
		/**
		 * Retorna o tamanho minimo entre os dois valores informados, 
		 * isso foi necessário para não fazer a substring com um tamanho 
		 * maior que o tamanho da string. Se o tamanho do campo for maior 
		 * que o tamanhoMaximo, ele irá retornar o tamanhoMaximo.
		 */
		Integer tamanhoPermitido = Math.min(campo.trim().length(), tamanhoMaximo);
				
		String campoAjustado = campo.substring(0, tamanhoPermitido);
		
		return campoAjustado;
	}
	
	/**
	 * Metodo criado para preencher o campo cpf com zeros a esquerda quando o tamanho for menor que 11
	 * @param campoCpf
	 * @return Retorna o campo cpf com 11 digitos
	 */
	public String formatarCpf(String campoCpf){
		
		String cpfFormatado = String.format("%11s", campoCpf).replace(" ", "0");
		
		return cpfFormatado;
	}

}
