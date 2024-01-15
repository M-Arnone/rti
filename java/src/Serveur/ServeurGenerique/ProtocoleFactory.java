package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAP;
import Serveur.ProtocoleVESPAP.VESPAPS;

public class ProtocoleFactory {
    public static Protocole getProtocole(boolean estSecurise, Logger l) {
        if (estSecurise) {
            return new VESPAPS(l);
        } else {
            return new VESPAP(l);
        }
    }
}