package src;
import java.util.Scanner;


interface MainInterface {

    /**
     * Controlador central de la Base de Datos Imprimirá un menú interactivo
     * para hacer el CRUD de la base de datos y hará que el usuario lo controle.
     *
     * @param base La base de datos a controlar
     * @return Lo que vaya a regresar la base de datos.
     */
    public String controlBD(BD base);

}

public class Main implements MainInterface {

    private static final Scanner scanner = new Scanner(System.in);


    @Override
    public String controlBD(BD base) { 
        int opción;
        int next;

        while (true) {
            VistaConsola.menúPrincipal();
            opción = VistaConsola.manejadorDeEntradas(scanner);

            
            if (opción == 4) {
                System.out.println("Saliendo del programa...");
                return ""; 
            }

            if (opción < 1 || opción > 4) {
                System.out.println("Seleccionar opción válida.");
                continue; 
            }

            VistaConsola.menúDeGestión();
            next = VistaConsola.manejadorDeEntradas(scanner);

            int fileIndex = opción - 1; 
            int upd = 0;
            String data = "";

            switch (next) {
                case 1:
                    System.out.println("Dame el dato a añadir:");
                    data = VistaConsola.manejadorDeStrings(scanner);
                    base.create(data, fileIndex);
                    System.out.println("Dato añadido correctamente.");
                    break;
                case 2:
                    System.out.println("\n--- CONTENIDO DE LA BASE DE DATOS ---");
                    System.out.println(base.read(fileIndex)); 
                    System.out.println("-------------------------------------\n");
                    break;
                case 3:
                    System.out.println("¿Qué id quieres editar?");
                    upd = VistaConsola.manejadorDeEntradas(scanner);
                    System.out.println("¿Qué irá en su lugar?");
                    data = VistaConsola.manejadorDeStrings(scanner);
                    base.update(upd, data, fileIndex);
                    System.out.println("Dato actualizado correctamente.");
                    break;
                case 4:
                    System.out.println("¿Qué id quieres eliminar?");
                    upd = VistaConsola.manejadorDeEntradas(scanner);
                    base.delete(upd, fileIndex); 
                    System.out.println("Dato eliminado correctamente.");
                    break;
                default:
                    System.out.println("Opción de gestión no válida.");
                    break;
            }
        }
    }

    private boolean  checkPresentation(String data, int file){
        if (data == null || data.trim().isEmpty()) return false;
        String[] partes = data.split(",");

        switch (file) {
            case 2:
                
                break;
            default:
                throw new AssertionError();
        }

        return false;
    }


    public static void main(String[] args) {
        Main mn = new Main();
        BD base = new BD();
        mn.controlBD(base);
    }
}
