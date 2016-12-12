package br.com.capesesp.ws;

import java.util.Arrays;

public class Colecao {
	
	/**
	 * Adiciona elemento ao ultimo indice do array, redimensionando o array caso o ultimo elemento já esteja ocupado.
	 * 
	 * @param instancia
	 * @param array
	 */
	public void add(Object instancia, Object[] array){
		if(array[array.length-1] != null){
			array = Arrays.copyOf(array, array.length + 1);
		}
		array[array.length-1] = instancia;
	}
	
}
