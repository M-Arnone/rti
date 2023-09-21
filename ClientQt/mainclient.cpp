#include "windowclient.h"
#include "Tcp.h"

#include <QApplication>

WindowClient *w;

int main(int argc, char *argv[])
{
    /*QApplication a(argc, argv);
    w = new WindowClient();
    w->show();
    return a.exec();*/
    int sClient = ClientSocket(NULL,5678);

    // ***** Envoi de texte pur ***************************************
    char texte[80];
    strcpy(texte,"Bonjour, comment vas-tu ?");
    
    int nbEcrits;
    if ((nbEcrits = Send(sClient,texte,strlen(texte))) == -1)
    {
            perror("Erreur de Send");
            close(sClient);
            exit(1);
    }   
    printf("NbEcrits = %d\n",nbEcrits);
    printf("Ecrit = --%s--\n",texte);
    printf("-----Client-----\n");
    return 0;
}
