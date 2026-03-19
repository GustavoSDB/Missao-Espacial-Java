package database;

import model.satelite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SateliteDAO {
    public static int salvar(satelite s){
        try{
            Connection conn = conexao.Conectar();

            String sql = "INSERT INTO satelite (nome, massa, orbitaAlvo, energia, status) Values (?, ?, ?, ?,?)";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, s.getNome());
            ps.setFloat(2, s.getMassa());
            ps.setString(3, s.getOrbitaAlvo());
            ps.setFloat(4, s.getEnergia());
            ps.setString(5, s.getStatus());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                int id = rs.getInt(1);
                System.out.println("ID gerado: " + id);
                return id;
            }


            System.out.println("Satelite salvo no banco!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
}
