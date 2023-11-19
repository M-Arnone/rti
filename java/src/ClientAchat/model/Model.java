package ClientAchat.model;

import javax.sound.midi.Receiver;
import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Model  {
    public static volatile Model instance;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket sClient;
    private String _requete;
    private ConfigProperties cg;
    private ArrayList<Article> panier = new ArrayList<>();

    public ArrayList<Article> getPanier() {
        return panier;
    }

    public void addArt(Article A){
        Article addTemp = new Article();
        boolean trouve = false;
        for (int i = 0;( getPanier() != null && getPanier().size() > i ) && trouve == false; i++) {
            if(getPanier().get(i).getId() == A.getId())
            {
                addTemp = getPanier().get(i);
                addTemp.setQuantite(addTemp.getQuantite()+ A.getQuantite());
                trouve = true;
            }
        }

        if (trouve == false)
        {
            getPanier().add(A);
        }

        for (int i = 0; getPanier() != null && getPanier().size() > i ; i++) {
            System.out.println("Panier client " + i + ":" + getPanier().get(i));
        }
    }

    public void setPanier(ArrayList<Article> panier) {
        this.panier = panier;
    }

    public int getNumArticle() {
        return numArticle;
    }

    int numArticle = 1;
    int numClient;


    public String getRequete() {
        return _requete;
    }

    public void setRequete(String _requete) {
        this._requete = _requete;
    }


    public void on_pushLogin(String nom, String pwd,boolean newClient) throws IOException {
        if(newClient)
            setRequete("LOGIN#" + nom + "#" + pwd + "#1");
        else setRequete("LOGIN#" + nom + "#" + pwd + "#0");
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        numClient = Integer.parseInt(mots[2]);
        setArticle(numArticle);
    }
    public void on_pushLogout() throws IOException {
        setRequete("LOGOUT#oui");
        Echange(getRequete());
    }

    public Article on_pushSuivant() throws IOException {
        if(numArticle+1>21){
            JOptionPane.showMessageDialog(null, "Plus d'articles", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        else numArticle++;
        return setArticle(numArticle);
    }

    public Article on_pushPrecedent() throws IOException{
        if(numArticle-1 < 1)
            JOptionPane.showMessageDialog(null, "Plus d'articles", "Erreur", JOptionPane.ERROR_MESSAGE);
        else numArticle--;
        return setArticle(numArticle);
    }

    public void on_pushAcheter(int quantite) throws IOException {
        System.out.println("NUM ARTICLE : " + numArticle);
        System.out.println("Quantite : " + quantite);
        setRequete("ACHAT#" + numArticle +"#" + quantite);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if(mots[1].equals("ok")){
            Article a = new Article(numArticle,mots[2], Double.parseDouble(mots[3]),quantite);
            addArt(a);
        }
        else System.err.println("Erreur d'achat !!");

    }
    public void on_pushSupprimerArticle(int id) throws IOException{
        setRequete("CANCEL#" + id);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if(mots[1].equals("ok"))
        {
            for (Article artPass : getPanier()) {
                if(artPass.getId() == id)
                {
                    getPanier().remove(artPass);
                    return;
                }
            }
        }
        else System.out.println("Erreur de suppression!!!");

    }
    public void on_pushViderPanier() throws IOException{
        setRequete("CANCELALL");
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if(mots[1].equals("ok"))
        {
            getPanier().clear();
            System.out.println("CANCELALL_OK");
        }
    }
    public void on_pushPayer() throws IOException{
        setRequete("CONFIRMER#"+numClient);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if(mots[1].equals("ok"))
        {
            getPanier().clear();
            System.out.println("Confirm_OK");
        }
    }

    private String Echange(String requete) throws IOException {
        // Envoie de la requête
        try {
            Send(requete);
        } catch (IOException e) {
            System.err.println("Erreur de Send : " + e.getMessage());
            sClient.close();
        }

        // Recevoir la réponse
        String reponse;
        try {
            reponse = Receive();
        } catch (IOException e) {
            System.err.println("Erreur de Receive : " + e.getMessage());
            sClient.close();
            throw e;
        }
        return reponse;
    }


    private void Send (String r) throws IOException {
        try {
            r += "#)";
            dos.write(r.getBytes());
            dos.flush();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi des données: " + e.getMessage());
            throw e;
        }
    }
    private String Receive() throws IOException {
        StringBuilder data = new StringBuilder();
        boolean finished = false;
        byte lastByte = 0;

        while (!finished) {
            int byteRead = dis.read();
            if (byteRead == -1) {
                throw new IOException("Erreur de lecture - Connection fermée ou autre erreur.");
            }

            char readChar = (char) byteRead;

            if (lastByte == '#' && readChar == ')') {
                finished = true;
            } else {
                data.append(readChar);
            }

            lastByte = (byte) readChar;
        }

        return data.toString();
    }

    public void connectToServer() throws IOException{
        try{
            ConfigProperties cg = new ConfigProperties();
            sClient = new Socket(cg.getServeurIP(),cg.getServeurPort());
            dos = new DataOutputStream(sClient.getOutputStream());
            dis = new DataInputStream(sClient.getInputStream());
        }catch (IOException e){
            System.out.println("Erreur : " +e);
            throw e;
        }

    }
    public Article setArticle(int num) throws IOException {
        String reponse = null;
        Article a = null;
        setRequete("CONSULT#"+num);
        try{
            reponse=Echange(getRequete());
        }catch (IOException ex){
            System.err.println("Erreur d'Echange - Consult : " + ex.getMessage());
        }
        String[] infos = reponse.split("#");

        if (infos.length >= 7) {

            String nomArticle = infos[3];
            double prix = Double.parseDouble(infos[4]);
            int quantite = Integer.parseInt(infos[5]);
            String nomFichierImage = infos[6];

            a = new Article(nomArticle,prix,quantite,nomFichierImage);

        } else {
            System.err.println("Réponse mal formée");
        }
        return a;

    }
    public static Model getInstance() throws SQLException, ClassNotFoundException, IOException {
        if(instance == null){
            synchronized (Model.class){
                if(instance == null){
                    instance = new Model();
                }
            }
        }
        return instance;
    }
    public Model() throws IOException {
            connectToServer();
    }



    public static void main(String[] args) throws ClassNotFoundException, SQLException {


    }
}
