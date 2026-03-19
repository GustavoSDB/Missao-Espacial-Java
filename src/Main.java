import controle.centroControle;
import database.FogueteDAO;
import database.SateliteDAO;
import database.missaoDAO;
import model.foguete;
import model.satelite;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        centroControle controle = new centroControle();

        boolean rodando = true;

        while(rodando){

            System.out.println("============================");
            System.out.println("1- Adicionar foguete");
            System.out.println("2- Adicionar satelite");
            System.out.println("3- Iniciar missao");
            System.out.println("4- Abastecer foguete");
            System.out.println("5- Status");
            System.out.println("6- Ativar paineis do satelite");
            System.out.println("7- Enviar mensagem");
            System.out.println("0- Sair do Menu");
            System.out.println("============================");
            int op = sc.nextInt();

            switch(op){
                case 1:
                    System.out.println("Digite o nome do foguete: ");
                    String nome = sc.next();
                    System.out.println("Digite o Combustivel :");
                    float combustivel = sc.nextFloat();
                    System.out.println("Digite a Carga Maxima: ");
                    float cargaMaxima = sc.nextFloat();
                    System.out.println("Digite o status: ");
                    String status = sc.next();

                    foguete foguete = new foguete(nome, combustivel, cargaMaxima, status);
                    controle.adicionarFoguete(foguete);
                    break;
                case 2:
                    System.out.println("Digite o nome do satelite: ");
                    String nomeSatelite = sc.next();
                    System.out.println("Digite a massa: ");
                    float massa = sc.nextFloat();
                    System.out.println("Digite a orbita alvo: ");
                    String orbita = sc.next();
                    System.out.println("Digite a energia: ");
                    float energia = sc.nextFloat();
                    System.out.println("Digite o status: ");
                    sc.nextLine();
                    String statusSatelite = sc.nextLine();

                    satelite satelite = new satelite(nomeSatelite,massa,orbita,energia,statusSatelite);
                    controle.adicionarSatelite(satelite);
                    break;
                case 3:
                    controle.mostrarFoguetes();
                    System.out.println("Digite o numero do foguete desejado: ");
                    int opFoguete = sc.nextInt();
                    controle.mostrarSatelites();
                    System.out.println("Digite o numero do satelite desejado: ");
                    int opSatelite = sc.nextInt();

                    controle.iniciarMissao(controle.getSatelite(opSatelite),controle.getFoguetes(opFoguete));
                    break;
                case 4:
                    controle.mostrarFoguetes();
                    System.out.println("Digite o foguete que voce quer abastecer: ");
                    int opcao = sc.nextInt();
                    System.out.println("Digite a quantidade de combustivel: ");
                    float quantidade = sc.nextFloat();

                    controle.getFoguetes(opcao).abastecer(quantidade);
                    break;
                case 5:
                    controle.statusMissao();
                    break;
                case 6:
                    controle.mostrarSatelites();
                    System.out.println("Digite o numero do satelite desejado: ");
                    int opPainel = sc.nextInt();
                    System.out.println("Digite o valor de energia abastecida: ");
                    float quantidadePainel = sc.nextFloat();

                    controle.getSatelite(opPainel).ativarPaineis(quantidadePainel);
                    break;
                case 7:
                    controle.mostrarSatelites();
                    System.out.println("Digite o numero do satelite desejado: ");
                    int opMensagem = sc.nextInt();
                    System.out.println("Digite a mensagem a ser enviada: ");
                    sc.nextLine();
                    String mensagemSatelite = sc.nextLine();

                    System.out.println(controle.getSatelite(opMensagem).enviarDados(mensagemSatelite));
                    break;
                case 0:
                    System.out.println("Programa encerrado! ");
                    rodando = false;
                    break;

                default:
                    System.out.println("Comando inexistente! Tente novamente!");
                    break;
            }
        }





    }
}
