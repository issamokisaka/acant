package br.org.acant.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilitario {
	private static final String FORMATO_PADRAO = "dd/MM/yyyy"; 
	private static final String FORMATO_INVERTIDO = "yyyyMMdd";

	public static void main(String[] args) {
		double x = 0.00d;
		x = 436456456.35;
		System.out.println(formatarParaDecimal(x));

		System.out.println("hash = ");
	}

	/**
	 * @param d
	 * @return
	 */
	public static String formatarParaDecimal(double d) {
		if (d == 0) {
			return "0,00";
		}
		return new DecimalFormat("###,###,###,###.00").format(d);
	}

	/**
	 * @return
	 */
	public static String obterDataAtual() {
		return new SimpleDateFormat(FORMATO_PADRAO).format(new Date());
	}

	/**
	 * @param stringData
	 * @return
	 */
	public static int obterDataFormatoInvertido(String stringData) {
		DateFormat formato = new SimpleDateFormat(FORMATO_PADRAO);
		Date data = null;
		try {
			data = (Date)formato.parse(stringData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(new SimpleDateFormat(FORMATO_INVERTIDO).format(data));
	}
	
	/**
	 * @param intData
	 * @return
	 */
	public static String obterDataFormatoPadrao(int intData) {
		DateFormat formato = new SimpleDateFormat(FORMATO_INVERTIDO);
		Date data = null;
		try {
			data = (Date)formato.parse(String.valueOf(intData));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat(FORMATO_PADRAO).format(data);
	}
	
	/**
	 * @param d
	 * @return
	 */
	public static Date obterObjetoDataPadrao(String d) {
		DateFormat formato = new SimpleDateFormat(FORMATO_PADRAO);
		Date data = null;
		try {
			data = (Date)formato.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}
}
