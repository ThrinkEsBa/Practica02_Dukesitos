package src;
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
    public void create(String data, int option);
    public String read(int option);
    public String update(int id, String data, int option);

    /**
     * Elimina el dato de la base de datos escogida
     * @param id El id del dato a remover
     * @param option El archivo a escoger
     * @return El dato removido
     */
    public String delete(int id, int option);
}

public class BD implements BDInterface {
    private final String[] options = {"clientes.csv", "sucursales.csv", "premios.csv"};
    private final String BDname = "data";

    private final int[] ids = {0,0,0};

    public BD() {
        File dir = new File(BDname);
        if (!dir.exists()) dir.mkdirs();

    
        for (String fileName : options) {
            createFile(fileName);
        }
        updateIds();
    }


    private void updateIds() {
        for (int i = 0; i < options.length; i++) {
            File file = getFileByOption(i);
            int maxId = 0; 

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        try {
                            int currentId = Integer.parseInt(parts[0].trim());
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar IDs de " + file.getName(), e);
            }
            ids[i] = maxId;
        }
    }


    private File getFileByOption(int option) {
        if (option < 0 || option >= options.length) {
            throw new IllegalArgumentException("Opción inválida. Debe ser 0..2");
        }
        return new File(BDname, options[option]);
    }

    @Override
    public File createFile(String name) {
        File file = new File(BDname, name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear archivo: " + file.getPath(), e);
        }
        return file;
    }

    @Override
    public void create(String data, int option) {
        File file = getFileByOption(option);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            ids[option]++;
            bw.write(""+ ids[option]+","+data);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al crear registro en " + file.getName(), e);
        }
    }

    @Override
    public String read(int option) {
        File file = getFileByOption(option);
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + file.getName(), e);
        }

        return sb.toString().trim();
    }

    @Override
    public String update(int id, String data, int option) {
        File file = getFileByOption(option);
        StringBuilder contenidoNuevo = new StringBuilder();
        String oldData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 0) continue;

                int currentId;
                try {
                    currentId = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException ex) {
                
                    contenidoNuevo.append(line).append(System.lineSeparator());
                    continue;
                }

                if (currentId == id) {
                    oldData = line;
                    contenidoNuevo.append(parts[0]).append(",").append(data).append(System.lineSeparator());
                } else {
                    contenidoNuevo.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar en " + file.getName(), e);
        }

        if(oldData == null) System.out.println("Dato no encontrado. Nada actualizado");
        else System.out.println("Dato actualizado correctamente.");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write(contenidoNuevo.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar actualización en " + file.getName(), e);
        }

        return oldData;
    }

    @Override
    public String delete(int id, int option) {
        File file = getFileByOption(option);
        StringBuilder contenidoNuevo = new StringBuilder();
        String deletedData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 0) continue;

                int currentId;
                try {
                    currentId = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException ex) {
                
                    contenidoNuevo.append(line).append(System.lineSeparator());
                    continue;
                }

                if (currentId == id) {
                    deletedData = line;
                } else {
                    contenidoNuevo.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar en " + file.getName(), e);
        }

    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write(contenidoNuevo.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar eliminación en " + file.getName(), e);
        }

        updateIds();

        return deletedData;
    }
}