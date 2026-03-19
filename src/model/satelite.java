package model;

public class satelite {

    private String nome;
    private float massa;
    private String orbitaAlvo;
    private float energia;
    private String status; // 1- Em solo / 2- Em orbita / 3- Ativo / 4- Desativado

    public satelite(String nome, float massa, String orbitaAlvo, float energia, String status){
        this.nome = nome;
        this.massa = massa;
        this.orbitaAlvo = orbitaAlvo;
        this.energia = energia;
        this.status = status;
    }

    public void ativarPaineis(float quantidade){
        if (energia + quantidade <= 500 && status.equals("Em orbita")){
            energia = energia + quantidade;
            System.out.println("Paineis atualizado com sucesso!");
            System.out.println("Energia atual: " + energia);
        }else{
            System.out.println("Limite de energia ultrapassado ou satelite não está em orbita!");
        }
    }

    public String enviarDados(String mensagem){
        if (status.equals("Em orbita")){
            return "Satelite: " + nome + " mensagem: " + mensagem;
        }else{
            return "O Satelite precisa estar Em orbita!";
        }
    }

    public String toString(){
        return "Nome: " + nome + "\nmassa: " + massa + "\norbita alvo: " + orbitaAlvo + "\nenergia: " + energia + "\nstatus: " + status + "\n=====================";
    }

    public void orbita(){
        status = "Em orbita";
        System.out.println("Satelite em orbita");
    }

    public String getNome() {
        return nome;
    }
    public float getMassa() {
        return massa;
    }
    public String getOrbitaAlvo() {return orbitaAlvo;}
    public float getEnergia() {return energia;}
    public String getStatus() {return status;}



}


