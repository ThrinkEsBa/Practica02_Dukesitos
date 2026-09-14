package src;
import java.util.Scanner;

public class VistaConsola {

    public static void menúPrincipal() {
        System.out.println("==== Menú Principal ====");
        System.out.println("1. Gestionar Clientes");
        System.out.println("2. Gestionar Sucursales");
        System.out.println("3. Gestionar Premios");
        System.out.println("4. Salir");
    }

    public static void menúDeGestión() {
        System.out.println("1. Agregar registro");
        System.out.println("2. Consultar todos los registros");
        System.out.println("3. Editar registro");
        System.out.println("4. Eliminar registro");
        System.out.println("5. Regresar al Menú Principal");
    }

    public static int manejadorDeEntradas(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("  Error: ingresa un numero entero valido.");
            }
        }
    }

    public static String manejadorDeStrings(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada; 
            } else {
                System.out.println("  Error: el texto no puede estar vacío. Intenta de nuevo.");
            }
        }
    }
}
