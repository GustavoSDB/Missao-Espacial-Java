public class Main {
    public static void main(String[] args) {
        centroControle controle = new centroControle();
        foguete a = new foguete("abc",50,100,"pronto");
        foguete b = new foguete("def",20,100,"Em orbita");

        satelite terra = new satelite("terra",100,"terra",20,"Pronto");
        satelite comunicacao = new satelite("comunicacao",100,"comunicacao",20,"Pronto");
        satelite cientifico = new satelite("cientifico",100,"cientifico",20,"Pronto");

        controle.adicionarFoguete(a);
        controle.adicionarFoguete(b);

        controle.adicionarSatelite(terra);
        controle.adicionarSatelite(comunicacao);
        controle.adicionarSatelite(cientifico);

        a.abastecer(20);
        controle.statusMissao();
        controle.iniciarMissao(terra,a);
        controle.statusMissao();
        terra.enviarDados("Oi");

    }
}
