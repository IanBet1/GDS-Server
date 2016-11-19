package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoMySQL {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/gds", "root", "");
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

}
