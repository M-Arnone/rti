import com.sun.net.httpserver.HttpServer;
import handler.*;
import model.ConnexionBD;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        HttpServer serveur = null;
        ConnexionBD con = null;
        try{
            con = ConnexionBD.getInstance();

            serveur = HttpServer.create(new InetSocketAddress(8080),0);

            serveur.createContext("/",new HandlerHtml());
            serveur.createContext("/FormArticle", new HandlerForm(con));
            serveur.createContext("/js",new HandlerJs());
            serveur.createContext("/style",new HandlerCss());
            serveur.createContext("/images",new HandlerImage());
            serveur.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Démarrage serveur web");

    }






}
