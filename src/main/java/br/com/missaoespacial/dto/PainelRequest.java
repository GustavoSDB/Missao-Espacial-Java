package br.com.missaoespacial.dto;

public class PainelRequest {

    private float quantidade;
    private String painel;

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public String getPainel() {
        return painel;
    }

    public void setPainel(String painel) {
        this.painel = painel;
    }
}
