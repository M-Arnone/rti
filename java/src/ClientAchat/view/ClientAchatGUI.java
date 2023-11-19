package ClientAchat.view;

import ClientAchat.controler.Controler;

import javax.swing.*;

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

    private JLabel image;

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
        setContentPane(panelPrincipal);
        pack();
    }

    public static void main(String[] args) {
        ClientAchatGUI cag = new ClientAchatGUI("Accueil");
        cag.setVisible(true);
    }
}
