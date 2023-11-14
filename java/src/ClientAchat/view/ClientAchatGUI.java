package ClientAchat.view;

import javax.swing.*;

public class ClientAchatGUI extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox nouveauClientCheckBox;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JSpinner spinner1;
    private JButton acheterButton;
    private JButton precedentButton;
    private JButton suivantButton;
    private JTable table1;
    private JButton supprimerLArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerLAchatButton;
    private JTextField textField6;
    private JPanel panel1;

    ClientAchatGUI(String nom){
        super(nom);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        ClientAchatGUI cag = new ClientAchatGUI("Accueil");
    }
}
