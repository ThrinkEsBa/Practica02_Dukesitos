import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

interface BDInterface {

    /**
     * Crea el archivo si es que no existe. Si existe
     * no lo modifica
     * @param name Nombre de archivo
     * @return El objeto tal que se pueda controlar el archivo en las demás consultas
     */
    public File createFile(String name);

    /**
     * Añade el dato al archivo indicado.
     * @param data El dato que se quiere escribir
     * @param option El archivo donde se quiere escribir
     */
    public int create(String data, int option);

    /**
     * Lee todos los registros de una entidad.
     *
     * @param option índice de la entidad
     * @return registros separados por saltos de línea; cadena vacía si no hay datos
     */
    public String read(int option);

    /**
     * Busca un registro usando su ID.
     *
     * @param id ID del registro
     * @param opcion entidad seleccionada
     * @return registro encontrado o null
     */
    String readById(int id, int option);

    /**
     * Edita un registro conservando su ID.
     *
     * @param id ID que se quiere editar
     * @param data nuevos datos
     * @param opcion entidad seleccionada
     * @return true si el registro existía
     */
    public boolean update(int id, String data, int option);

    /**
     * Elimina un registro usando su ID.
     *
     * @param id ID que se quiere eliminar
     * @param opcion entidad seleccionada
     * @return true si el registro existía
     */
    public boolean delete(int id, int option);
}

/**
 * Implementa una capa de persistencia basada en archivos CSV.
 *
 * Cada entidad se almacena en un archivo independiente dentro del directorio
 * data. El primer campo de cada registro es un ID entero autogenerado.
 * Las modificaciones se escriben primero en un archivo temporal y despues se
 * reemplaza el original para reducir el riesgo de corrupción.
 */
public class BD implements BDInterface {

    /** Nombres de los CSV en el mismo orden usado por el menu. */
    private final String[] archivos = {
        "clientes.csv", "sucursales.csv", "premios.csv"
    };

    /** Encabezados derivados del análisis de requerimientos. */
    private final String[] encabezados = {
        "idCliente,nombre,apellidoPaterno,apellidoMaterno,fechaNacimiento,sexo,correos,telefonos",
        "idSucursal,nombre,calle,numeroInterior,numeroExterior,colonia,estado,telefono,horarioAtencion",
        "idPremio,nombre,categoria,rangoEdad,valorComercialAproximado,puntosNecesarios"
    };

    /** Ultimo ID conocido por entidad durante la ejecucion actual. */
    private final int[] ids = {0, 0, 0};

    /**
     * Crea la carpeta y los archivos CSV si no existen.
     */
    public BD() {
        for (int i = 0; i < archivos.length; i++) {
            File archivo = createFile(archivos[i]);
            escribirEncabezadoSiEstaVacio(archivo, encabezados[i]);
        }

        actualizarIds();
    }

    @Override
    public File createFile(String nombre) {
        File archivo = new File(nombre);

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo.");
        }

        return archivo;
    }

    @Override
    public int create(String data, int opcion) {
        File archivo = obtenerArchivo(opcion);
        ids[opcion]++;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(ids[opcion] + "," + data);
            bw.newLine();
            return ids[opcion];
        } catch (IOException e) {
            ids[opcion]--;
            throw new RuntimeException("Error al guardar el registro.", e);
        }
    }

    @Override
    public String read(int opcion) {
        File archivo = obtenerArchivo(opcion);
        StringBuilder resultado = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (obtenerId(linea) != -1) {
                    resultado.append(linea).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo.", e);
        }

        return resultado.toString().trim();
    }

    @Override
    public String readById(int id, int opcion) {
        File archivo = obtenerArchivo(opcion);

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (obtenerId(linea) == id) {
                    return linea;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al consultar el archivo.", e);
        }

        return null;
    }

    @Override
    public boolean update(int id, String data, int opcion) {
        File archivo = obtenerArchivo(opcion);
        StringBuilder nuevoContenido = new StringBuilder();
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (obtenerId(linea) == id) {
                    nuevoContenido.append(id).append(",").append(data);
                    encontrado = true;
                } else {
                    nuevoContenido.append(linea);
                }
                nuevoContenido.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer para actualizar.", e);
        }

        if (!encontrado) {
            return false;
        }

        sobrescribir(archivo, nuevoContenido.toString());
        return true;
    }

    @Override
    public boolean delete(int id, int opcion) {
        File archivo = obtenerArchivo(opcion);
        StringBuilder nuevoContenido = new StringBuilder();
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (obtenerId(linea) == id) {
                    encontrado = true;
                } else {
                    nuevoContenido.append(linea).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer para eliminar.", e);
        }

        if (!encontrado) {
            return false;
        }

        sobrescribir(archivo, nuevoContenido.toString());
        return true;
    }

    /**
     * Devuelve el encabezado del CSV de una entidad.
     *
     * @param opcion entidad seleccionada
     * @return encabezado CSV
     */
    public String getHeader(int opcion) {
        validarOpcion(opcion);
        return encabezados[opcion];
    }

    /** Escribe el encabezado si el archivo está vacío. */
    private void escribirEncabezadoSiEstaVacio(File archivo, String encabezado) {
        if (archivo.length() != 0) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write(encabezado);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el encabezado.", e);
        }
    }
    
    /** Busca el ID más alto guardado en cada archivo. */
    private void actualizarIds() {
        for (int i = 0; i < archivos.length; i++) {
            File archivo = obtenerArchivo(i);
            int maximo = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    int id = obtenerId(linea);
                    if (id > maximo) {
                        maximo = id;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar los IDs.", e);
            }

            ids[i] = maximo;
        }
    }

    /** Obtiene el archivo correspondiente a la entidad indicada. */
    private File obtenerArchivo(int opcion) {
    return new File(archivos[opcion]);
}

    /** Valida que la opción de entidad sea correcta. */
    private void validarOpcion(int opcion) {
        if (opcion < 0 || opcion >= archivos.length) {
            throw new IllegalArgumentException("Opción de archivo inválida.");
        }
    }

    /** Obtiene el ID de una línea CSV; devuelve -1 si no es un registro. */
    private int obtenerId(String linea) {
        try {
            String[] partes = linea.split(",", 2);
            return Integer.parseInt(partes[0].trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /** Sobrescribe un archivo con el contenido recibido. */
    private void sobrescribir(File archivo, String contenido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write(contenido);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar los cambios.", e);
        }
    }
}