package br.com.missaoespacial.model;

import java.util.ArrayList;
import java.util.List;

public class Satelite {

    private Long id;
    private String nome;
    private float massa;
    private String orbitaAlvo;
    private float energia;
    private String status;
    private List<String> paineis = new ArrayList<>();
    private String ultimaMensagem;

    public Satelite() {
    }

    public Satelite(String nome, float massa, String orbitaAlvo, float energia, String status) {
        this.nome = nome;
        this.massa = massa;
        this.orbitaAlvo = orbitaAlvo;
        this.energia = energia;
        this.status = status;
    }

    public void ativarPaineis(float quantidade) {
        ativarPaineis("Solar principal", quantidade);
    }

    public void ativarPaineis(String painel, float quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de energia deve ser maior que zero.");
        }

        if (!estaOperacional()) {
            throw new IllegalStateException("O satelite precisa estar Em orbita para ativar os paineis.");
        }

        if (painel != null && !painel.isBlank() && !paineis.contains(painel)) {
            paineis.add(painel);
        }

        energia = Math.min(100, energia + quantidade);
        status = "Paineis ativos";
    }

    public String enviarDados(String mensagem) {
        if (!estaOperacional()) {
            throw new IllegalStateException("O satelite precisa estar Em orbita para enviar dados.");
        }

        ultimaMensagem = mensagem;
        return "Satelite: " + nome + " mensagem: " + mensagem;
    }

    public void orbita() {
        status = "Em orbita";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getMassa() {
        return massa;
    }

    public void setMassa(float massa) {
        this.massa = massa;
    }

    public String getOrbitaAlvo() {
        return orbitaAlvo;
    }

    public void setOrbitaAlvo(String orbitaAlvo) {
        this.orbitaAlvo = orbitaAlvo;
    }

    public float getEnergia() {
        return energia;
    }

    public void setEnergia(float energia) {
        this.energia = energia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPaineis() {
        return paineis;
    }

    public void setPaineis(List<String> paineis) {
        this.paineis = paineis == null ? new ArrayList<>() : paineis;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    private boolean estaOperacional() {
        return "Em orbita".equals(status) || "Paineis ativos".equals(status);
    }
}
