package BD.facture;

import BD.interfaces.Reponse;

public class ReponsePAYFACTURES implements Reponse {
    private boolean valide;
    private String message;

    public ReponsePAYFACTURES(boolean valide) {
        this.valide = valide;
    }

    public ReponsePAYFACTURES(boolean valide, String msg) {
        this.valide = valide;
        this.message = msg;
    }

    public boolean isValide() {
        return valide;
    }

    public String getMessage() { return message; }
}
