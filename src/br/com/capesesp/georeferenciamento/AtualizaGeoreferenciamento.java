package br.com.capesesp.georeferenciamento;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.quartz.JobExecutionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import br.com.capesesp.georeferenciamento.domain.Credenciados;
import br.com.capesesp.georeferenciamento.domain.Endereco;
import br.com.capesesp.georeferenciamento.domain.GoogleGeoCodeResponse;
import br.com.capesesp.georeferenciamento.domain.GoogleGeoCodeResponse.results;
import br.com.capesesp.georeferenciamento.domain.SaidaListaCredenciados;
import br.com.capesesp.opus.OpusAdapter;
import br.com.capesesp.opus.OpusException;
import br.com.capesesp.ws.GenericSaida;

public class AtualizaGeoreferenciamento {

	private static final String GOOGLE_API_URL = "http://maps.googleapis.com/maps/api/geocode/json";

	private Logger logger = Logger.getLogger(AtualizaGeoreferenciamentoJob.class.getPackage().getName());

	private static final String BRASIL = "Brazil";
	
	private static final String LAT_LONG_ERROR = "999.999999999999";

	public void execute(JobExecutionContext context, String nomeJob, String nomeTrigger){
		
		logger.info("Executando o Job para Atualizar Georeferenciamento");
		String enderecoCredenciado = "";
		String latitude = "0";
		String longitude = "0";

		GoogleGeoCodeResponse googleResponse = new GoogleGeoCodeResponse();
		GenericSaida saidaOpusJson = new GenericSaida();

		try {

			Gson gson = new GsonBuilder().create();

			logger.info("Consulta da lista dos Credenciados que serão atualizados");
			// pesquisa a lista com os endereços que serão atualizados
			SaidaListaCredenciados saida = gson.fromJson(OpusAdapter.fcdj03(), SaidaListaCredenciados.class);

			logger.info("Total de Credenciados: "+ saida.credenciados.size());
			//verifica se foi executado corretamente
			if(saida.getStatusExecucao().executadoCorretamente.booleanValue()){
				int qtdCredSemGeo = 0;
				// loop para recuperar latitude e longitude de cada endereço
				for (Credenciados cred : saida.credenciados) {

					latitude = LAT_LONG_ERROR;
					longitude = LAT_LONG_ERROR;
					enderecoCredenciado = enderecoCompleto(cred.credenciado.endereco);
					try {
						// recupera na API do Google a latitude e longitude
						googleResponse = consultaGoogleAPI(enderecoCredenciado,gson);

						logger.info("cpf/cnpj: " + cred.credenciado.cpf_cnpj + " - Status: " + googleResponse.status);

						if(	googleResponse.status.equals("OVER_QUERY_LIMIT")){
							logger.info("Limite de acessos para o Google atingido");
							break;

						}else{
							//verifica se houve a recuperação da georeferencia 
							if(googleResponse.status.equals("ZERO_RESULTS")){
								//verifica se o CEP tem DDDDD-000
								if(cred.credenciado.endereco.cep.toString().matches("(.*)(000)")){
									//se sim, retira-se o bairro para a consulta da geolocalização
									cred.credenciado.endereco.bairro = "";
									enderecoCredenciado = enderecoCompleto(cred.credenciado.endereco);
									googleResponse = consultaGoogleAPI(enderecoCredenciado,gson);
									logger.info("cpf/cnpj: " + cred.credenciado.cpf_cnpj + " - Status: " + googleResponse.status);
									if(!googleResponse.status.equals("ZERO_RESULTS")){
										results results = googleResponse.results[0];

										latitude = results.geometry.location.lat;
										longitude = results.geometry.location.lng;
									}else{
										qtdCredSemGeo++;
									}
								}else{
									qtdCredSemGeo++;
								}
							}else{
								results results = googleResponse.results[0];

								latitude = results.geometry.location.lat;
								longitude = results.geometry.location.lng;
							}

						}

						// atualiza o endereço com a latitude e longitude recuperados pela API da Google
						saidaOpusJson = gson.fromJson(OpusAdapter.fcdj04(cred.credenciado.cpf_cnpj,cred.credenciado.sequencial, latitude, longitude),GenericSaida.class);

						if (!saidaOpusJson.getStatusExecucao().executadoCorretamente.booleanValue()) {
							logger.error(saidaOpusJson.getStatusExecucao().mensagens.mensagem[0].mensagem);
						} else {
							logger.info(saidaOpusJson.getStatusExecucao().mensagens.mensagem[0].mensagem);
						}

					}catch (MalformedURLException e) {
						logger.error("Erro na formação da URL", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
					} catch (UnsupportedEncodingException e) {
						logger.error("Erro no encode da resposta", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
					} catch (IOException e) {
						logger.error("Erro ao manipular response", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
					}catch (JsonSyntaxException e) {
						logger.error("Erro ao manipular response", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
					}catch (OpusException e) {
						logger.error("Erro ao manipular response", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO); 
					}catch (Exception e) {
						logger.error("Erro Interno", e);
						Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
					}

				}

				logger.info("Quantidade de endereços com problema : " + qtdCredSemGeo);
			}else{
				logger.info("Falha no retorno da lista de Credenciados pelo Sistema Central " + saida.getStatusExecucao().mensagens.mensagem[0].mensagem);
				Thread.sleep(GeoReferenciamentoConstantes.INTERVALO_ERRO);
			}
		}catch (InterruptedException e) {
			logger.error("Erro ao colocar a Thread em Sleep: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Metodo para efetuar a consulta na API do Google 
	 * @param enderecoCredenciado
	 * @param gson
	 * @return GoogleGeoCodeResponse Resposta do Google com a georeferencia do endereço passado por enrtada
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private GoogleGeoCodeResponse consultaGoogleAPI(String enderecoCredenciado, Gson gson) 
			throws MalformedURLException, UnsupportedEncodingException, IOException {
		URL url = null;;
		URLConnection conn = null;
		ByteArrayOutputStream output = null;
		GoogleGeoCodeResponse googleResponse = null;
		
		url = new URL(GOOGLE_API_URL + "?address=" + URLEncoder.encode(enderecoCredenciado, "UTF-8") + "&sensor=false");
		logger.info("URL: " + url.toString());
		conn = url.openConnection();
		output = new ByteArrayOutputStream(1024);
		IOUtils.copy(conn.getInputStream(), output);
		output.close();

		googleResponse = gson.fromJson(output.toString(), GoogleGeoCodeResponse.class);
		return googleResponse;
	}

	/**
	 * Metodo que monta o endereco a partir dos dados de endereco do
	 * credenciado para que seja possível efetuar a busca pela API do Google
	 * 
	 * @param endereco
	 * @return
	 */
	private String enderecoCompleto(Endereco endereco) {
		String enderecoCompleto = "";

		enderecoCompleto = StringUtils.replace((endereco.logradouro + " "
				+ endereco.numero + "," + endereco.bairro + ","
				+ endereco.municipio + "-" + endereco.estado + "," + BRASIL),
				" ", "+");
		return enderecoCompleto;

	}

}