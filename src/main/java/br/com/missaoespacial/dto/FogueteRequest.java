package br.com.missaoespacial.dto;

import br.com.missaoespacial.model.Foguete;

public class FogueteRequest {

    private String nome;
    private float combustivelRestante;
    private float cargaMaxima;
    private String status;

    public Foguete toModel() {
        return new Foguete(nome, combustivelRestante, cargaMaxima, status);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public float getCombustivelRestante() {
        return combustivelRestante;
    }

    public void setCombustivelRestante(float combustivelRestante) {
        this.combustivelRestante = combustivelRestante;
    }

    public void setFuel(float fuel) {
        this.combustivelRestante = fuel;
    }

    public float getCargaMaxima() {
        return cargaMaxima;
    }

    public void setCargaMaxima(float cargaMaxima) {
        this.cargaMaxima = cargaMaxima;
    }

    public void setMaxLoad(float maxLoad) {
        this.cargaMaxima = maxLoad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
