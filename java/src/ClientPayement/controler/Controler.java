package ClientPayement.controler;

import ClientPayement.view.ClientPayementGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class Controler extends WindowAdapter implements ActionListener {
    private ClientPayementGUI cpg;
    public Controler(ClientPayementGUI c){cpg = c;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cpg.getBtnConnexion()){
            on_pushBtnLogin();
        }
    }

    private void on_pushBtnLogin(){
        String login = cpg.getTextFieldLogin().getText();
        String pwd = cpg.getTextFieldPassword().getText();
        if(login.isEmpty() || pwd.isEmpty())
            JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
        else{

        }
    }
}


