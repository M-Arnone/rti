package ClientPayement;


import ClientPayement.controler.Controler;
import ClientPayement.model.Model;
import ClientPayement.view.ClientPayementGUI;
import ClientPayement.view.choixSecure;
import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        try {
            FlatLightLaf.setup();
            choixSecure cs = new choixSecure();
            Controler c = new Controler(cs);
            cs.setControler(c);
            cs.setVisible(true);
        }catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
