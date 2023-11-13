#include "OVESP.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <stdbool.h>
#include <sys/socket.h>

#define NBARTICLE 21

//***** Etat du protocole : liste des clients loggés ****************
int clients[NB_MAX_CLIENTS];
int nbClients = 0;
int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);
pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requete et creation de la reponse *************
bool SMOP(char* requete, char* reponse,int socket,ARTICLEPANIER *tabPanierServeur)
{
    
    // ***** Récupération nom de la requete *****************

    char *ptr = strtok(requete,"#");
    // ***** LOGOUT *****************************************
    if (strcmp(ptr,"LOGOUT") == 0)
    {
        printf("\t[THREAD %p] LOGOUT\n",(void*)pthread_self());
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
        return true;
    }

    // ***** LOGIN ******************************************
    if (strcmp(ptr,"LOGIN") == 0){

        char user[50], password[50];
        int newUser;
        strcpy(user,strtok(NULL,"#"));
        strcpy(password,strtok(NULL,"#"));
        newUser = atoi(strtok(NULL,"#"));
        printf("\t[THREAD %p] LOGIN de %s\n",(void*)pthread_self(),user);

        if (estPresent(socket) >= 0){ // client déjà loggé
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
            return true;
        }
        else{
            if (SMOP_Login(user,password) == AUTH_SUCCESS){
                sprintf(reponse,"LOGIN#ok#%d",getUserIdByUsername(user));
                ajoute(socket);
            }
            else if(SMOP_Login(user,password) == AUTH_INCORRECT_PASSWORD)
                        sprintf(reponse,"LOGIN#ko#pwd");
                    else 
                        if(SMOP_Login(user,password) == AUTH_USERNAME_NOT_FOUND){
                            sprintf(reponse,"LOGIN#ko#username");
                            if(newUser == 1){
                                sprintf(reponse,"SIGNUP");
                                SMOP_Signup(user,password);
                            }
                                
                        }
            
            return true;
            
        }
    }
    // ***** CONSULT ******************************************
    if (strcmp(ptr,"CONSULT") == 0){
        int numArticle = atoi(strtok(NULL,"#"));
        MYSQL_ROW tuple = getArticleById(numArticle);
        if(!tuple)
            sprintf(reponse,"CONSULT#ko#-1");
        else sprintf(reponse,"CONSULT#ok#%d#%s#%f#%d#%s",atoi(tuple[0]),tuple[1],atof(tuple[2]),atoi(tuple[3]),tuple[4]);//id,intitule,prix,stock,image
    }
    // ***** ACHAT ******************************************
    if (strcmp(ptr,"ACHAT") == 0){
        int id = atoi(strtok(NULL,"#"));
        int qtDemande = atoi(strtok(NULL,"#"));
        MYSQL_ROW tuple = getArticleById(id);
        if(!tuple)
            sprintf(reponse,"ACHAT#ko#-1");
        else{

            // Extract and print the fields from the tuple
            int articleId = atoi(tuple[0]);
            const char *articleName = tuple[1];
            double articlePrice = atof(tuple[2]);
            int articleStock = atoi(tuple[3]);

            printf("Article ID: %d\n", articleId);
            printf("Article Name: %s\n", articleName);
            printf("Article Price: %.2f\n", articlePrice);
            printf("Article Stock: %d\n", articleStock);

            int qtDispo = atoi(tuple[3]);
            if (qtDemande > qtDispo)
                    strcpy(reponse,"ACHAT#ko#Stock_Insufisant#0");
            else{
                    int newQt = qtDispo - qtDemande;
                    int ret = updateArticleStock(id,newQt);
                    if(ret == 0)
                         strcpy(reponse,"ACHAT#ko#ERREUR_SQL#-1");
                    else{
                            int j = 0;
                            bool ok = true;
                            printf("PASSAGE\n");
                            for (j = 0,ok = true; j < NBARTICLE && ok == true; j++)
                            {
                                printf("id : %d\n",id);
                                printf("atof(tuple[2]) : %f\n",atof(tuple[2]));
                                // des qu'il y a un emplacement vide ou qu'il croise l'id qu'il a
                                if(tabPanierServeur[j].id == 0 || tabPanierServeur[j].id == id)
                                {
                                    ok = false;
                                    tabPanierServeur[j].id = id;
                                    tabPanierServeur[j].prix = articlePrice;
                                    tabPanierServeur[j].quantite = tabPanierServeur[j].quantite + qtDemande;
                                }
                            }
                            printf("f : %f\n",atof(tuple[2]));
                            printf(".2f : %.2f\n",atof(tuple[2]));
                            printf(".2lf : %.2lf\n",atof(tuple[2]));
                            sprintf(reponse,"ACHAT#ok#%s#%f#1",tuple[1],atof(tuple[2]));
                            // strcpy(reponse,"ACHAT#ok");
                            // strcat(reponse,"#");
                            // strcat(reponse,tuple[1]);
                            // strcat(reponse,"#");
                            // strcat(reponse,tuple[2]);
                            // strcat(reponse,"#1");
                    }

            }
                    
        }
    }
    // ***** CANCEL ******************************************
    if (strcmp(ptr,"CANCEL") == 0){
        int id = atoi(strtok(NULL,"#")),qteDispo,newQte,j;
        bool ok;
        int i;
        MYSQL_ROW tuple;

        tuple = getArticleById(id);
        if(!tuple)
            sprintf(reponse,"CANCEL#ko#ERREUR_SQL#-1");
        else{
            qteDispo = atoi(tuple[3]);
            for(i = 0 , ok = true; ok == true && i < NBARTICLE  ; i++)
            {
                if(tabPanierServeur[i].id == id) ok = false;   
            }
            newQte = qteDispo + tabPanierServeur[i-1].quantite;

            int rep = updateArticleStock(id,newQte);
             if(!rep)
                strcpy(reponse,"CANCEL#ko#ERREUR_SQL#-1");
            else{
                for(j = 0 , ok = true; j < NBARTICLE && ok == true ; j++)
                {
                    if(tabPanierServeur[j].id == id)
                    {
                        ok = false;
                        tabPanierServeur[j].id = 0;
                        tabPanierServeur[j].prix = 0;
                        tabPanierServeur[j].quantite = 0;
                        sprintf(reponse,"CANCEL#ok");
                    }
                }
                
            }
             
        }
        
    }
    // ***** CANCELALL ******************************************
    if (strcmp(ptr,"CANCELALL") == 0){
        bool ok;
        int qteDispo,newQte,k,i=0;
        MYSQL_ROW tuple;
        while(tabPanierServeur[i].id != 0){

            int id = tabPanierServeur[i].id;
            tuple = getArticleById(id);
            if(!tuple)
            sprintf(reponse,"CANCELALL#ko#ERREUR_SQL#-1");
            else{

                qteDispo = atoi(tuple[3]);
                for(k = 0 , ok = true; ok == true && k < NBARTICLE  ; k++)
                {
                    if(tabPanierServeur[k].id == id) ok = false;   
                }
                newQte = qteDispo + tabPanierServeur[k-1].quantite;

                int rep = updateArticleStock(id,newQte);
                if(!rep)
                    strcpy(reponse,"CANCELALL#ko#ERREUR_SQL#-1");
                else{
                  
                        
                        tabPanierServeur[i].id = 0;
                        tabPanierServeur[i].prix = 0;
                        tabPanierServeur[i].quantite = 0;
                        sprintf(reponse,"CANCELALL#ok");
                        
                    }
                
            }
             i++;
            }
        

    }
    // ***** CONFIRMER ******************************************
    if(strcmp(ptr,"CONFIRMER") == 0){
        char date[20]; 
        int idClient = atoi(strtok(NULL,"#"));  
        strcpy(date,"2023-10-08"); // Date
        bool paye= false; // Montant
        int idFact = 0;
        bool ok = true;

        MYSQL_ROW tuple;

        int ret = insererFacture(idClient,date,paye);
        if(!ret)
            strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1#1");
        else{
            tuple = getFactureByMaxId();
            if(!tuple)
                 strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1");
            else{
                idFact = atoi(tuple[0]);
                for(int j = 0 ; j < NBARTICLE && ok == true ; j++)
                {
                    if(tabPanierServeur[j].id != 0)
                    {
                        int ret = insererArticleAchete(idFact,tabPanierServeur[j].id,tabPanierServeur[j].quantite);
                        if(!ret){
                            strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1#3");
                            ok = false;
                        }
                        else
                        {
                            printf("\nARTICLE ACHETE\n");
                            tabPanierServeur[j].id = 0;
                            tabPanierServeur[j].prix = 0;
                            tabPanierServeur[j].quantite = 0;
                            sprintf(reponse,"CONFIRMER#ok");
                           
                        }
                    }
                }
                    
            }
        }


    }
    return true;
}

