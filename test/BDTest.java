import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class BDTest {

    private BD bd;
    private final int CLIENTES = 0;
    private final int SUCURSALES = 1;
    private final int PREMIOS = 2;

    @BeforeEach
    public void setUp() {
        bd = new BD();
        bd.createFile("clientes.csv");
        bd.createFile("sucursales.csv");
        bd.createFile("premios.csv");
    }

    @AfterEach
    public void tearDown() {
        // Limpiar entorno eliminando los archivos generados
        new File("clientes.csv").delete();
        new File("sucursales.csv").delete();
        new File("premios.csv").delete();
    }

    @Test
    public void testArchivosEntidadesCreadosCorrectamente() {
        // Esto comprueba que guarde la información en archivos .CSV
        assertTrue(new File("clientes.csv").exists(), "El archivo de clientes debe existir.");
        assertTrue(new File("sucursales.csv").exists(), "El archivo de sucursales debe existir.");
        assertTrue(new File("premios.csv").exists(), "El archivo de premios debe existir.");
    }

    @Test
    public void testCreateAndReadClientes() {
        // Prueba que almacene información de clientes.
        String cliente = "1,Maria Lopez,1500";
        bd.create(cliente, CLIENTES);
        
        String contenido = bd.read(CLIENTES);
        assertTrue(contenido.contains(cliente), "El archivo de clientes debe contener el registro insertado.");
    }

    @Test
    public void testCreateAndReadSucursales() {
        // Almacenar información de sucursales
        String sucursal = "10,Sucursal Centro,CDMX";
        bd.create(sucursal, SUCURSALES);
        
        String contenido = bd.read(SUCURSALES);
        assertTrue(contenido.contains(sucursal), "El archivo de sucursales debe contener el registro insertado.");
    }

    @Test
    public void testCreateAndReadPremios() {
        // Almacenar información de premios
        String premio = "100,Consola de Videojuegos,5000";
        bd.create(premio, PREMIOS);
        
        String contenido = bd.read(PREMIOS);
        assertTrue(contenido.contains(premio), "El archivo de premios debe contener el registro insertado.");
    }

    @Test
    public void testPersistenciaDeDatos() {
        // La información capturada tiene que persistir al reiniciar el sistema. Esto se puede simular creando una nueva instancia de BD y leyendo los archivos.
        bd.create("2,Carlos Rivera,300", CLIENTES);
        
        // Simulamos el cierre y apertura de la aplicación instanciando una nueva BD
        BD nuevaSesionBD = new BD();
        String contenido = nuevaSesionBD.read(CLIENTES);
        
        assertTrue(contenido.contains("2,Carlos Rivera,300"), "Los datos deben persistir entre diferentes instancias del objeto BD.");
    }

    @Test
    public void testUpdateDevuelveDatoAntiguo() {
        // Modifica el dato en la posición del id y retorna el antiguo dato.
        bd.create("1,Maria Lopez,1500", CLIENTES);
        String datoAntiguo = bd.update(1, "1,Maria Lopez,2000", CLIENTES);
        
        assertEquals("1,Maria Lopez,1500", datoAntiguo, "El método update debe retornar el string exacto antes de ser reemplazado.");
        assertTrue(bd.read(CLIENTES).contains("2000"), "El archivo debe reflejar la actualización de los puntos.");
    }

    @Test
    public void testDeleteDevuelveDatoRemovido() {
        // Elimina el dato y retorna el dato removido.
        bd.create("10,Sucursal Centro,CDMX", SUCURSALES);
        String datoRemovido = bd.delete(10, SUCURSALES);
        
        assertEquals("10,Sucursal Centro,CDMX", datoRemovido, "El método delete debe retornar el registro eliminado.");
        assertFalse(bd.read(SUCURSALES).contains("Sucursal Centro"), "El registro ya no debe existir tras la eliminación.");
    }

    @Test
    public void testUpdateLanzaExcepcionSiIdNoExiste() {
        // Es necesario el manejo de excepciones para notificar que algo salió mal.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bd.update(999, "999,Dato Fantasma,0", PREMIOS);
        });
        assertNotNull(exception.getMessage(), "Debe lanzarse una excepción si se intenta actualizar un ID inexistente.");
    }
}