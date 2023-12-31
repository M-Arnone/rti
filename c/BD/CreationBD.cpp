#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <mysql.h>
#include <string.h>

typedef struct
{
  int   id;
  char  intitule[20];
  float prix;
  int   stock;  
  char  image[20];
} ARTICLE;

ARTICLE Elm[] = 
{
  {-1,"carottes",2.16f,9,"carottes.jpg"},
  {-1,"cerises",9.75f,8,"cerises.jpg"},
  {-1,"artichaut",1.62f,15,"artichaut.jpg"},
  {-1,"bananes",2.6f,8,"bananes.jpg"},
  {-1,"champignons",10.25f,4,"champignons.jpg"},
  {-1,"concombre",1.17f,5,"concombre.jpg"},
  {-1,"courgette",1.17f,14,"courgette.jpg"},
  {-1,"haricots",10.82f,7,"haricots.jpg"},
  {-1,"laitue",1.62f,10,"laitue.jpg"},
  {-1,"oranges",3.78f,23,"oranges.jpg"},
  {-1,"oignons",2.12f,4,"oignons.jpg"},
  {-1,"nectarines",10.38f,6,"nectarines.jpg"},
  {-1,"peches",8.48f,11,"peches.jpg"},
  {-1,"poivron",1.29f,13,"poivron.jpg"},
  {-1,"pommes de terre",2.17f,25,"pommesDeTerre.jpg"},
  {-1,"pommes",4.00f,26,"pommes.jpg"},
  {-1,"citrons",4.44f,11,"citrons.jpg"},
  {-1,"ail",1.08f,14,"ail.jpg"},
  {-1,"aubergine",1.62f,17,"aubergine.jpg"},
  {-1,"echalotes",6.48f,13,"echalotes.jpg"},
  {-1,"tomates",5.49f,22,"tomates.jpg"}
};

int main(int argc,char *argv[])
{
  // Connexion a MySql
  printf("Connexion a la BD...\n");
  MYSQL* connexion = mysql_init(NULL);
  mysql_real_connect(connexion,"localhost","root","","PourStudent",0,0,0);

  // Supprimer les contraintes de clé étrangère de la table ventes
  mysql_query(connexion, "ALTER TABLE ventes DROP FOREIGN KEY idFacture");
  mysql_query(connexion, "ALTER TABLE ventes DROP FOREIGN KEY idArticle");

  // Supprimer la table ventes
  mysql_query(connexion, "DROP TABLE ventes;");

  // Répéter le processus pour la table factures
  mysql_query(connexion, "ALTER TABLE factures DROP FOREIGN KEY idClient");
  mysql_query(connexion, "DROP TABLE factures;");

  // Répéter le processus pour la table clients
  mysql_query(connexion, "DROP TABLE clients;");

  // Répéter le processus pour la table articles
  mysql_query(connexion, "DROP TABLE articles;");

  // Creation d'une table UNIX_FINAL
  printf("Creation de la table articles...\n");
  mysql_query(connexion,"create table articles (id INT(4) auto_increment primary key, intitule varchar(20),prix FLOAT(4),stock INT(4),image varchar(20));");

  // Ajout de tuples dans la table UNIX_FINAL
  printf("Ajout de 21 articles la table articles...\n");
  char requete[256];
  for (int i=0 ; i<21 ; i++)
  {
	  sprintf(requete,"insert into articles values (NULL,'%s',%f,%d,'%s');",Elm[i].intitule,Elm[i].prix,Elm[i].stock,Elm[i].image);
	  mysql_query(connexion,requete);
  }

  //clients -- Id, login, password
  printf("Creation de la table clients...\n");

  mysql_query(connexion,"create table clients (id INT(4) auto_increment primary key, login varchar(20),password varchar(20));");

  //ajout des tuples clients
  printf("Ajout des clients...\n");
  mysql_query(connexion, "INSERT INTO clients (login, password) VALUES ('wagner', 'abc123');");
  mysql_query(connexion, "INSERT INTO clients (login, password) VALUES ('charlet', 'xyz456');");
  mysql_query(connexion, "INSERT INTO clients (login, password) VALUES ('w', '1');");

  //employes

  printf("Creation de la table employes...\n");

  mysql_query(connexion,"create table employes (id INT(4) auto_increment primary key, login varchar(20),password varchar(20));");
  printf("Ajout des employes...\n");
  mysql_query(connexion, "INSERT INTO employes (login, password) VALUES ('q', '1');");


  // Création de la table factures
  printf("Création de la table factures...\n");
  mysql_query(connexion, "CREATE TABLE factures (id INT(4) AUTO_INCREMENT PRIMARY KEY, idClient INT(4), date DATE,montant varchar(20), paye BOOLEAN);");

  // Création de la table ventes
  printf("Création de la table ventes...\n");
  mysql_query(connexion, "CREATE TABLE ventes (idFacture INT(4), idArticle INT(4), quantite INT(4));");

  // Ajout de clés étrangères
  printf("Ajout de clés étrangères...\n");
  mysql_query(connexion, "ALTER TABLE factures ADD FOREIGN KEY (idClient) REFERENCES clients(id);");
  mysql_query(connexion, "ALTER TABLE ventes ADD FOREIGN KEY (idFacture) REFERENCES factures(id);");
  mysql_query(connexion, "ALTER TABLE ventes ADD FOREIGN KEY (idArticle) REFERENCES articles(id);");

  // Deconnection de la BD
  mysql_close(connexion);
  exit(0);
}
