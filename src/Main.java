import java.util.Scanner;

/**
 * Controla el menú del prototipo de PuellaGame.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] entidades = {"Clientes", "Sucursales", "Premios"};

    /**
     * Muestra el menú principal hasta que el usuario decida salir.
     *
     * @param base objeto que maneja los archivos CSV
     */
    public void controlBD(BD base) {
        while (true) {
            VistaConsola.menuPrincipal();
            int opcion = VistaConsola.leerEntero(scanner);

            if (opcion == 4) {
                return;
            }

            if (opcion < 1 || opcion > 3) {
                System.out.println("Opción inválida.");
                continue;
            }

            gestionarEntidad(base, opcion - 1);
        }
    }

    /** Muestra el CRUD de clientes, sucursales o premios. */
    private void gestionarEntidad(BD base, int entidad) {
        while (true) {
            VistaConsola.menuGestion(entidades[entidad]);
            int opcion = VistaConsola.leerEntero(scanner);

            try {
                switch (opcion) {
                    case 1:
                        agregar(base, entidad);
                        break;
                    case 2:
                        consultarPorId(base, entidad);
                        break;
                    case 3:
                        editar(base, entidad);
                        break;
                    case 4:
                        eliminar(base, entidad);
                        break;
                    case 5:
                        consultarTodos(base, entidad);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    /** Agrega un nuevo registro. */
    private void agregar(BD base, int entidad) {
        String datos = capturarDatos(entidad);
        int id = base.create(datos, entidad);
        System.out.println("Registro guardado. ID: " + id);
    }

    /** Consulta un registro por su llave. */
    private void consultarPorId(BD base, int entidad) {
        int id = VistaConsola.leerEnteroMinimo(scanner, "ID a consultar: ", 1);
        String registro = base.readById(id, entidad);

        if (registro == null) {
            System.out.println("No se encontró ese ID.");
        } else {
            System.out.println(base.getHeader(entidad));
            System.out.println(registro);
        }
    }

    /** Edita un registro existente. */
    private void editar(BD base, int entidad) {
        int id = VistaConsola.leerEnteroMinimo(scanner, "ID a editar: ", 1);

        if (base.readById(id, entidad) == null) {
            System.out.println("No se encontró ese ID.");
            return;
        }

        String datos = capturarDatos(entidad);
        if (base.update(id, datos, entidad)) {
            System.out.println("Registro actualizado.");
        }
    }

    /** Elimina un registro existente. */
    private void eliminar(BD base, int entidad) {
        int id = VistaConsola.leerEnteroMinimo(scanner, "ID a eliminar: ", 1);

        if (base.delete(id, entidad)) {
            System.out.println("Registro eliminado.");
        } else {
            System.out.println("No se encontró ese ID.");
        }
    }

    /** Muestra todos los registros de una entidad. */
    private void consultarTodos(BD base, int entidad) {
        String registros = base.read(entidad);

        if (registros.isEmpty()) {
            System.out.println("No hay registros.");
        } else {
            System.out.println(base.getHeader(entidad));
            System.out.println(registros);
        }
    }

    /** Captura los campos de la entidad seleccionada. */
    private String capturarDatos(int entidad) {
        switch (entidad) {
            case 0:
                return capturarCliente();
            case 1:
                return capturarSucursal();
            case 2:
                return capturarPremio();
            default:
                throw new IllegalArgumentException("Entidad inválida.");
        }
    }

    /** Captura los datos de un cliente. */
    private String capturarCliente() {
        String nombre = VistaConsola.leerTexto(scanner, "Nombre: ");
        String paterno = VistaConsola.leerTexto(scanner, "Apellido paterno: ");
        String materno = VistaConsola.leerTexto(scanner, "Apellido materno: ");
        String fecha = VistaConsola.leerFecha(scanner, "Fecha de nacimiento (AAAA-MM-DD): ");
        String sexo = VistaConsola.leerOpcion(
            scanner,
            "Sexo:",
            new String[] {"Masculino", "Femenino", "Indeterminado"}
        );
        String correos = VistaConsola.leerCorreos(
            scanner, "Correo(s), separados por ';': "
        );
        String telefonos = VistaConsola.leerTelefonos(
            scanner, "Teléfono(s), separados por ';': "
        );
        return String.join(",", nombre, paterno, materno, fecha, sexo, correos, telefonos);
    }

    /** Captura los datos de una sucursal. */
    private String capturarSucursal() {
        String nombre = VistaConsola.leerTexto(scanner, "Nombre: ");
        String calle = VistaConsola.leerTexto(scanner, "Calle: ");
        String interior = VistaConsola.leerNumeroDomicilio(
            scanner, "Número interior (N/A si no aplica): ", true
        );
        String exterior = VistaConsola.leerNumeroDomicilio(
            scanner, "Número exterior: ", false
        );
        String colonia = VistaConsola.leerTexto(scanner, "Colonia: ");
        String estado = VistaConsola.leerTexto(scanner, "Estado: ");
        String telefono = VistaConsola.leerTexto(scanner, "Teléfono: ");
        String horario = VistaConsola.leerHorario(
            scanner, "Horario de atención (HH:MM-HH:MM): "
        );

        return String.join(",", nombre, calle, interior, exterior, colonia, estado, telefono, horario);
    }

    /** Captura los datos de un premio. */
    private String capturarPremio() {
        String nombre = VistaConsola.leerTexto(scanner, "Nombre: ");
        String rangoEdad = VistaConsola.leerOpcion(
            scanner,
            "Rango de edad:",
            new String[] {"Infantil", "Juvenil", "Adulto"}
        );
        double valor = VistaConsola.leerDouble(scanner, "Valor comercial: ");
        int puntos = VistaConsola.leerEnteroMinimo(
            scanner, "Puntos necesarios (mínimo 20): ", 20
        );
        String categoria = categoriaPremio(puntos);

        return nombre + "," + categoria + "," + rangoEdad + ","
            + valor + "," + puntos;
    }

    /** Calcula la categoría del premio usando sus puntos. */
    private String categoriaPremio(int puntos) {
        if (puntos <= 1000) {
            return "Bajo";
        } else if (puntos <= 3999) {
            return "Medio";
        } else {
            return "Grande";
        }
    }

    /** Inicia el programa. */
    public static void main(String[] args) {
        try {
            BD base = new BD();
            Main programa = new Main();
            programa.controlBD(base);
        } catch (RuntimeException e) {
            System.out.println("No se pudo iniciar el programa: " + e.getMessage());
        }
    }
}