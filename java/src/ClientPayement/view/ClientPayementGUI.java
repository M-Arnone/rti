package ClientPayement.view;

import ClientPayement.controler.Controler;

import javax.swing.*;

public class ClientPayementGUI extends JFrame {

    private JPanel panelPrincipal;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;
    private JButton btnConnexion;
    private JButton btnDeconnexion;
    private JTable factureClient;
    private JTextField textField3;
    private JButton btnVoirFactures;
    private JButton btnPayerFactures;
    private JTable factureDetaille;

    public JButton getBtnConnexion() {
        return btnConnexion;
    }

    public void setBtnConnexion(JButton btnConnexion) {
        this.btnConnexion = btnConnexion;
    }

    public JButton getBtnDeconnexion() {
        return btnDeconnexion;
    }

    public void setBtnDeconnexion(JButton btnDeconnexion) {
        this.btnDeconnexion = btnDeconnexion;
    }

    public JTable getFactureClient() {
        return factureClient;
    }

    public void setFactureClient(JTable factureClient) {
        this.factureClient = factureClient;
    }

    public JButton getBtnVoirFactures() {
        return btnVoirFactures;
    }

    public void setBtnVoirFactures(JButton btnVoirFactures) {
        this.btnVoirFactures = btnVoirFactures;
    }

    public JButton getBtnPayerFactures() {
        return btnPayerFactures;
    }

    public void setBtnPayerFactures(JButton btnPayerFactures) {
        this.btnPayerFactures = btnPayerFactures;
    }

    public JTextField getTextFieldLogin() {
        return textFieldLogin;
    }

    public JTextField getTextFieldPassword() {
        return textFieldPassword;
    }

    public JTable getFactureDetaille() {
        return factureDetaille;
    }

    public void setFactureDetaille(JTable factureDetaille) {
        this.factureDetaille = factureDetaille;
    }

    public void setControler(Controler c){
        getBtnConnexion().addActionListener(c);
        getBtnDeconnexion().addActionListener(c);
        getBtnPayerFactures().addActionListener(c);
        getBtnPayerFactures().addActionListener(c);
        addWindowListener(c);
    }
    public ClientPayementGUI()
    {
        setContentPane(panelPrincipal);
        pack();
    }

    public static void main(String[] args) {
        ClientPayementGUI cpg = new ClientPayementGUI();
        cpg.setVisible(true);
    }
}
