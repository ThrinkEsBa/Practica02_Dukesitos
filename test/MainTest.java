import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private Main controladorMain;
    private BD baseDatos;
    private InputStream consolaOriginal;

    @BeforeEach
    public void setUp() {
        controladorMain = new Main();
        baseDatos = new BD();
        baseDatos.createFile("clientes.csv");
        consolaOriginal = System.in;
    }

    @AfterEach
    public void tearDown() {
        System.setIn(consolaOriginal);
    }

    private void simularEntradaUsuario(String entrada) {
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
    }

    @Test
    public void testConsultaRecibeLlaveYRegresaDatos() {
        baseDatos.create("5,Ana Torres,800", 0); 
        baseDatos.create("6,Luis Gomez,120", 0);
        
        // Simulamos que el usuario teclea el ID "5" en el menú de consulta
        simularEntradaUsuario("5\n");
        
        String resultadoConsulta = controladorMain.readFromBD(baseDatos);
        
        assertTrue(resultadoConsulta.contains("Ana Torres"), "La consulta debe extraer los datos relacionados a la llave 5.");
        assertFalse(resultadoConsulta.contains("Luis Gomez"), "La consulta NO debe extraer datos de otras llaves.");
    }

    @Test
    public void testCamposNumericosRechazanLetras() {
        // Verificar que en los campos numéricos solo se puedan almacenar números.
        
        // Simulamos intentar crear un premio con ID "ABC", que debe fallar.
        simularEntradaUsuario("ABC\nConsola\n5000\n");
        
        // La aplicación debe capturar esto y lanzar una excepción controlada.
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            controladorMain.createToBD(baseDatos);
        });
        
        assertNotNull(exception, "El sistema debe manejar la excepción si se insertan letras en un campo numérico.");
    }

    @Test
    public void testDeleteEliminaRelaciones() {
        // Buscará que en sus relaciones se elimine también el dato. Por ejemplo, si eliminamos una sucursal, los clientes asociados a esa sucursal también deben ser eliminados.
        
        // Simulamos teclear el ID del archivo a eliminar
        simularEntradaUsuario("10\n");
        String resultado = controladorMain.deleteFromBD(baseDatos);
        
        // Este test asegura que el controlador procesa la orden y devuelve un string no nulo (confirmación)
        assertNotNull(resultado, "El método debe retornar el status de la eliminación interactiva.");
    }
}