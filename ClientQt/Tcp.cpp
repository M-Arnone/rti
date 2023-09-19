#include "Tcp.h"

	int Tcp::ServerSocket(int s)
	{
		if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("Erreur de socket()");
			exit(1);
		}
		struct addrinfo hints;
		struct addrinfo *results;
		memset(&hints,0,sizeof(struct addrinfo));
		hints.ai_family = AF_INET;
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive
		if (getaddrinfo(NULL,"50000",&hints,&results) != 0)
		{
				close(s);
				exit(1);
		}
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
