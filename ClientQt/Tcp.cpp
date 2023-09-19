#include "Tcp.h"

	int Tcp::ServerSocket(int s)
	{
		if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("Erreur de socket()");
			exit(1);
		}
		//getAdresseInfo
		//bind
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
