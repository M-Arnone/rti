package ClientAchat.view;

import ClientAchat.controler.Controler;
import ClientAchat.model.Article;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ClientAchatGUI extends JFrame {


    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JButton getAcheterButton() {
        return acheterButton;
    }

    public JButton getPrecedentButton() {
        return precedentButton;
    }

    public JButton getSuivantButton() {
        return suivantButton;
    }

    public JButton getSupprimerLArticleButton() {
        return supprimerLArticleButton;
    }

    public JButton getViderLePanierButton() {
        return viderLePanierButton;
    }

    public JButton getConfirmerLAchatButton() {
        return confirmerLAchatButton;
    }

    public JTextField getTextFieldNom() {
        return textFieldNom;
    }

    public JTextField getTextFieldMotDePasse() {
        return textFieldMotDePasse;
    }

    private JTextField textFieldNom;
    private JTextField textFieldMotDePasse;
    private JButton loginButton;
    private JButton logoutButton;

    public JCheckBox getNouveauClientCheckBox() {
        return nouveauClientCheckBox;
    }

    private JCheckBox nouveauClientCheckBox;
    private JButton acheterButton;
    private JButton precedentButton;
    private JButton suivantButton;
    private JTable table1;
    private JButton supprimerLArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerLAchatButton;
    private JTextField textField6;
    private JPanel panelPrincipal;
    private JTextField textFieldArticle;
    private JTextField textFieldPrix;
    private JTextField textFieldStock;

    public JTextField getTextFieldArticle() {
        return textFieldArticle;
    }

    public void setTextFieldArticle(String s) {
        this.textFieldArticle.setText(s);
    }

    public JTextField getTextFieldPrix() {
        return textFieldPrix;
    }

    public void setTextFieldPrix(Double s) {
        String text = String.valueOf(s);
        this.textFieldPrix.setText(text);
    }

    public JTextField getTextFieldStock() {
        return textFieldStock;
    }

    public void setTextFieldStock(Integer s) {
        String text = String.valueOf(s);
        this.textFieldStock.setText(text);
    }

    private JSpinner spinnerQuantite;

    public JSpinner getSpinnerQuantite() {
        return spinnerQuantite;
    }

    public void setImage(String s) {
        s = "img/"+s;
        ImageIcon nouvelleImageIcon = new ImageIcon(s);
        image.setIcon(nouvelleImageIcon);
    }
    public void updateTable(ArrayList<Article> panier) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Efface les données existantes

        for (Article article : panier) {
            model.addRow(new Object[]{article.getId(), article.getNom(), article.getPrix(), article.getQuantite()});
        }
    }


    private JLabel image;
    private JScrollPane jScrollTable;

    public void setControler(Controler c){
        getLoginButton().addActionListener(c);
        getLogoutButton().addActionListener(c);
        getAcheterButton().addActionListener(c);
        getPrecedentButton().addActionListener(c);
        getSuivantButton().addActionListener(c);
        getSupprimerLArticleButton().addActionListener(c);
        getViderLePanierButton().addActionListener(c);
        getConfirmerLAchatButton().addActionListener(c);
        addWindowListener(c);
    }

    public ClientAchatGUI(String nom){
        super(nom);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nom");
        model.addColumn("Prix");
        model.addColumn("Quantité");
        table1.setModel(model);

        // Ajout de données de test
        //model.addRow(new Object[]{1, "Article Test 1", 100.0, 10});

        setContentPane(panelPrincipal);

        pack();
    }

    public static void main(String[] args) {
        ClientAchatGUI cag = new ClientAchatGUI("Accueil");
        cag.setVisible(true);
    }
}
