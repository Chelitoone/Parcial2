import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

import bancario.OperacionesBanco;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/banco";
        String user = "postgres";
        String password = "Colombia2024";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión exitosa");
            Statement statement = connection.createStatement();


            ResultSet resultSet = statement.executeQuery("SELECT * FROM cuentas");

            while (resultSet.next()) {

                // Acceder a los datos de cada fila
                int idCuenta = resultSet.getInt("id_cuenta");
                String numeroCuenta = resultSet.getString("numero_cuenta");
                String tipoCuenta = resultSet.getString("tipo_cuenta");
                double saldo = resultSet.getDouble("saldo_cuenta");

                // Imprimir los resultados
                System.out.println("ID: " + idCuenta + ", Numero: " + numeroCuenta + ", Tipo: " + tipoCuenta + ", Saldo: " + saldo);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }

        OperacionesBanco operacionesBanco = new OperacionesBanco();
        operacionesBanco.menuOpciones();
    }
}