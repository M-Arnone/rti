package ClientAchat.controler;

import ClientAchat.model.Article;
import ClientAchat.model.Model;
import ClientAchat.view.ClientAchatGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controler extends WindowAdapter implements ActionListener {
    Model m = Model.getInstance();
    private ClientAchatGUI _cag = new ClientAchatGUI("Acceuil");
    public Controler(ClientAchatGUI cag) throws SQLException, IOException, ClassNotFoundException {_cag = cag;}
    public Controler() throws SQLException, IOException, ClassNotFoundException {}
    @Override
    public void actionPerformed(ActionEvent e) {

        //LOGIN
        if(e.getSource() == _cag.getLoginButton()){
            try {
                String nom = _cag.getTextFieldNom().getText();
                String pwd = _cag.getTextFieldMotDePasse().getText();
                if(nom.isEmpty() || pwd.isEmpty())
                    JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                else{
                    boolean newClient = false;
                    if(_cag.getNouveauClientCheckBox().isSelected())
                        newClient = true;
                    m.on_pushLogin(nom,pwd,newClient);
                    Article a;
                    if((a = m.setArticle(1)) == null ){
                        System.out.println("ERREUR");
                    }
                    _cag.setImage(a.getImg());
                    _cag.setTextFieldArticle(a.getNom());
                    _cag.setTextFieldPrix(a.getPrix());
                    _cag.setTextFieldStock(a.getQuantite());
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //LOGOUT
        if(e.getSource() == _cag.getLogoutButton()){
            try {
                m.on_pushLogout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //PRECEDENT
        if(e.getSource() == _cag.getPrecedentButton())
        {
            try {
                Article a = m.on_pushPrecedent();
                _cag.setImage(a.getImg());
                _cag.setTextFieldArticle(a.getNom());
                _cag.setTextFieldPrix(a.getPrix());
                _cag.setTextFieldStock(a.getQuantite());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //SUIVANT
        if(e.getSource() == _cag.getSuivantButton())
        {
            System.out.println("SUIVANT");
            try {
               Article a = m.on_pushSuivant();
                _cag.setImage(a.getImg());
                _cag.setTextFieldArticle(a.getNom());
                _cag.setTextFieldPrix(a.getPrix());
                _cag.setTextFieldStock(a.getQuantite());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        //ACHETER
        if(e.getSource() == _cag.getAcheterButton())
        {
            int qte = (int)_cag.getSpinnerQuantite().getValue();
            if(qte >0){
                try {
                    m.on_pushAcheter(qte);
                    majCurrentArticle();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else JOptionPane.showMessageDialog(null, "La quantite doit etre renseignée", "Erreur", JOptionPane.ERROR_MESSAGE);

        }
        //SUPPRIMER
        if(e.getSource() == _cag.getSupprimerLArticleButton())
        {
            int row = _cag.getTable1().getSelectedRow();
            if(row == -1) JOptionPane.showMessageDialog(null, "La quantite doit etre renseignée", "Erreur", JOptionPane.ERROR_MESSAGE);
            else{
                try {
                    DefaultTableModel model = (DefaultTableModel) _cag.getTable1().getModel();
                    int id = (int) model.getValueAt(row, 0);
                    m.on_pushSupprimerArticle(id);
                    _cag.updateTable(m.getPanier());
                    majCurrentArticle();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        //VIDER PANIER
        if(e.getSource() == _cag.getViderLePanierButton())
        {
            try {
                m.on_pushViderPanier();
                majCurrentArticle();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //PAYER
        if(e.getSource() == _cag.getConfirmerLAchatButton())
        {
            try {
                m.on_pushPayer();
                majCurrentArticle();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

       _cag.revalidate();
       _cag.repaint();
    }
    private void majCurrentArticle() throws IOException {
        Article a = m.setArticle(m.getNumArticle());
        _cag.setImage(a.getImg());
        _cag.setTextFieldArticle(a.getNom());
        _cag.setTextFieldPrix(a.getPrix());
        _cag.setTextFieldStock(a.getQuantite());
        _cag.updateTable(m.getPanier());
    }
    @Override
    public void windowClosing(WindowEvent e) {

    }
}
