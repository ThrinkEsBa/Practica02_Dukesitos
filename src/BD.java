
import java.io.File;

interface BDInterface{

    /**
     * Crea el archivo si es que no existe. Si existe
     * no lo modifica
     * @param name Nombre de archivo
     * @return El objeto tal que se pueda controlar el archivo en las demás consultas
     */
    public File createFile(String name);

    /**
     * Consulta la base de datos escogida
     * @param option El número de archivo a escoger
     * @return El texto del archivo
     */
    public String consult( int option);

    /**
     * Añade el dato al archivo indicado.
     * @param data El dato que se quiere escribir
     * @param option El archivo donde se quiere escribir
     */
    public void add(String data, int option);

    /**
     * Modifica el dato escogido en la posición del id
     * @param id El id del dato a modificar
     * @param data El string con el cual se reemplazará el dato
     * @param option El archivo a escoger
     * @return El antiguo dato.
     */
    public String update(int id, String data, int option);

    /**
     * Elimina el dato de la base de datos escogida
     * @param id El id del dato a remover
     * @param option El archivo a escoger
     * @return El dato removido
     */
    public String delete(int id, int option);


}


public class BD implements BDInterface{
    private String[] options;
    private String BDname;


}
