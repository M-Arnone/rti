package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerJs implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Lecture de la requete
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        System.out.print("HandlerHtml (methode " + requestMethod + ") = " + requestPath + " --> ");

        // Ecriture de la reponse
        if (requestPath.endsWith(".js"))
        {

            String temp = System.getProperty("user.dir") + "/src/main/resources";

            String fichier = "/js/code.js" ;
            System.out.println("fichier" + temp+fichier);
            File file = new File(temp + fichier);
            if (file.exists())
            {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type", "text/js");
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
                System.out.println("OK");
            }
            else Erreur404(exchange);
        }
        else Erreur404(exchange);
    }
    private void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "Fichier HTML introuvable !!!";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
}
