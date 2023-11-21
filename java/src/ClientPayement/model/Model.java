package ClientPayement.model;

import ClientPayement.model.ConfigProperties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Model {

    public static volatile Model instance;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket sClient;
    public void connectToServer() throws IOException {
        ConfigProperties cg = new ConfigProperties();
        sClient = new Socket(cg.getServeurIP(),cg.getServeurPort());
        dos = new DataOutputStream(sClient.getOutputStream());
        dis = new DataInputStream(sClient.getInputStream());
    }
    public Model() throws IOException {
        connectToServer();
    }
    public static Model getInstance() throws SQLException, ClassNotFoundException, IOException {
        if(instance == null){
            synchronized (ClientAchat.model.Model.class){
                if(instance == null){
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

    }
}
