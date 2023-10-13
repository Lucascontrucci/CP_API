package com.example.Checkpoint.Repository;

import com.example.Checkpoint.Model.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProdutoRepository {

    private final ConcurrentHashMap<Long, Produto> produtos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    // Retorna todos os produtos
    public List<Produto> findAll() {
        return new ArrayList<>(produtos.values());
    }

    // Salva um novo produto ou atualiza um existente
    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(idGenerator.incrementAndGet());
        }

        produtos.put(produto.getId(), produto);
        return produto;
    }

    // Encontra um produto por ID
    public Optional<Produto> findById(Long id) {
        return Optional.ofNullable(produtos.get(id));
    }

    // Exclui um produto por ID
    public void deleteById(Long id) {
        produtos.remove(id);
    }
}
