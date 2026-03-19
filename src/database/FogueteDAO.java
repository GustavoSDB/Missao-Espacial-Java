package database;

import model.foguete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FogueteDAO {
    public static int salvar(foguete f){
        try{
            Connection conn = conexao.Conectar();

            String sql = "INSERT INTO foguete (nome, combustivel, cargaMaxima, status) Values (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, f.getNome());
            ps.setFloat(2, f.getCombustivelRestante());
            ps.setFloat(3, f.getCargaMaxima());
            ps.setString(4, f.getStatus());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                int id = rs.getInt(1);
                System.out.println("ID gerado: " + id);
                return id;
            }

            System.out.println("Foguete salvo no banco!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

}
