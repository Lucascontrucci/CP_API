package com.example.Checkpoint.Service;

import com.example.Checkpoint.Model.Pedido;
import com.example.Checkpoint.Model.Produto;
import com.example.Checkpoint.Repository.PedidoRepository;
import com.example.Checkpoint.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    private final double TAXA_IMPOSTO = 0.0535;

    public PedidoService(@Autowired PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    // Exibe todos os pedidos
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    // Salva um novo pedido
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // Encontra um pedido por ID
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    // Cria um novo pedido
    public Pedido criarPedido() {
        Pedido pedido = new Pedido();
        return pedidoRepository.save(pedido);
    }

    // Deletar por ID
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    // Adiciona um produto ao pedido, gerenciando quantidade solicitada e estoque
    public Pedido adicionarProdutoAoPedido(Long pedidoId, Long produtoId, int quantidade) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);

        if (pedidoOptional.isPresent() && produtoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            Produto produto = produtoOptional.get();

            int quantidadeSolicitada = quantidade;
            int quantidadeEstoque = produto.getQuantidadeEstoque();

            // Verifique se o produto já está no pedido
            for (Produto p : pedido.getProdutos()) {
                if (p.getId().equals(produtoId)) {
                    quantidadeSolicitada += p.getQuantidadeSolicitada();
                    break;
                }
            }

            if (quantidadeSolicitada <= quantidadeEstoque) {
                pedido.getProdutos().removeIf(p -> p.getId().equals(produtoId)); // Remove se estiver no pedido
                produto.setQuantidadeSolicitada(quantidadeSolicitada);
                pedido.getProdutos().add(produto);
                double custoProduto = produto.getPreco() * quantidade;
                pedido.setTotal(pedido.getTotal() + custoProduto);
                produto.setQuantidadeEstoque(quantidadeEstoque - quantidadeSolicitada); // subtrai do estoque
                pedidoRepository.save(pedido);
                produtoRepository.save(produto);
                return calcularTotal(pedido);
            } else {
                throw new IllegalArgumentException("Estoque insuficiente para o produto");
            }
        } else {
            throw new IllegalArgumentException("Pedido ou produto não encontrado");
        }
    }

    // Remove um produto do pedido, adiciona a quantidade de volta ao estoque e zera a quantidade solicitada
    public Pedido removerProdutoDoPedido(Long pedidoId, Long produtoId) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);

        if (pedidoOptional.isPresent() && produtoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            Produto produto = produtoOptional.get();

            pedido.getProdutos().removeIf(p -> p.getId().equals(produtoId));
            int quantidadeSolicitada = produto.getQuantidadeSolicitada();
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidadeSolicitada);
            produto.setQuantidadeSolicitada(0);
            pedido.setTotal(0);

            pedidoRepository.save(pedido);
            produtoRepository.save(produto);
            return calcularTotal(pedido);
        } else {
            throw new IllegalArgumentException("Pedido ou produto não encontrado");
        }
    }

    // Calcula o subtotal, impostos e total do pedido com base nos produtos
    public Pedido calcularTotal(Pedido pedido) {
        double subtotal = 0.0;
        double impostos;
        double total;

        for (Produto produto : pedido.getProdutos()) {
            subtotal += produto.getPreco() * produto.getQuantidadeSolicitada();
        }

        impostos = subtotal * TAXA_IMPOSTO;

        total = subtotal + impostos;

        pedido.setSubtotal(subtotal);
        pedido.setImposto(impostos);
        pedido.setTotal(total);

        pedidoRepository.save(pedido);

        return pedido;
    }
}
