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
        System.out.println("Foguete: " + f + " Satelite: " + s);
        f.lancar();
        s.orbita();

    }

    public void statusMissao(){
        for(int i = 0; i < listaSatelites.size(); i++){
            System.out.println((String)listaSatelites.get(i).toString());
        }

        for(int i = 0; i < listaFoguetes.size(); i++){
            System.out.println((String)listaFoguetes.get(i).toString());
        }
    }

}
