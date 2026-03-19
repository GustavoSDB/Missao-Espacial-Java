package controle;

import model.foguete;
import model.satelite;

import java.util.ArrayList;
public class centroControle{
    private ArrayList<foguete> listaFoguetes = new ArrayList<>();
    private ArrayList<satelite> listaSatelites = new ArrayList<>();

    public void adicionarFoguete(foguete f){
        listaFoguetes.add(f);
    }

    public void adicionarSatelite(satelite s){
        listaSatelites.add(s);
    }

    public void iniciarMissao(satelite s,foguete f){
        System.out.println("Foguete: " + f.getNome() + " Satelite: " + s.getNome());
        f.lancar();
        if(f.getStatus().equals("falha")){
            System.out.println("Foguete sem energia! Missao cancelada!!!");

        }else if(f.getCargaMaxima() < s.getMassa()){
            System.out.println("Massa do satelite ultrapassa capacidade do foguete! Missao cancelada!!!");
        }else{
            s.orbita();
        }

    }

    public void statusMissao(){
        System.out.println("Satelites:");
        for(int i = 0; i < listaSatelites.size(); i++){
            System.out.println((String)listaSatelites.get(i).toString());
        }

        System.out.println("Foguetes:");
        for(int i = 0; i < listaFoguetes.size(); i++){
            System.out.println((String)listaFoguetes.get(i).toString());
        }
    }

    public void mostrarFoguetes(){
        for(int i = 0; i < listaFoguetes.size(); i++){
            System.out.println(i + " - " + listaFoguetes.get(i).getNome());
        }
    }

    public void mostrarSatelites(){
        for(int i = 0; i < listaSatelites.size(); i++){
            System.out.println(i + " - " + listaSatelites.get(i).getNome());
        }
    }

    public foguete getFoguetes(int opFoguete){
        return listaFoguetes.get(opFoguete);
    }

    public satelite getSatelite(int opSatelite){
        return listaSatelites.get(opSatelite);
    }

}
