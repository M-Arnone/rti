package ClientPayementSecurise.view;

import BD.classes.Facture;
import ClientPayementSecurise.controler.Controler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ClientPayementSecuriseGUI extends JFrame {
    private JPanel panelPrincipale;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;

    public JTextField getTextFieldLogin() {
        return textFieldLogin;
    }

    public JTextField getTextFieldPassword() {
        return textFieldPassword;
    }

    private JButton btnConnexion;
    private JButton btnDeconnexion;
    private JTable factureClient;
    private JTextField textFieldClient;
    private JButton btnVoirFactures;

    public JTextField getTextFieldClient() {
        return textFieldClient;
    }
    public JTable getFactureClient() {
        return factureClient;
    }

    private JButton btnPayerFactures;
    private JTable factureDetaille;

    public JButton getBtnConnexion() {
        return btnConnexion;
    }

    public JButton getBtnDeconnexion() {
        return btnDeconnexion;
    }

    public JButton getBtnPayerFactures() {
        return btnPayerFactures;
    }

    public JButton getBtnVoirFactures() {
        return btnVoirFactures;
    }

    public ClientPayementSecuriseGUI()
    {
        setContentPane(panelPrincipale);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("IdClient");
        model.addColumn("Date");
        model.addColumn("Montant");
        model.addColumn("Payé");
        factureClient.setModel(model);
        getBtnDeconnexion().setEnabled(false);
        getBtnVoirFactures().setEnabled(false);
        getBtnPayerFactures().setEnabled(false);
        pack();
    }
    public void setControler(Controler c){
        getBtnConnexion().addActionListener(c);
        getBtnDeconnexion().addActionListener(c);
        getBtnVoirFactures().addActionListener(c);
        getBtnPayerFactures().addActionListener(c);
        addWindowListener(c);
    }
    public void updateFactures(ArrayList<Facture> listfacture)
    {
        DefaultTableModel model = (DefaultTableModel) factureClient.getModel();
        model.setRowCount(0);
        for (Facture facture : listfacture) {
            model.addRow(new Object[]{facture.getId(),facture.getIdClient(), facture.getDate(), facture.getMontant(),facture.getPaye()});
        }
    }
    public static void main(String[] args) {
        ClientPayementSecuriseGUI cpsg = new ClientPayementSecuriseGUI();
        cpsg.setVisible(true);
    }
}
