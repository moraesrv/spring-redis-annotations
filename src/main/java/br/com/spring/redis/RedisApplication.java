package br.com.spring.redis;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import br.com.spring.redis.collection.Produto;
import br.com.spring.redis.service.ProdutoService;

@EnableCaching
@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(ProdutoService produtoService) {
		return args -> {

			// Cria um produto e adiciona ao repositório, essa ação não reflete no cache
			Produto produto = new Produto("6", "Moussssse", 90);
			produtoService.adicionarProduto(produto);

			// Irá adicionar os produto no cache (@Cacheable)
			produtoService.buscarProduto("1");
			produtoService.buscarProduto("1");
			produtoService.buscarProduto("2");
			produtoService.buscarProduto("1");
			produtoService.buscarProduto("3");
			produtoService.buscarProduto("1");
			produtoService.buscarProduto("1");
			produtoService.buscarProduto("6");

			// Irá atualizar os dados do produto com id = 6 no cache (@CachePut)
			produto.setNome("Mouse");
			produtoService.atualizarProduto("6", produto);

			// Irá remover do cache o produto com id = 3 (@CacheEvict)
			produtoService.removerProduto("3");
		};
	}

}