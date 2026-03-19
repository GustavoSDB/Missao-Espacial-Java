package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexao {

    public static Connection Conectar(){
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
            System.out.println("Conectado com sucesso!");
            return conn;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
