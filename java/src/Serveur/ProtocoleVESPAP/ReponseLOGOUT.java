package Serveur.ProtocoleVESPAP;

import Serveur.ServeurGenerique.Reponse;

public class ReponseLOGOUT implements Reponse {
    private boolean valide;
    ReponseLOGOUT(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}