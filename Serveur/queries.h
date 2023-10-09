#ifndef QUERIES_H
#define QUERIES_H
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <signal.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>
#include <mysql.h>

enum AuthenticationResult {
    AUTH_SUCCESS,           // Authentification réussie
    AUTH_INCORRECT_PASSWORD, // Mot de passe incorrect
    AUTH_USERNAME_NOT_FOUND  // Nom d'utilisateur introuvable
};

MYSQL * ConnexionBD();
enum AuthenticationResult authenticateUser(const char *, const char *);
void addUser(const char *, const char *);
MYSQL_ROW getArticleById(int articleId);
int updateArticleStock(int id, int newqte);
int getUserIdByUsername(const char *username);
int insererFacture(int idClient, const char* date, int paye);
MYSQL_ROW getFactureByMaxId(); 

#endif