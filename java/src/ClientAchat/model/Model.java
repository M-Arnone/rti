package ClientAchat.model;

import javax.sound.midi.Receiver;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Properties;

public class Model  {
    public static volatile Model instance;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket sClient;
    private String _requete;
    private ConfigProperties cg;

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
        System.out.println("req :" + getRequete());
        Echange(getRequete());
    }
    public void on_pushLogout() throws IOException {
        setRequete("LOGOUT#oui");
        Echange(getRequete());
    }


    private void Echange(String requete) throws IOException {
        // Envoie de la requête
        try {
            Send(requete);
        } catch (IOException e) {
            System.err.println("Erreur de Send : " + e.getMessage());
            sClient.close();
            return;
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
        System.out.println("Reponse :" + reponse);
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
            int byteRead = dis.read(); // Lit un byte
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
        ConfigProperties cg = new ConfigProperties();
         sClient = new Socket(cg.getServeurIP(),cg.getServeurPort());
         dos = new DataOutputStream(sClient.getOutputStream());
         dis = new DataInputStream(sClient.getInputStream());
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
