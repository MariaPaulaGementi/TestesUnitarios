package br.ce.wcaquino.servicos;


import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testeLocacao() throws Exception {
		//iniciando teste do metodo de alugarFilme
		//cenario
		LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("filme" ,2 , 5.0);
		
		//ação
		Locacao locacao = servico.alugarFilme(usuario, filme);
		
		//verificação
		Assert.assertEquals(5.0, locacao.getValor(), 0.01); //0.01 -> margem de erro
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(5.0));
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.0)));
		
		//utilizando o rule
		error.checkThat(locacao.getValor(), CoreMatchers.is(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),  CoreMatchers.is(true));
		
	}
	
	//tratamento de exceção "elegante"
	@Test(expected=Exception.class) //o teste já espera uma exceção
	public void testeLocacao_FilmesSemEstoque() throws Exception {
		LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("filme" ,0, 5.0);
		
		servico.alugarFilme(usuario, filme);
		
	}
	
	//tratamento de exceção "robusta"
	@Test 
	public void testeLocacao_FilmesSemEstoque2() {
		LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("filme" ,0, 5.0);
		
		try {
			servico.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma exceção");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
		
	}
	
	//tratamento de exceção "nova"
	//declara a espera da exceção antes da ação
		@Test
		public void testeLocacao_FilmesSemEstoque3() throws Exception {
			//cenário
			LocacaoService servico = new LocacaoService();
			Usuario usuario = new Usuario("Usuario 1");
			Filme filme = new Filme("filme" ,0, 5.0);
			
			//exceção
			exception.expect(Exception.class);
			exception.expectMessage("Filme sem estoque");
			
			//ação
			servico.alugarFilme(usuario, filme);

		}
}
