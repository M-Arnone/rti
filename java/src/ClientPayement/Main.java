package ClientPayement;


import ClientPayement.controler.Controler;
import ClientPayement.model.Model;
import ClientPayement.view.ClientPayementGUI;
import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        try {
            FlatLightLaf.setup();
            Model m = Model.getInstance();
            ClientPayementGUI cpg = new ClientPayementGUI();
            cpg.setVisible(true);
            Controler c = new Controler(cpg);
        }catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
