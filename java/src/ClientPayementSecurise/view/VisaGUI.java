package ClientPayementSecurise.view;

import ClientPayementSecurise.controler.Controler;

import javax.swing.*;

public class VisaGUI extends JFrame{
    private JPanel panelPrincipal;
    private JTextField textFieldVisa;
    private JButton confirmerButton;
    private JButton annulerButton;
    private JTextField textFieldTitulaire;
    public JTextField getTextFieldTitulaire() {
        return textFieldTitulaire;
    }

    public JTextField getTextFieldVisa() {
        return textFieldVisa;
    }

    public JButton getConfirmerButton() {
        return confirmerButton;
    }

    public JButton getAnnulerButton() {
        return annulerButton;
    }
    public void setControler(Controler c){
        getConfirmerButton().addActionListener(c);
        getAnnulerButton().addActionListener(c);
        addWindowListener(c);
    }
    public VisaGUI(){
        setContentPane(panelPrincipal);
        pack();
    }
}
