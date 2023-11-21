package ClientPayement;

import ClientPayement.controler.Controler;
import ClientPayement.view.ClientPayementGUI;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        ClientPayementGUI cpg = new ClientPayementGUI();
        cpg.setVisible(true);
        Controler c = new Controler(cpg);
    }
}
