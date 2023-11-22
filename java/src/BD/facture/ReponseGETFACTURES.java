package BD.facture;

import BD.classes.Facture;
import BD.interfaces.Reponse;

import java.util.ArrayList;

public class ReponseGETFACTURES implements Reponse {
    private ArrayList<Facture> listeFactures;

    public ReponseGETFACTURES(ArrayList<Facture> listeFactures) {
        this.listeFactures = listeFactures;
    }

    public ArrayList<Facture> getListeFactures() {
        return listeFactures;
    }
}
