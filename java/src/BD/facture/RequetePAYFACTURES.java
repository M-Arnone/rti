package BD.facture;

import BD.interfaces.Requete;

public class RequetePAYFACTURES implements Requete {
    private int idFacture;
    private String nom;
    private String visa;
    public RequetePAYFACTURES(int idFacture, String nom, String visa) {
        this.idFacture = idFacture;
        this.nom = nom;
        this.visa = visa;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public String getNom() {
        return nom;
    }

    public String getVisa() {
        return visa;
    }
}
