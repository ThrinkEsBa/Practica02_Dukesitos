
interface MainInterface{
    /**
     * Controlador central de la Base de Datos
     * Imprimirá un menú interactivo para hacer el CRUD de la
     * base de datos y hará que el usuario lo controle.
     * @param base La base de datos a controlar
     * @return Lo que vaya a regresar la base de datos.
     */
    public String controlBD(BD base);

    /**
     * Menú interactivo para añadir datos a la base de datos.
     * Pondrá relaciones si es que las hay entre varios archivos
     * y verificará que los datos estén bien estructurados.
     * @param base La base de datos a usar
     */
    public void createToBD(BD base);

    /**
     * Menú interactivo para controlar la base de datos.
     * Preguntará qué base de datos quieres mostrar
     * @param base La base de datos a usar
     * @return El output del archivo a leer.
     */
    public String readFromBD(BD base);


    /**
     * Menú interactivo para modificar datos de la base de datos.
     * Preguntará qué dato se quiere, y buscará que todos
     * los datos de la relación estén modificados.
     * 
     * @param base La base de datos a usar
     * @return El dato antiguo
     */
    public String updateFromBD(BD base);

    /**
     * Menú interactivo para eliminar datos de la base de datos.
     * Preguntará de qué archivo eliminar, y buscará que
     * en sus relaciones se elimine también el dato.
     * @param base La base de datos a usar
     * @return
     */
    public String deleteFromBD(BD base);

}

public class Main implements MainInterface{
    
}
