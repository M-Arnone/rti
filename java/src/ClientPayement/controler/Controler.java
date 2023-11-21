package ClientPayement.controler;

import ClientPayement.view.ClientPayementGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class Controler extends WindowAdapter implements ActionListener {
    private ClientPayementGUI cpg;
    public Controler(ClientPayementGUI c){cpg = c;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cpg.getBtnConnexion()){
            //connexion
        }
    }
}
