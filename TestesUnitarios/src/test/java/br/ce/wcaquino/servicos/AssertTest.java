package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void teste() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(1, 1); //verifica 2 numeros
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		
		Usuario u1 = new Usuario("usuario 1");
		Usuario u2 = new Usuario("usuario 1");
		Assert.assertEquals(u1, u2);
		
		Assert.assertSame(u2, u2); //verifica se são iguais com objetos 
		Assert.assertNotSame(u2, u2);
		
		Assert.assertNull(u2); //verifica se não é nullo

	}

}
