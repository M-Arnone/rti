#ifndef OVESP_H
#define OVESP_H
#define NB_MAX_CLIENTS 100

#include <pthread.h>
#include "queries.h"

bool SMOP(char* requete, char* reponse,int socket);
enum AuthenticationResult SMOP_Login(const char* user,const char* password);
void SMOP_Signup(const char* user,const char* password);

void SMOP_Close();

#endif