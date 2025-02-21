package br.com.spring.redis.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.spring.redis.entity.Produto;
import br.com.spring.redis.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto buscarProduto(String id) {
        LOGGER.info(String.format("Solicitada pesquisa do produto de id [%s]", id));
        return produtoRepository.findById(id);
    }

    public Produto adicionarProduto(Produto produto) {
        LOGGER.info(String.format("Solicitado o cadastro do produto de id [%s]", produto.getId()));
        return produtoRepository.create(produto);
    }

    public Produto atualizarProduto(String id, Produto produto) {
        LOGGER.info(String.format("Solicitada a atualização do produto de id [%s]", id));
        return produtoRepository.update(id, produto);
    }

    public Produto removerProduto(String id) {
        LOGGER.info(String.format("Solicitação a remoção do produto de id [%s]", id));
        return produtoRepository.removeById(id);
    }

}
