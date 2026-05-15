package br.com.missaoespacial.dto;

import br.com.missaoespacial.model.Satelite;

public class SateliteRequest {

    private String nome;
    private float massa;
    private String orbitaAlvo;
    private float energia;
    private String status;

    public Satelite toModel() {
        return new Satelite(nome, massa, orbitaAlvo, energia, status);
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

    public float getMassa() {
        return massa;
    }

    public void setMassa(float massa) {
        this.massa = massa;
    }

    public void setMass(float mass) {
        this.massa = mass;
    }

    public String getOrbitaAlvo() {
        return orbitaAlvo;
    }

    public void setOrbitaAlvo(String orbitaAlvo) {
        this.orbitaAlvo = orbitaAlvo;
    }

    public void setOrbit(String orbit) {
        this.orbitaAlvo = orbit;
    }

    public float getEnergia() {
        return energia;
    }

    public void setEnergia(float energia) {
        this.energia = energia;
    }

    public void setEnergy(float energy) {
        this.energia = energy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
