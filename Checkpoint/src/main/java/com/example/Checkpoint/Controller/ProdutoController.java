package com.example.Checkpoint.Controller;

import com.example.Checkpoint.Model.Produto;
import com.example.Checkpoint.Service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/v1/Produto")
public class ProdutoController {

    private final ProdutoService produtoService;


    public ProdutoController(@Autowired ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Endpoint para listar todos os produtos
    // Endpoint Sem Json
    @GetMapping
    public ResponseEntity<List<Produto>> exibirTodosProdutos() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    // Endpoint para criar um novo produto
    // JSON: {
    //    "nome":"XXXX",
    //    "preco":Y,
    //    "quantidadeEstoque":x
    //}
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.save(produto));
    }

    // Endpoint para atualizar um produto por ID
    //{
    //    "nome":"xxxx",
    //    "preco":x,
    //    "quantidadeEstoque":x
    //}
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Optional<Produto> produtos = produtoService.findById(id);
        if (produtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(produtoService.atualizaProduto(id, produto));
        }
    }

    // Endpoint para obter um produto por ID
    // Endpoint Sem Json
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(produto.get());
        }
    }

    // Endpoint para excluir um produto por ID
    // Endpoint Sem Json
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
