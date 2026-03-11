
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
        if (energia + quantidade <= 100){
            energia = energia + quantidade;
            System.out.println("Paineis atualizado com sucesso!");
        }else{
            System.out.println("Limite de energia ultrapassado!");
        }
    }

    public String enviarDados(String mensagem){
        if (status.equals("Ativo")){
            return "Satelite: " + nome + " mensagem: " + mensagem;
        }else{
            return "O Satelite precisa estar ativo";
        }
    }

    public String toString(){
        return "Nome: " + nome + "massa: " + massa + "orbita alvo: " + orbitaAlvo + "energia: " + energia + "status: " + status;
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


}


