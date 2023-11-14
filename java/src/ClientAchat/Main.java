package ClientAchat;


import ClientAchat.controler.Controler;
import ClientAchat.model.Model;
import ClientAchat.view.ClientAchatGUI;
import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        FlatLightLaf.setup();
        try {
            Model m = Model.getInstance();
            ClientAchatGUI cag = new ClientAchatGUI("Accueil");
            Controler c = new Controler(cag);
            cag.setControler(c);
            cag.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
