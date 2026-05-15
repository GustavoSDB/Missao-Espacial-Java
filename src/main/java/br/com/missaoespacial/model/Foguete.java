package br.com.missaoespacial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "foguetes")
public class Foguete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private float combustivelRestante;

    @Column(nullable = false)
    private float cargaMaxima;

    @Column(nullable = false)
    private String status;

    public Foguete() {
    }

    public Foguete(String nome, float combustivelRestante, float cargaMaxima, String status) {
        this.nome = nome;
        this.combustivelRestante = combustivelRestante;
        this.cargaMaxima = cargaMaxima;
        this.status = status;
    }

    public void abastecer(float quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de combustivel deve ser maior que zero.");
        }
        combustivelRestante += quantidade;
    }

    public boolean lancar() {
        if (combustivelRestante >= 50) {
            combustivelRestante = Math.max(0, combustivelRestante - 50);
            status = "Em missao";
            return true;
        }

        status = "falha";
        return false;
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

    public float getCombustivelRestante() {
        return combustivelRestante;
    }

    public void setCombustivelRestante(float combustivelRestante) {
        this.combustivelRestante = combustivelRestante;
    }

    public float getCargaMaxima() {
        return cargaMaxima;
    }

    public void setCargaMaxima(float cargaMaxima) {
        this.cargaMaxima = cargaMaxima;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
