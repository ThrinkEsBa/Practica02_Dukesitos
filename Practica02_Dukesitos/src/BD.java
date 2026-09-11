
import java.io.File;

interface BDInterface{

    public File createFile(String name);

    public String consult( int option);
    public String add(String dato, int option);
    public String remove(int id, int option);
    public String modify(int id, int option);
}


public class BD implements BDInterface{
    private String[] options;


}
