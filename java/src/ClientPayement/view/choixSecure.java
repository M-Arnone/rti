package ClientPayement.view;

import ClientPayement.controler.Controler;

import javax.swing.*;
import java.awt.*;

public class choixSecure extends JFrame{

    private JCheckBox securiseCheckBox;
    private JPanel panel1;
    private JButton validerButton;

    public JButton getValiderButton() {
        return validerButton;
    }

    public JCheckBox getSecuriseCheckBox() {
        return securiseCheckBox;
    }

    public void setControler(Controler c){
        getValiderButton().addActionListener(c);
        addWindowListener(c);
    }

    public choixSecure(){
        super("Clicli");
        initializeComponents();
        setupLayout();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pour fermer l'application correctement
    }

    private void setupLayout() {
        setLayout(new FlowLayout());// ou un autre LayoutManager
        add(validerButton);
        add(securiseCheckBox);
    }

    private void initializeComponents() {
        validerButton = new JButton("Valider");
        securiseCheckBox = new JCheckBox("Sécurisé");
    }
}
