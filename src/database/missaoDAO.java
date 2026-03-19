package database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class missaoDAO {
    public static void salvar(int fogueteID, int sateliteID, String status){

        try{

            Connection conn = conexao.Conectar();

            String sql = "INSERT INTO missao(fogueteID, sateliteID, status) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, fogueteID);
            ps.setInt(2, sateliteID);
            ps.setString(3, status);

            ps.execute();

            System.out.println("Missao salva!");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
