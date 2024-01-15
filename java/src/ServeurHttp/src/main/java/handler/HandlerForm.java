package handler;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Article;
import model.ConnexionBD;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandlerForm implements HttpHandler {
    private final ConnexionBD con;

    public HandlerForm(ConnexionBD c){
        this.con = c;

    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.print("call HandlerForm");


        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            //on va dans la bd et on prend les infos
            System.out.print("request GET");

            try {
                // Mettre une condition
                ResultSet resultSet = con.executeQuery("SELECT * FROM articles");

                // Construire une liste d'articles à partir des résultats de la requête
                ArrayList<Article> articlesList = new ArrayList<>();
                while (resultSet.next()) {
                    Article article = new Article();
                    article.setId(resultSet.getInt("id"));
                    article.setNom(resultSet.getString("intitule"));
                    article.setPrix((double) resultSet.getFloat("prix"));
                    article.setQuantite(resultSet.getInt("stock"));
                    article.setImg(resultSet.getString("image"));
                    articlesList.add(article);
                }

                // Convertir la liste d'articles en JSON
                Gson gson = new Gson();
                JsonArray jsonArray = gson.toJsonTree(articlesList).getAsJsonArray();

                // Envoi de la réponse JSON au client
                exchange.sendResponseHeaders(200, jsonArray.toString().length());
                OutputStream os = exchange.getResponseBody();
                os.write(jsonArray.toString().getBytes());
                os.close();
            } catch (SQLException e) {
                // Gérer l'exception SQL ici
                System.err.println("Erreur lors de l'exécution de la requête SQL : " + e.getMessage());
                exchange.sendResponseHeaders(500, 0);  // Code 500 pour une erreur interne du serveur
                OutputStream os = exchange.getResponseBody();
                os.close();
            }
        } else {
            // Autre traitement pour les requêtes de méthode différente de GET
        }

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            System.out.print("request POST");
            System.out.print("aaaaaaaaaaaaaaaa");

            // Traitement des requêtes POST
            // Récupérer les données du corps de la requête
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String data = br.readLine();
            System.out.print(data);

            try {
                JSONObject json = new JSONObject(data);

                int id = json.getInt("id");
                double price = json.getDouble("price");
                int quantity = json.getInt("quantity");

                System.out.print("ID : " + id);
                System.out.print("Prix : " + price);
                System.out.print("Quantité : " + quantity);

                String updateQuery = "UPDATE articles SET prix = " + price + ", stock = " + quantity + " WHERE id = " + id + ";";

                // Exécution de la requête SQL
                System.out.print(updateQuery);
                con.executeUpdate(updateQuery);

            } catch (JSONException e) {
                // Gérer l'exception JSON ici
                System.err.println("Erreur lors de la conversion JSON : " + e.getMessage());
                exchange.sendResponseHeaders(400, 0);  // Code 400 pour une mauvaise requête du client
                OutputStream os = exchange.getResponseBody();
                os.close();
            } catch (SQLException e) {
                // Gérer l'exception SQL ici
                System.err.println("Erreur lors de l'exécution de la requête SQL : " + e.getMessage());
                exchange.sendResponseHeaders(500, 0);  // Code 500 pour une erreur interne du serveur
                OutputStream os = exchange.getResponseBody();
                os.close();
            }
        }


            //prend la requete qui arrive du client pour recupe les article


        }
}

