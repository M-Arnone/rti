package ClientAchat.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ClientAchat extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox nouveauClientCheckBox;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JSpinner spinner1;
    private JButton precedentButton;
    private JButton suivantButton;
    private JButton acheterButton;
    private JTextField textField6;
    private JTable table1;
    private JButton supprimerLArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerLAchatButton;
    private JTextField textField7;

    public ClientAchat(String nom){
        super(nom);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        ClientAchat ca = new ClientAchat("Acceuil");
    }
}
