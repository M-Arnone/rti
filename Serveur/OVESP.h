#ifndef OVESP_H
#define OVESP_H
#define NB_MAX_CLIENTS 100

#include <pthread.h>

bool SMOP(char* requete, char* reponse,int socket);
bool SMOP_Login(const char* user,const char* password);
int SMOP_Operation(char op,int a,int b);
void SMOP_Close();

#endif