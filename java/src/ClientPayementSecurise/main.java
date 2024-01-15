package ClientPayementSecurise;

import ClientPayementSecurise.controler.Controler;
import ClientPayementSecurise.model.Model;
import ClientPayementSecurise.view.ClientPayementSecuriseGUI;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.SQLException;

public class main {
    private static Model m;
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {

        ClientPayementSecuriseGUI cpsg = new ClientPayementSecuriseGUI();
        Controler controller = new Controler(cpsg);
        cpsg.setControler(controller);
        m = Model.getInstance(true);
        cpsg.setVisible(true);
    }
}
