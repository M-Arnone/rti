#include "Tcp.h"

	int ServerSocket(int port2)
	{
		//Pour l'affichage de l'adresse
		char host[NI_MAXHOST];
		char port[NI_MAXSERV];
		sprintf(port, "%d", port2);

		int sServeur;
		if ((sServeur = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("Erreur de socket()");
			exit(1);
		}
		printf("socket creee = %d\n",sServeur);


		struct addrinfo hints;
		struct addrinfo *results;
		memset(&hints,0,sizeof(struct addrinfo));
		hints.ai_family = AF_INET;
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive
		if (getaddrinfo(NULL,port,&hints,&results) != 0)
		{
				close(sServeur);
				exit(1);
		}
		getnameinfo(results->ai_addr,results->ai_addrlen,host,NI_MAXHOST,port,NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
		printf("Mon Adresse IP: %s -- Mon Port: %s\n",host,port);

		if (bind(sServeur,results->ai_addr,results->ai_addrlen) < 0)
		{
				perror("Erreur de bind()");
				exit(1);
		}
		freeaddrinfo(results);
		return sServeur;

	}
	int Accept(int sEcoute,char *ipClient)
	{
		char host[NI_MAXHOST];
		char port[NI_MAXSERV];
		sprintf(port, "%d", sEcoute);
		if (listen(sEcoute,SOMAXCONN) == -1)
		{
			perror("Erreur de listen()");
			exit(1);
		}
		printf("listen() reussi !\n");

		// Attente d'une connexion
		int sService;
		if ((sService = accept(sEcoute,NULL,NULL)) == -1)
		{
				perror("Erreur de accept()");
				exit(1);
		}
		printf("accept() reussi !\n");
		printf("socket de service = %d\n",sService);

		// Recuperation d'information sur le client connecte
		struct sockaddr_in adrClient;
		socklen_t adrClientLen;
		getpeername(sService,(struct sockaddr*)&adrClient,&adrClientLen);
		getnameinfo((struct sockaddr*)&adrClient,adrClientLen,host,NI_MAXHOST,port,NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
		printf("Client connecte --> Adresse IP: %s -- Port: %s\n",host,port);
		
		return sEcoute;

	}
	int ClientSocket(char* ipServeur,int portServeur)
	{
		//socket, getaddrinfo, connect
		int sClient;
		printf("pid = %d\n",getpid());
		// Creation de la socket
		if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("Erreur de socket()");
			exit(1);
		}
		printf("socket creee = %d\n",sClient);
		// Construction de l'adresse du serveur
		struct addrinfo hints;
		struct addrinfo *results;
		memset(&hints,0,sizeof(struct addrinfo));
		hints.ai_family = AF_INET;
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_flags = AI_NUMERICSERV;
		if (getaddrinfo(ipServeur,"1234",&hints,&results) != 0)
			exit(1);
		// Demande de connexion
		if (connect(sClient,results->ai_addr,results->ai_addrlen) == -1)
		{
			perror("Erreur de connect()");
			exit(1);
		}
		printf("connect() reussi !\n");

		return sClient;
	}
	int Send(int sSocket,char* data,int taille)
	{
		return sSocket;
	}
	int Receive(int sSocket,char* data)
	{
		return sSocket;
	}
