#include "Tcp.h"

int sService;
int sEcoute;

//void HandlerSIGINT(int s);
//void TraitementConnexion();

int main(){


	sEcoute = ServerSocket(1234);
	printf("Demarrage du serveur.\n");

	sService = Accept(sEcoute,NULL);
	printf("sService : %d\n",sService);
// ***** Reception texte pur **************************************
	char buffer[100];
	int nbLus;
	if ((nbLus = Receive(sService,buffer)) < 0)
	{
		perror("Erreur de Receive");
		close(sService);
		close(sEcoute);
		exit(1);
	}
	printf("NbLus = %d\n",nbLus);
	buffer[nbLus] = 0;
	printf("Lu= --%s--\n",buffer);
	
	shutdown(sService,SHUT_RDWR);
	shutdown(sEcoute,SHUT_RDWR);

	return 0;
}


