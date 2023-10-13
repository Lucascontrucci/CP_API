package com.example.Checkpoint.Repository;

import com.example.Checkpoint.Model.Pedido;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PedidoRepository {

    private final ConcurrentHashMap<Long, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    // Retorna todos os pedidos
    public List<Pedido> findAll() {
        return new ArrayList<>(pedidos.values());
    }

    // Salva um novo pedido ou atualiza um existente
    public Pedido save(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(idGenerator.incrementAndGet());
        }

        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    // Encontra um pedido por ID
    public Optional<Pedido> findById(Long id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    // Deletar por id
    public void deleteById(Long id) {
        pedidos.remove(id);
    }
}
