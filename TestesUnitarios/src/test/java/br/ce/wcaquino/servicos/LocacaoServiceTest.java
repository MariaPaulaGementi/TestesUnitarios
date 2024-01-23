package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService servico;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// executa antes de cada teste
	@Before
	public void setup() {
		servico = new LocacaoService(); // como uso essa instancia em todos os teste, chamo ela antes de cada teste
		System.out.println("Before");
	}

	// executa depois de cada teste
	@After
	public void tearDown() {
		System.out.println("After");
	}

	@BeforeClass
	public static void setupClass() {
		System.out.println("Before Class");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}

	// @FixMethodOrder(MethodSorters.NAME_ASCENDING) -> ordena os testes por ordem
	// alfabetica

	@Test
	public void testeLocacao() throws Exception {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// iniciando teste do metodo de alugarFilme
		// cenario
		// LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("filme", 2, 5.0));

		// ação
		Locacao locacao = servico.alugarFilme(usuario, filmes);

		// verificação
		Assert.assertEquals(5.0, locacao.getValor(), 0.01); // 0.01 -> margem de erro
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

		Assert.assertThat(locacao.getValor(), CoreMatchers.is(5.0));
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.0)));

		// utilizando o rule
		error.checkThat(locacao.getValor(), CoreMatchers.is(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));

	}

	// tratamento de exceção "elegante" -> usar quando tem a garantia do pq a
	// exceção foi lançada
	@Test(expected = Exception.class) // o teste já espera uma exceção
	public void testeLocacao_FilmesSemEstoque() throws Exception {
		// LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("filme", 0, 5.0));

		servico.alugarFilme(usuario, filmes);

	}

	// tratamento de exceção "robusta" -> tem a necessidade do retorno da mensagem
	@Test
	public void testeLocacao_FilmesSemEstoque2() {
		// LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("filme", 0, 5.0));

		try {
			servico.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma exceção");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}

	}

	// tratamento de exceção "nova"
	// declara a espera da exceção antes da ação
	@Test
	public void testeLocacao_FilmesSemEstoque3() throws Exception {
		// cenário
		// LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("filme", 0, 5.0));

		// exceção
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// ação
		servico.alugarFilme(usuario, filmes);

	}

	// tratamento de exceção "robusta"
	@Test
	public void testeLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
		// LocacaoService servico = new LocacaoService();
		List<Filme> filmes = Arrays.asList(new Filme("filme", 2, 5.0));

		try {
			servico.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario Vazio"));
		}
	}

	// tratamento de exceção "nova"
	@Test
	public void testeLocacao_FilmesVazio() throws FilmeSemEstoqueException, LocadoraException {
		// cenário
		// LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");

		// exceção
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// ação
		servico.alugarFilme(usuario, null);

	}

	@Test
	// @Ignore
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)); // ele só irá funcionar no
																						// sabado
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 3", 2, 4.0));

		Locacao retorno = servico.alugarFilme(usuario, filmes);

		Boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataLocacao(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);

	}
}
