package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora cal;

	@Before
	public void setup() {
		cal = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValore() {
		int a = 3;
		int b = 5;

		int resultado = cal.soma(a, b);

		org.junit.Assert.assertEquals(8, resultado);

	}

	@Test
	public void deveSubtrairDoisValore() {
		int a = 5;
		int b = 3;

		int resultado = cal.subtrair(a, b);

		org.junit.Assert.assertEquals(2, resultado);

	}

	@Test
	public void deveDividirDoisValore() throws NaoPodeDividirPorZeroException {
		int a = 6;
		int b = 3;
		int resultado = cal.dividir(a, b);

		org.junit.Assert.assertEquals(2, resultado);

	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 0;

		cal.dividir(a, b);

	}
}
