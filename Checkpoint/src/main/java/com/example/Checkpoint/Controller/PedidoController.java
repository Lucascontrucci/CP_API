package com.example.Checkpoint.Controller;

import com.example.Checkpoint.Model.AdicionarProdutoSolicitacao;
import com.example.Checkpoint.Model.Pedido;
import com.example.Checkpoint.Model.Produto;
import com.example.Checkpoint.Service.PedidoService;
import com.example.Checkpoint.Service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/v1/Pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;


    public PedidoController(@Autowired PedidoService pedidoService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    // Endpoint para listar todos os pedidos
    // Endpoint Sem Json
    @GetMapping
    public ResponseEntity<List<Pedido>> exibirTodosPedidos() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    // Endpoint para criar um novo pedido
    // Endpoint Sem Json
    @PostMapping
    public ResponseEntity<Pedido> criarPedido() {
        Pedido pedido = pedidoService.criarPedido();
        return ResponseEntity.ok(pedido);
    }

    // Endpoint para adicionar um produto a um pedido específico
    // JSON: { "produtoId": y, "quantidade": x }
    @PostMapping("/{pedidoId}/adicionarProduto")
    public ResponseEntity<Pedido> adicionarProdutoAoPedido(
            @PathVariable Long pedidoId,
            @RequestBody AdicionarProdutoSolicitacao solicitacao) {
        Optional<Pedido> pedido = pedidoService.findById(pedidoId);
        if (pedidoService.findById(pedidoId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(pedidoService.adicionarProdutoAoPedido(pedidoId, solicitacao.getProdutoId(), solicitacao.getQuantidade()));
        }
    }

    // Endpoint para obter um pedido por ID
    // Endpoint Sem Json
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.findById(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(pedido.get());
        }
    }

    // Endpoint para remover um produto de um pedido
    // Endpoint Sem Json
    @DeleteMapping("/{pedidoId}/removerProduto/{produtoId}")
    public ResponseEntity<Pedido> removerProdutoDoPedido(@PathVariable Long pedidoId, @PathVariable Long produtoId) {
        if (pedidoService.findById(pedidoId).isEmpty() || produtoService.findById(produtoId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.removerProdutoDoPedido(pedidoId, produtoId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para excluir um pedido por ID
    // Endpoint Sem Json
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        if (pedidoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
