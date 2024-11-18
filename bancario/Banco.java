package bancario;
import java.util.ArrayList;
import bancario.base.Cliente;
import bancario.base.Cuenta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Banco {
    String nombre;
    ArrayList<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>(4);
    }

    public Cuenta buscarCuenta(String numero) {

        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero().equals(numero)) {
                return cuenta;
            }
        }
        return null;
    }

    public boolean adicionarCuenta(String numero, double saldoInicial, String tipo, String cedulaTitular, String nombreTitular, Statement statement) {
    Cliente cliente = new Cliente(cedulaTitular, nombreTitular);
    Cuenta cuentaBuscar = this.buscarCuenta(numero);
    
    // Verificamos si la cuenta ya existe
    if (cuentaBuscar == null) {
        Cuenta cuenta = new Cuenta(tipo, numero, saldoInicial, cliente);
        // Insertamos la cuenta en la base de datos
        boolean exito = cuenta.insertarCuenta(statement);
        if (exito) {
            // Si la inserción fue exitosa, agregamos la cuenta a la lista en memoria (opcional)
            this.cuentas.add(cuenta);
        }
        return exito;
    } else {
        return false;
    }
}

    public double consultarTotalDinero() {
    double total = 0;
    
    // Obtener las cuentas desde la base de datos
    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "postgres", "Colombia2024")) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM cuentas");

        while (resultSet.next()) {
            double saldo = resultSet.getDouble("saldo_cuenta");
            total += saldo;  // Sumamos el saldo a total
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return total;
}

public String consultarClienteMayorSaldo() {
    double mayorSaldo = 0;
    String nombreTitular = "";

    // Obtener el cliente con el mayor saldo desde la base de datos
    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "postgres", "Colombia2024")) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT nombre_titular, saldo_cuenta FROM cuentas");

        while (resultSet.next()) {
            double saldo = resultSet.getDouble("saldo_cuenta");
            String nombre = resultSet.getString("nombre_titular");

            if (saldo > mayorSaldo) {
                mayorSaldo = saldo;
                nombreTitular = nombre;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nombreTitular.isEmpty() ? "Nadie" : nombreTitular;
}
}