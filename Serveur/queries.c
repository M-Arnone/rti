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

bool ifUserExist(const char *username, const char *password){
    MYSQL *connexion = ConnexionBD();

    // Échapper les caractères spéciaux dans le nom d'utilisateur
    char escaped_username[100];
    mysql_real_escape_string(connexion, escaped_username, username, strlen(username));

    // Échapper les caractères spéciaux dans le mot de passe
    char escaped_password[100];
    mysql_real_escape_string(connexion, escaped_password, password, strlen(password));

    char query[256];
    snprintf(query, sizeof(query), "SELECT * FROM clients WHERE login = '%s' AND password = '%s'", escaped_username, escaped_password);

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

    mysql_free_result(result);
    mysql_close(connexion);
    // Si num_rows est supérieur à zéro, l'utilisateur est authentifié
    return (num_rows > 0) ? true : false;
}