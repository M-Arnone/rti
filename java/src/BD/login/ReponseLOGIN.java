package BD.login;
import BD.interfaces.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valide;
    String message;
    public ReponseLOGIN(boolean v) {
        valide = v;
    }
    public ReponseLOGIN(boolean v,String m) {
        valide = v;
        message = m;

    }
    public boolean isValide() {
        return valide;
    }
}
