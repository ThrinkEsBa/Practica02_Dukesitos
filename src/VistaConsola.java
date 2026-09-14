
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
        System.out.println("2. Consultar por Llave(ID)");
        System.out.println("3. Consultar todos los registros");
        System.out.println("4. Editar registro");
        System.out.println("5. Eliminar registro");
        System.out.println("6. Regresar al Menú Principal");
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
}
