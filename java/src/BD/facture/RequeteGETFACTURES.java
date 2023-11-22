package BD.facture;

import BD.interfaces.Requete;

public class RequeteGETFACTURES implements Requete {
    private int idClient;

    public RequeteGETFACTURES(int id) {
        this.idClient = id;
    }

    public int getIdClient() {
        return idClient;
    }
}
