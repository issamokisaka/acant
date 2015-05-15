package br.org.acant.modelo;

import java.util.Date;

public class FluxoCaixa implements Comparable<FluxoCaixa> {
	private Date   data;
	private String conta;
	private String lancamento;
	private String valorRecebido;
	private String valorPago;
	private String saldo;

	public FluxoCaixa(Date data, String conta, String lancamento,
					  String vr, String vp, String saldo) {
		this.data = data;
		this.conta = conta;
		this.lancamento = lancamento;
		this.valorRecebido = vr;
		this.valorPago = vp;
		this.saldo = saldo;
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getLancamento() {
		return lancamento;
	}
	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}

	public String getValorRecebido() {
		return valorRecebido;
	}
	public void setValorRecebido(String valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public String getValorPago() {
		return valorPago;
	}
	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}

	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}


	@Override
	public int compareTo(FluxoCaixa fc) {
		return getData().compareTo(fc.getData());
	}
}
