package ClientPayement.controler;

import ClientPayement.model.Model;
import ClientPayement.view.ClientPayementGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.sql.SQLException;

public class Controler extends WindowAdapter implements ActionListener {
    private ClientPayementGUI cpg;
    public Controler(ClientPayementGUI c){cpg = c;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cpg.getBtnConnexion()){
            try {
                on_pushBtnLogin();
            } catch (SQLException | IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void on_pushBtnLogin() throws SQLException, IOException, ClassNotFoundException {
        String login = cpg.getTextFieldLogin().getText();
        String pwd = cpg.getTextFieldPassword().getText();
        if(login.isEmpty() || pwd.isEmpty())
            JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
        else{
            Model m = Model.getInstance();
            if(m.login(login,pwd))
                cpg.getBtnConnexion().setEnabled(false);
            else  JOptionPane.showMessageDialog(null, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}


