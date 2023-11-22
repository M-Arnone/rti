package ClientPayement.view;

import BD.classes.Article;
import BD.classes.Facture;
import ClientPayement.controler.Controler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ClientPayementGUI extends JFrame {

    private JPanel panelPrincipal;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;
    private JButton btnConnexion;
    private JButton btnDeconnexion;
    private JTable factureClient;
    private JTextField textFieldClient;
    private JButton btnVoirFactures;
    private JButton btnPayerFactures;
    private JTable factureDetaille;

    public JTextField getTextFieldClient() {
        return textFieldClient;
    }

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
    public ClientPayementGUI()
    {
        setContentPane(panelPrincipal);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("IdClient");
        model.addColumn("Date");
        model.addColumn("Montant");
        model.addColumn("Payé");
        factureClient.setModel(model);
        pack();
    }

    public static void main(String[] args) {
        ClientPayementGUI cpg = new ClientPayementGUI();
        cpg.setVisible(true);
    }
}