//***** Traitement des requetes *************************************
enum AuthenticationResult SMOP_Login(const char* user,const char* password)
{
    enum AuthenticationResult result =  authenticateUser(user,password);
    switch (result) {
        case AUTH_SUCCESS:
            printf("Authentification réussie\n");
            break;
        case AUTH_INCORRECT_PASSWORD:
            printf("Mot de passe incorrect\n");
            break;
        case AUTH_USERNAME_NOT_FOUND:
            printf("Nom d'utilisateur introuvable\n");
            break;
    }
    return result;
}

void SMOP_Signup(const char* user,const char* password){
    addUser(user,password);
}


//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
    int indice = -1;
    pthread_mutex_lock(&mutexClients);
    for(int i=0 ; i<nbClients ; i++)
        if (clients[i] == socket){ 
            indice = i; break; 
        }
    pthread_mutex_unlock(&mutexClients);
    return indice;
}
void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
    clients[nbClients] = socket;
    nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
    int pos = estPresent(socket);
    if (pos == -1) return;
    pthread_mutex_lock(&mutexClients);
    for (int i=pos ; i<=nbClients-2 ; i++)
        clients[i] = clients[i+1];
    nbClients--;
    pthread_mutex_unlock(&mutexClients);
}

//***** Fin prématurée **********************************************
void SMOP_Close()
{
    pthread_mutex_lock(&mutexClients);
    for (int i=0 ; i<nbClients ; i++)
        shutdown(clients[i],SHUT_RDWR);
    pthread_mutex_unlock(&mutexClients);
}