#include "queries.h"

MYSQL* ConnexionBD()
{
    MYSQL* connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0) == NULL)
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
    snprintf(query, sizeof(query), "SELECT * FROM clients WHERE login = '%s'", escaped_username);

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
    snprintf(query, sizeof(query), "INSERT INTO clients (login, password) VALUES ('%s', '%s')", username, password);

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'insertion de l'utilisateur : %s\n", mysql_error(connexion));
    } else {
        printf("Utilisateur ajouté avec succès.\n");
    }

    mysql_close(connexion);
}

MYSQL_ROW getArticleById(int articleId) {
    MYSQL *connexion = ConnexionBD();

    char query[256];
    snprintf(query, sizeof(query), "SELECT * FROM articles WHERE id = %d", articleId);

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

    mysql_free_result(result);
    mysql_close(connexion);

    return row;
}

int updateArticleStock(int id, int newqte) {
    MYSQL *connexion = ConnexionBD();

    char requete[256];
    snprintf(requete, sizeof(requete), "UPDATE articles SET stock = %d WHERE id = %d", newqte, id);

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
    snprintf(query, sizeof(query), "SELECT id FROM clients WHERE login = '%s'", escaped_username);

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
int insererFacture(int idClient, const char* date, int paye) {
    MYSQL* connexion = ConnexionBD();

    // Échapper les caractères spéciaux dans la date
    char escaped_date[20];
    mysql_real_escape_string(connexion, escaped_date, date, strlen(date));

    char query[256];
    snprintf(query, sizeof(query), "INSERT INTO factures (idClient, date, paye) VALUES (%d, '%s', %d)", idClient, escaped_date, paye);

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

    char query[256];
    snprintf(query, sizeof(query), "SELECT * FROM factures WHERE idfacture = (SELECT MAX(idfacture) FROM factures)");

    if (mysql_query(connexion, query) != 0) {
        fprintf(stderr, "Échec de l'exécution de la requête : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    MYSQL_RES* result = mysql_store_result(connexion);
    if (result == NULL) {
        fprintf(stderr, "Échec de la récupération du résultat : %s\n", mysql_error(connexion));
        mysql_close(connexion);
        exit(1);
    }

    int num_rows = mysql_num_rows(result);
    if (num_rows == 0) {
        // Aucune facture trouvée, renvoyer une erreur
        mysql_free_result(result);
        mysql_close(connexion);
        fprintf(stderr, "Aucune facture trouvée.\n");
        return NULL; // Vous pouvez choisir de gérer l'erreur de manière différente si nécessaire
    }

    // Récupérer les données de la facture
    MYSQL_ROW row = mysql_fetch_row(result);

    mysql_free_result(result);
    mysql_close(connexion);

    return row;
}

