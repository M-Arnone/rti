package ClientAchat.model;

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
    private Socket cSocket;
    private String _requete;
    private ConfigProperties cg;

    public String getRequete() {
        return _requete;
    }

    public void setRequete(String _requete) {
        this._requete = _requete;
    }


    public int setLogin(String nom, String pwd,boolean newClient){
        if(newClient)
            setRequete("LOGIN" + nom + "#" + pwd + "#1");
        else setRequete("LOGIN" + nom + "#" + pwd + "#0");


        return 0;
    }


    void Echange(String requete, String reponse)
    {

    }
    public void connectToServer() throws IOException{
        ConfigProperties cg = new ConfigProperties();
         cSocket = new Socket(cg.getServeurIP(),cg.getServeurPort());
         dos = new DataOutputStream(cSocket.getOutputStream());
         dis = new DataInputStream(cSocket.getInputStream());
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
