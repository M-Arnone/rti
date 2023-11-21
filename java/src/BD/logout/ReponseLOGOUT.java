package BD.logout;

import BD.interfaces.Reponse;

public class ReponseLOGOUT implements Reponse {
    private boolean valide;
    ReponseLOGOUT(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}
