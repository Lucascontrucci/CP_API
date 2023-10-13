package com.example.Checkpoint.Model;

public class AdicionarProdutoSolicitacao {

    // Essa classe utiliza as variaveis abaixo no JSON para adicionar a quantidade e mencionar o ID do produto

    private Long produtoId;
    private int quantidade;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
