package model;

public class foguete {
    private String nome;
    private float combustivelRestante;
    private float cargaMaxima;
    private String status;

    public foguete(String nome, float combustivelRestante, float cargaMaxima, String status){
        this.nome = nome;
        this.combustivelRestante = combustivelRestante;
        this.cargaMaxima = cargaMaxima;
        this.status = status;
    }

    public void abastecer(float quantidade){
        combustivelRestante = combustivelRestante + quantidade;
        System.out.println("Foguete: " + nome + " abastecido com sucesso! Combustivel:" + combustivelRestante);
    }

    public void lancar(){
        if (combustivelRestante >= 50){
            status = "Lancando";
        }else{
            status = "falha";
            System.out.println("combustivel insuficiente para o lancamento!");
        }
    }

    public String toString(){
        return "Nome: " + nome + "\nCombustivel restante:" + combustivelRestante + "\n Carga maxima: " + cargaMaxima + "\nStatus: " + status + "\n===================================";
    }

    public String getNome() {
        return nome;
    }
    public String getStatus() { return status;}
    public float getCombustivelRestante() { return combustivelRestante;}
    public float getCargaMaxima() { return cargaMaxima;}
}
