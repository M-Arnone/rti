#include "Tcp.h"

int main(){

	int sServer = ServerSocket(1234);
	int sService = Accept(sServer,NULL);
	printf("sService : %d\n",sService);

	// ***** Reception texte pur **************************************
	char buffer[100];
	int nbLus;
	if ((nbLus = Receive(sService,buffer)) < 0)
	{
		perror("Erreur de Receive");
		close(sService);
		close(sServer);
		exit(1);
	}
	printf("NbLus = %d\n",nbLus);
	buffer[nbLus] = 0;
	printf("Lu= --%s--\n",buffer);

	return 0;
}