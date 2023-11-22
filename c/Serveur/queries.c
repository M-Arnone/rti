#include "queries.h"

MYSQL* ConnexionBD()
{
    MYSQL* connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,"0.0.0.0","root","","PourStudent",0,0,0) == NULL)
    {
        fprintf(stderr,"(ACCESBD) Erreur de connexion à la base de données...\n");
        exit(1);  
    }

    return connexion;
}

enum AuthenticationResult authenticateUser(const char *username, const char *password){
    MYSQL *connexion = ConnexionBD();

    printf("\n\n\nNOM : %s\n",username);
    printf("PASSWORD : %s\n",password);

    // Échapper les caractères spéciaux dans le nom d'utilisateur
    char escaped_username[100];
    mysql_real_escape_string(connexion, escaped_username, username, strlen(username));

    
    char query[256];
    sprintf(query, "SELECT * FROM clients WHERE login = '%s'", escaped_username);

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'exécution de la requête : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    MYSQL_RES *result = mysql_store_result(connexion);
    if (result == NULL) {
        fprintf(stderr, "Échec de la récupération du résultat : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    int num_rows = mysql_num_rows(result);
    if (num_rows == 0) {
        mysql_free_result(result);
        mysql_close(connexion);
        return AUTH_USERNAME_NOT_FOUND; // Username incorrect
    }

    // Maintenant, nous avons trouvé un nom d'utilisateur correspondant.
    // Vérifions le mot de passe.
    MYSQL_ROW row = mysql_fetch_row(result);
    char *stored_password = row[2]; // row[2] pour aller a la colonne 'password'

    if (strcmp(password, stored_password) == 0) {
        // Le mot de passe correspond, authentification réussie
        mysql_free_result(result);
        mysql_close(connexion);
        return AUTH_SUCCESS;
    } else {
        // Le mot de passe ne correspond pas
        mysql_free_result(result);
        mysql_close(connexion);
        return AUTH_INCORRECT_PASSWORD;
    }
}

void addUser(const char *username, const char *password) {
    MYSQL *connexion = ConnexionBD(); // Assurez-vous que ConnexionBD() est défini dans votre code

    char query[256];
    sprintf(query, "INSERT INTO clients (login, password) VALUES ('%s', '%s')", username, password);

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'insertion de l'utilisateur : %s\n", mysql_error(connexion));
    } else {
        printf("Utilisateur ajouté avec succès.\n");
    }

    mysql_close(connexion);
}

Article* getArticleById(int articleId) {
    MYSQL *connexion = ConnexionBD();

    char query[256];
    sprintf(query,"SELECT * FROM articles WHERE id = %d", articleId);
    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'exécution de la requête : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    MYSQL_RES *result = mysql_store_result(connexion);
    if (result == NULL) {
        fprintf(stderr, "Échec de la récupération du résultat : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    int num_rows = mysql_num_rows(result);
    if (num_rows == 0) {
        // L'article n'a pas été trouvé, renvoyer une erreur
        mysql_free_result(result);
        mysql_close(connexion);
        fprintf(stderr, "L'article avec l'ID %d n'a pas été trouvé.\n", articleId);
        return 0; // Vous pouvez choisir de gérer l'erreur de manière différente si nécessaire
    }

    // Récupérer les données de l'article
    MYSQL_ROW row = mysql_fetch_row(result);
 // Créer une nouvelle instance de Article
  Article *article = (Article *)malloc(sizeof(Article));
    if (article == NULL) {
        // Gestion de l'erreur d'allocation mémoire
        mysql_free_result(result);
        mysql_close(connexion);
        return NULL;
    }

    // Copier les données
    article->id = atoi(row[0]);
    article->intitule = strdup(row[1]); // Dupliquer la chaîne
    article->prix = atof(row[2]);
    article->stock = atoi(row[3]);
    article->img = strdup(row[4]);
    mysql_free_result(result);
    mysql_close(connexion);

    return article;
}

int updateArticleStock(int id, int newqte) {
    MYSQL *connexion = ConnexionBD();
    char requete[256];
    sprintf(requete, "UPDATE articles SET stock = %d WHERE id = %d", newqte, id);
    if (mysql_query(connexion, requete) != 0) {
        fprintf(stderr, "Échec de la mise à jour de la quantité : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return 0; // Indique une erreur
    } else {
        mysql_close(connexion);
        return 1; // Indique le succès
    }
}

int getUserIdByUsername(const char *username) {
    MYSQL *connexion = ConnexionBD();

    // Échapper les caractères spéciaux dans le nom d'utilisateur
    char escaped_username[100];
    mysql_real_escape_string(connexion, escaped_username, username, strlen(username));

    char query[256];
    sprintf(query, "SELECT id FROM clients WHERE login = '%s'", escaped_username);

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'exécution de la requête : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return -1; // Indique une erreur
    }

    MYSQL_RES *result = mysql_store_result(connexion);
    if (result == NULL) {
        fprintf(stderr, "Échec de la récupération du résultat : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return -1; // Indique une erreur
    }

    int num_rows = mysql_num_rows(result);
    if (num_rows == 0) {
        // L'utilisateur n'a pas été trouvé, renvoyer une erreur
        mysql_free_result(result);
        mysql_close(connexion);
        fprintf(stderr, "L'utilisateur avec le nom '%s' n'a pas été trouvé.\n", username);
        return -1; // Indique une erreur
    }

    // Récupérer l'ID de l'utilisateur
    MYSQL_ROW row = mysql_fetch_row(result);
    int userId = atoi(row[0]); // Convertir la valeur en entier

    mysql_free_result(result);
    mysql_close(connexion);

    return userId;
}
int insererFacture(int idClient,const char* date,float montant, int paye) {
    MYSQL* connexion = ConnexionBD();

    // Échapper les caractères spéciaux dans la date
    char escaped_date[20];
    mysql_real_escape_string(connexion, escaped_date, date, strlen(date));

    char query[256];
    sprintf(query, "INSERT INTO factures (idClient, date,montant, paye) VALUES (%d, '%s',%.2f, %d)", idClient, escaped_date,montant, paye);

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'insertion de la facture : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return 0; // Retourne 0 en cas d'erreur
    } else {
        printf("Facture ajoutée avec succès.\n");
        mysql_close(connexion);
        return 1; // Retourne 1 en cas de succès
    }
}

MYSQL_ROW getFactureByMaxId() {
    MYSQL* connexion = ConnexionBD();

    char chaine[256];
    MYSQL_RES* resultat;
    MYSQL_ROW Tuple;
    int idFact = -1; // Par défaut, si aucune facture n'est trouvée, idFact reste à -1

    sprintf(chaine, "SELECT MAX(id) FROM factures;"); // Utilisez 'id' au lieu de 'idfacture'

    if (mysql_query(connexion, chaine) != 0) {
        fprintf(stderr, "Échec de l'exécution de la requête : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return NULL;
    }

    resultat = mysql_store_result(connexion);
    if (resultat == NULL) {
        fprintf(stderr, "Échec de la récupération du résultat : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return NULL;
    }

    Tuple = mysql_fetch_row(resultat);

    if (Tuple != NULL) {
        idFact = atoi(Tuple[0]);
    }

    mysql_free_result(resultat);
    mysql_close(connexion);

    if (idFact == -1) {
        return NULL; // Aucune facture trouvée
    } else {
        MYSQL_ROW row = (MYSQL_ROW)malloc(sizeof(MYSQL_ROW));
        row[0] = (char*)malloc(10 * sizeof(char)); // Vous pouvez ajuster la taille selon vos besoins
        sprintf(row[0], "%d", idFact);
        return row;
    }
}


int insererArticleAchete(int idfacture,int idarticle, int quantite) {
    MYSQL* connexion = ConnexionBD();

    char query[256];
    //snprintf(query, sizeof(query), "INSERT INTO articlesachetes (idarticle, prix, stock, idfacture) VALUES (%d, '%.2f', %d, %d)", idarticle, prix, stock, idfacture);
    sprintf(query, "INSERT INTO ventes (idFacture, idArticle, quantite) VALUES (%d, %d, %d)", idfacture, idarticle,quantite);


    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'insertion de l'article acheté : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        return 0; // Retourne 0 en cas d'erreur
    } else {
        printf("Article acheté ajouté avec succès.\n");
        mysql_close(connexion);
        return 1; // Retourne 1 en cas de succès
    }
}