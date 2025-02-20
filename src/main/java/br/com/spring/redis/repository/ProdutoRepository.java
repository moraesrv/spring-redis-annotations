package br.com.spring.redis.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import br.com.spring.redis.collection.Produto;

@Repository
public class ProdutoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRepository.class);

    private List<Produto> produtos = new ArrayList<>();

    public ProdutoRepository() {
        this.produtos.add(new Produto("1", "TV", 1999.99));
        this.produtos.add(new Produto("2", "Soundbar", 1299.99));
        this.produtos.add(new Produto("3", "Iphone", 8999.99));
        this.produtos.add(new Produto("4", "Notebook", 5999.99));
        this.produtos.add(new Produto("5", "Headset", 399.99));
    }

    /**
     * Método utilizado para simular a latência de uma consulta ao banco de dados
     */
    private void latencia() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por localizar um produto dentro da lista
    */
    private Produto getProduto(String id) {
        return this.produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findAny()
            .get();
    }

    /**
     * Método utilizado para adicionar um produto na lista
     */
    public Produto create(Produto produto) {
        LOGGER.info(String.format("Inserindo o produto de id [%s] no banco de dados", produto.getId()));
        this.produtos.add(produto);
        LOGGER.info("Produto cadastrado com sucesso");
        return produto;
    }

    /**
     * O método anotado com @Cacheable pesquisa o produto no cache, caso exista retorna o produto, senão
     * pesquisa o produto no repositório e adiciona ao cache
     * @param id identificação do produto
     * @return informações do produto
     */
    @Cacheable("produtos")
    public Produto findById(String id) {
        LOGGER.info(String.format("Pesquisando o produto de id [%s] no banco de dados", id));
        latencia();
        return getProduto(id);
    }

    /**
     * O método anotado com @CachePut pesquisa o produto no repositório e atualiza o cache
     * @param id identificação do produto
     * @param produto dados do produto a serem atualizados
     * @return informações do produto
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
     * O método anotado com @CacheEvict é utilizado para remover um item do cache
     * @param id identificação do produto
     * @return informações do produto
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
