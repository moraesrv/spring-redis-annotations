package br.com.spring.redis.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import br.com.spring.redis.entity.Produto;

@Repository
public class ProdutoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRepository.class);

    // Representa os dados já cadastrados no repositório
    private List<Produto> produtos = new ArrayList<>();

    public ProdutoRepository() {
        this.produtos.add(new Produto("1", "TV", 1999.99));
        this.produtos.add(new Produto("2", "Soundbar", 1299.99));
        this.produtos.add(new Produto("3", "Iphone", 8999.99));
        this.produtos.add(new Produto("4", "Notebook", 5999.99));
        this.produtos.add(new Produto("5", "Headset", 399.99));
    }

    /**
     * Método utilizado para simular a latência de uma consulta ao repositório
     */
    private void latencia() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por localizar um produto dentro do repositório
    */
    private Produto getProduto(String id) {
        return this.produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findAny()
            .get();
    }

    /**
     * Método utilizado para adicionar um produto no repositório
     */
    public Produto create(Produto produto) {
        LOGGER.info(String.format("Inserindo o produto de id [%s] no banco de dados", produto.getId()));
        this.produtos.add(produto);
        LOGGER.info("Produto cadastrado com sucesso");
        return produto;
    }

    /**
     * Método responsável por pesquisar o produto no cache, caso exista retorna o produto, senão
     * pesquisa o produto no repositório e adiciona ao cache
     * @param id identificação do produto
     * @return informações do produto pesquisado
     */
    @Cacheable("produtos")
    public Produto findById(String id) {
        LOGGER.info(String.format("Pesquisando o produto de id [%s] no banco de dados", id));
        latencia();
        return getProduto(id);
    }

    /**
     * Método responsável por pesquisar o produto no repositório e atualizá-lo no repositório e cache
     * @param id identificação do produto
     * @param produto dados do produto a serem atualizados
     * @return informações atualizadas do produto
     */
    @CachePut(value = "produtos", key = "#id")
    public Produto update(String id, Produto produto) {
        LOGGER.info(String.format("Atualizando o produto de id [%s] no banco de dados", id));
        Produto produtoEncontrado = this.produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findAny()
            .get();
        if (produtoEncontrado != null) {
            produtoEncontrado = produto;
            LOGGER.info("Produto encontrado com sucesso");
            return produto;
        }
        LOGGER.info("Produto não localizado");
        return null;
    }

    /**
     * Método responsável por remover o registro do repositório e do cache
     * @param id identificação do produto
     * @return informações do produto removido
     */
    @CacheEvict("produtos")
    public Produto removeById(String id) {
        LOGGER.info(String.format("Removendo o produto de id [%s] do banco de dados", id));
        Produto produto = getProduto(id);
        if (produto != null) {
            this.produtos.remove(produto);
        }
        LOGGER.info("Produto removido com sucesso");
        return produto;
    }

}
