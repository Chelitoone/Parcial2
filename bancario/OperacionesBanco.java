package bancario;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import bancario.base.Cuenta;

public class OperacionesBanco {
    private String decisionTxt;
    private int decision;
    private Banco banco = new Banco();
    private int numCuentas = 0;


    public OperacionesBanco() {
    }

    public void menuOpciones(){

        JOptionPane.showMessageDialog(null, "Bienvenido al Banco CheloMuchoDinero que deseas hacer hoy?");
        decisionTxt= JOptionPane.showInputDialog("1: Agregar Cuenta \n" +
                        "2: Buscar cuenta\n" +
                        "3: Consignar\n" + 
                        "4: retirar dinero\n" + 
                        "5: consultar total de dinero del banco\n" + 
                        "6: consultar cliente con mayor dinero");
        decision = Integer.parseInt(decisionTxt);
        menu(decision);

    }
    
    private void menu(int num){
        switch (num) {

            case 1:
            numCuentas += 1;
            String numCuentasString = String.valueOf(numCuentas);
            String nombreTitular = JOptionPane.showInputDialog("Ingresa el nombre del titular de la cuenta\n");
            String cedulaTitular = JOptionPane.showInputDialog("Ingresa la cedula del titular de la cuenta\n");
            String tipoCuenta = JOptionPane.showInputDialog("Ingresa el tipo de la cuenta, \nAhorros\nCorriente\n");
            String saldoInicialStr = JOptionPane.showInputDialog("Ingresa el saldo inicial de la cuenta \n");
            double saldoInicial = Double.parseDouble(saldoInicialStr);
            
            // Crear una conexión con la base de datos
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "postgres", "Colombia2024")) {
                System.out.println("Conexión exitosa");

                Statement statement = connection.createStatement();
                boolean seAgrego = banco.adicionarCuenta(numCuentasString, saldoInicial, tipoCuenta, cedulaTitular, nombreTitular, statement);
                
                if (seAgrego) {
                    JOptionPane.showMessageDialog(null, "Registro exitoso");
                } else {
                    JOptionPane.showMessageDialog(null, "No se registró la cuenta");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
            }
                break;
            
                case 2:
                    String cuentaABuscar = JOptionPane.showInputDialog("Ingresa el numero de la cuenta a buscar\n");
                    Cuenta busqueda = banco.buscarCuenta(cuentaABuscar);
                    JOptionPane.showMessageDialog(null, "Titular: "+ busqueda.getTitular().getNombre() +"\n"
                                                                        + "Cedula titular: "+ busqueda.getTitular().getCedula() +"\n" 
                                                                        + "Tipo de cuenta: "+ busqueda.getTipo() + "\n"
                                                                        + "Saldo: " + busqueda.getSaldo() + "\n"
                                                                        );
                    break;

                case 3:
                    cuentaABuscar = JOptionPane.showInputDialog("Ingresa el numero de la cuenta a consignar\n");
                    busqueda = banco.buscarCuenta(cuentaABuscar);

                    if (busqueda == null) {
                        JOptionPane.showMessageDialog(null, "La cuenta no existe");
                    }else{
                        String cantidadConsignarStr = JOptionPane.showInputDialog("Ingresa la cantidad a consignar");
                        double cantidadConsignar = Double.parseDouble(cantidadConsignarStr);
                        busqueda.consignar(cantidadConsignar);
                    }
                    break;

                case 4:
                    cuentaABuscar = JOptionPane.showInputDialog("Ingresa el numero de la cuenta a retirar\n");
                    busqueda = banco.buscarCuenta(cuentaABuscar);
                    if (busqueda == null) {
                        JOptionPane.showMessageDialog(null, "La cuenta no existe");
                    }else{
                        String cantidadConsignarStr = JOptionPane.showInputDialog("Ingresa la cantidad a retirar");
                        double cantidadConsignar = Double.parseDouble(cantidadConsignarStr);
                        busqueda.retirar(cantidadConsignar);
                    }
                    break;
                
                case 5:
                    double totalDineroBanco = banco.consultarTotalDinero();
                    JOptionPane.showMessageDialog(null, "El total de dinero del banco es: "+ totalDineroBanco);
                    break;
                case 6:
                    String nombreMayorDinero = banco.consultarClienteMayorSaldo();
                    JOptionPane.showMessageDialog(null, "El cliente con mayor dinero es: "+ nombreMayorDinero);    
            default:
                break;
        }
    }
}