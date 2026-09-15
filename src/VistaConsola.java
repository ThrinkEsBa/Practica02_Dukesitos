import java.util.Scanner;

/**
 * Contiene los menús y las validaciones básicas de entrada.
 */
public class VistaConsola {

    /** Muestra el menú principal. */
    public static void menuPrincipal() {
        System.out.println("\n==== PuellaGame ====");
        System.out.println("1. Gestionar clientes");
        System.out.println("2. Gestionar sucursales");
        System.out.println("3. Gestionar premios");
        System.out.println("4. Salir");
        System.out.print("Opción: ");
    }

    /** Muestra el menú CRUD de la entidad seleccionada. */
    public static void menuGestion(String entidad) {
        System.out.println("\n--- " + entidad + " ---");
        System.out.println("1. Agregar");
        System.out.println("2. Consultar por ID");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("5. Consultar todos");
        System.out.println("6. Regresar");
        System.out.print("Opción: ");
    }

    /** Lee un número entero y vuelve a pedirlo si es inválido. */
    public static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingresa un número entero válido: ");
            }
        }
    }

    /** Lee un entero mayor o igual al mínimo indicado. */
    public static int leerEnteroMinimo(Scanner scanner, String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            int numero = leerEntero(scanner);
            if (numero >= minimo) {
                return numero;
            }
            System.out.println("El valor debe ser al menos " + minimo + ".");
        }
    }

    /** Lee un número decimal no negativo. */
    public static double leerDouble(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double numero = Double.parseDouble(scanner.nextLine().trim());
                if (numero >= 0) {
                    return numero;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Ingresa un número válido.");
        }
    }

    /** Lee texto no vacío y sin comas para no romper el formato CSV. */
    public static String leerTexto(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("El texto no puede estar vacío.");
            } else if (texto.contains(",")) {
                System.out.println("No uses comas porque separan los campos del CSV.");
            } else {
                return texto;
            }
        }
    }

    /**
     * Lee una fecha únicamente comprobando el formato AAAA-MM-DD.
     * No comprueba si la fecha existe realmente.
     */
    public static String leerFecha(Scanner scanner, String mensaje) {
        while (true) {
            String fecha = leerTexto(scanner, mensaje);

            if (fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return fecha;
            }

            System.out.println("Formato inválido. Usa AAAA-MM-DD, por ejemplo 2001-08-25.");
        }
    }

    /** Permite elegir solamente una opción de una lista cerrada. */
    public static String leerOpcion(Scanner scanner, String mensaje, String[] opciones) {
        while (true) {
            System.out.println(mensaje);
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            System.out.print("Opción: ");

            int opcion = leerEntero(scanner);
            if (opcion >= 1 && opcion <= opciones.length) {
                return opciones[opcion - 1];
            }

            System.out.println("Selecciona una opción válida.");
        }
    }

    /**
     * Lee uno o varios correos separados por punto y coma.
     * La validación sencilla solo exige que cada correo contenga '@'.
     */
    public static String leerCorreos(Scanner scanner, String mensaje) {
        while (true) {
            String correos = leerTexto(scanner, mensaje);
            String[] lista = correos.split(";");
            boolean validos = true;

            for (String correo : lista) {
                correo = correo.trim();
                if (correo.isEmpty() || !correo.contains("@")) {
                    validos = false;
                    break;
                }
            }

            if (validos) {
                return correos;
            }

            System.out.println("Cada correo debe contener un '@'.");
        }
    }

    /** Lee uno o varios teléfonos separados por punto y coma; solo acepta dígitos. */
    public static String leerTelefonos(Scanner scanner, String mensaje) {
        while (true) {
            String telefonos = leerTexto(scanner, mensaje);
            String[] lista = telefonos.split(";");
            boolean validos = true;

            for (String telefono : lista) {
                telefono = telefono.trim();
                if (!telefono.matches("\\d+")) {
                    validos = false;
                    break;
                }
            }

            if (validos) {
                return telefonos;
            }

            System.out.println("Los teléfonos deben contener solamente números.");
        }
    }

    /** Lee un número de domicilio; permite N/A cuando no aplica. */
    public static String leerNumeroDomicilio(Scanner scanner, String mensaje, boolean permiteNA) {
        while (true) {
            String numero = leerTexto(scanner, mensaje);

            if (numero.matches("\\d+")) {
                return numero;
            }

            if (permiteNA && numero.equalsIgnoreCase("N/A")) {
                return "N/A";
            }

            System.out.println(permiteNA
                ? "Ingresa solamente números o N/A."
                : "Ingresa solamente números.");
        }
    }

    /**
     * Lee un horario con el formato HH:MM-HH:MM.
     * Solo se valida la forma del texto, no si las horas existen realmente.
     */
    public static String leerHorario(Scanner scanner, String mensaje) {
        while (true) {
            String horario = leerTexto(scanner, mensaje);

            if (horario.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                return horario;
            }

            System.out.println("Formato inválido. Usa HH:MM-HH:MM, por ejemplo 09:00-22:00.");
        }
    }
}