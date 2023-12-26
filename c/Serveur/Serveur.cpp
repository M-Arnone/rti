#include "Tcp.h"
#include "OVESP.h"


void HandlerSIGINT(int s);
void TraitementConnexion(int sService);
void* FctThread(void* p);
int sEcoute;
// Gestion du pool de threads
//#define NB_THREADS_POOL 2
//#define TAILLE_FILE_ATTENTE 20
//#define PORT_ACHAT 1234
#define NBARTICLE 21
std::map<std::string, int> config = loadConfig("Serveur/config.txt");
std::vector<int> socketsAcceptees(config["TAILLE_FILE_ATTENTE"]);

int indiceEcriture=0, indiceLecture=0;
pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;



int NB_THREADS_POOL = config["NB_THREADS_POOL"];
int PORT_ACHAT = config["PORT_ACHAT"];
int TAILLE_FILE_ATTENTE = config["TAILLE_FILE_ATTENTE"];





int main(){
    // Initialisation socketsAcceptees
    pthread_mutex_init(&mutexSocketsAcceptees,NULL);
    pthread_cond_init(&condSocketsAcceptees,NULL);
    for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++)
        socketsAcceptees[i] = -1;

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    if (sigaction(SIGINT,&A,NULL) == -1){
        perror("Erreur de sigaction");
        exit(1);
    }

    // Creation de la socket d'écoute
    if ((sEcoute = ServerSocket(PORT_ACHAT))== -1){
        perror("Erreur de ServeurSocket");
        exit(1);
    }

    // Creation du pool de threads
    printf("Création du pool de threads.\n");
    pthread_t th;
    for (int i=0 ; i<NB_THREADS_POOL ; i++)
        pthread_create(&th,NULL,FctThread,NULL);



    // Mise en boucle du serveur
    int sService;
    char ipClient[50];
    printf("Demarrage du serveur.\n");
    while(1)
    {
        printf("Attente d'une connexion...\n");
        if ((sService = Accept(sEcoute,ipClient)) == -1){
            perror("Erreur de Accept");
            close(sEcoute);
            SMOP_Close();
            exit(1);
        }
        printf("Connexion acceptée : IP=%s socket=%d\n",ipClient,sService);

        // Insertion en liste d'attente et réveil d'un thread du pool
        // (Production d'une tâche)
        pthread_mutex_lock(&mutexSocketsAcceptees);
        socketsAcceptees[indiceEcriture] = sService; // !!!
        indiceEcriture++;
        if (indiceEcriture == TAILLE_FILE_ATTENTE) indiceEcriture = 0;
        pthread_mutex_unlock(&mutexSocketsAcceptees);
        pthread_cond_signal(&condSocketsAcceptees);
    }
}


void* FctThread(void* p)
{
    int sService;

    while(1)
    {


        printf("\t[THREAD %p] Attente socket...\n",(void*)pthread_self());

        // Attente d'une tâche
        pthread_mutex_lock(&mutexSocketsAcceptees);
        while (indiceEcriture == indiceLecture)
            pthread_cond_wait(&condSocketsAcceptees,&mutexSocketsAcceptees);

        sService = socketsAcceptees[indiceLecture];
        socketsAcceptees[indiceLecture] = -1;
        indiceLecture++;
        if (indiceLecture == TAILLE_FILE_ATTENTE) indiceLecture = 0;
        pthread_mutex_unlock(&mutexSocketsAcceptees);
        // Traitement de la connexion (consommation de la tâche)

        printf("\t[THREAD %p] Je m'occupe de la socket %d\n",(void*)pthread_self(),sService);

        TraitementConnexion(sService);
    }
}

void TraitementConnexion(int sService)
{
    char requete[200], reponse[200];
    int nbLus, nbEcrits;
    bool onContinue = true;

    ARTICLEPANIER tabPanierServeur[NBARTICLE];
    //mets tous les id a 0
    for (int i = 0; i < NBARTICLE; i++)
    {
        tabPanierServeur[i].id = 0;
        tabPanierServeur[i].prix = 0;
        tabPanierServeur[i].quantite = 0;
    }
    while (onContinue){

        printf("\t[THREAD %p] Attente requete...\n",(void*)pthread_self());
        // ***** Reception Requete ******************
        if ((nbLus = Receive(sService,requete)) < 0){
            perror("Erreur de Receive");
            close(sService);
            HandlerSIGINT(0);
        }
        // ***** Fin de connexion ? *****************
        if (nbLus == 0){
            printf("\t[THREAD %p] Fin de connexion du client.\n",(void*)pthread_self());
            close(sService);
            return;
        }
        requete[nbLus] = 0;
        printf("\t[THREAD %p] Requete recue = %s\n",(void*)pthread_self(),requete);
        // ***** Traitement de la requete ***********
        onContinue = SMOP(requete,reponse,sService,tabPanierServeur);
        // ***** Envoi de la reponse ****************
        if ((nbEcrits = Send(sService,reponse,strlen(reponse))) < 0){
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }
        printf("\t[THREAD %p] Reponse envoyee = %s\n",(void*)pthread_self(),reponse);
        printf("\t[THREAD %ld] - Mon panier :\n",pthread_self());
        for(int k = 0 ; k < NBARTICLE ; k++)
        {
            printf("\tid : %d  - qt :  %d\n",tabPanierServeur[k].id, tabPanierServeur[k].quantite);
        }
        printf("\n");
        if (!onContinue)
            printf("\t[THREAD %p] Fin de connexion de la socket%d\n",(void*)pthread_self(),sService);
    }
}

void HandlerSIGINT(int s)
{
    printf("\nArret du serveur.\n");
    close(sEcoute);
    pthread_mutex_lock(&mutexSocketsAcceptees);
    for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++)
        if (socketsAcceptees[i] != -1) close(socketsAcceptees[i]);
    pthread_mutex_unlock(&mutexSocketsAcceptees);
    SMOP_Close();
    exit(0);
}