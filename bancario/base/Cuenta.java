package bancario.base;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Cuenta {
    double saldo;
    String numero;
    String tipo;
    Cliente titular;

    public Cuenta(String tipo, String numero, double saldo, Cliente titular) {
        this.tipo = tipo;
        this.numero = numero;
        this.saldo = saldo;
        this.titular = titular;
    }

    public Cuenta(String tipo, String numero, Cliente titular) {
        this(tipo, numero, 0, titular);
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double consultarSaldo() {
        return saldo;
    }

    public boolean retirar(double cantidad) {
        if (saldo >= cantidad) {
            saldo -= cantidad;
            return true;
        } else {
            return false;
        }
    }

    public void consignar(double cantidad) {
        saldo += cantidad;
    }

    public boolean insertarCuenta(Statement statement) {
    String sql = "INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_cuenta, nombre_titular, cedula_titular) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql)) {
        preparedStatement.setString(1, this.numero);
        preparedStatement.setString(2, this.tipo);
        preparedStatement.setDouble(3, this.saldo);
        preparedStatement.setString(4, this.titular.getNombre());
        preparedStatement.setString(5, this.titular.getCedula());
        
        // Ejecutamos la inserción
        int rowsAffected = preparedStatement.executeUpdate();
        
        if (rowsAffected > 0) {
            return true;  // La inserción fue exitosa
        } else {
            return false; // No se insertó ninguna fila
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Hubo un error al insertar
    }
}
}