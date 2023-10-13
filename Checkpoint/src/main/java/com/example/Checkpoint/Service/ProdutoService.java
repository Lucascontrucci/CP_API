package com.example.Checkpoint.Service;

import com.example.Checkpoint.Model.Produto;
import com.example.Checkpoint.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Injetar repositório
    public ProdutoService(@Autowired ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Exibe todos os produtos
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    // Salva um novo produto
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Encontra um produto por ID
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    // Exclui um produto por ID
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }

    // Atualiza os atributos de um produto com base no ID
    public Produto atualizaProduto(Long id, Produto novoProduto) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();

            // Atualiza os atributos do produto
            produto.setNome(novoProduto.getNome());
            produto.setQuantidadeEstoque(novoProduto.getQuantidadeEstoque());
            produto.setPreco(novoProduto.getPreco());


            produtoRepository.save(produto);

            return produto;
        } else {
            return null;
        }
    }
}
