#include "Tcp.h"

	int Tcp::ServerSocket(int port2)
	{
		//Pour l'affichage de l'adresse
		char host[NI_MAXHOST];
		char port[NI_MAXSERV];
		sprintf(port, "%d", port2);

		int s;
		if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("Erreur de socket()");
			exit(1);
		}
		printf("socket creee = %d\n",s);


		struct addrinfo hints;
		struct addrinfo *results;
		memset(&hints,0,sizeof(struct addrinfo));
		hints.ai_family = AF_INET;
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive
		if (getaddrinfo(NULL,port,&hints,&results) != 0)
		{
				close(s);
				exit(1);
		}
		getnameinfo(results->ai_addr,results->ai_addrlen,host,NI_MAXHOST,port,NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
		printf("Mon Adresse IP: %s -- Mon Port: %s\n",host,port);

		if (bind(s,results->ai_addr,results->ai_addrlen) < 0)
		{
				perror("Erreur de bind()");
				exit(1);
		}
		freeaddrinfo(results);
		return s;

	}
	int Tcp::Accept(int sEcoute,char *ipClient)
	{
		return sEcoute;
	}
	int Tcp::ClientSocket(char* ipServeur,int portServeur)
	{
		return portServeur;
	}
	int Tcp::Send(int sSocket,char* data,int taille)
	{
		return sSocket;
	}
	int Tcp::Receive(int sSocket,char* data)
	{
		return sSocket;
	}
