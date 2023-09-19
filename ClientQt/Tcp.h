#ifndef TCP_H
#define TCP_H
#define TAILLE_MAX_DATA 10000

#include <stdio.h>
#include <iostream>
#include <string>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>






class Tcp{
private:
public:
	int ServerSocket(int port);
	int Accept(int sEcoute,char *ipClient);
	int ClientSocket(char* ipServeur,int portServeur);
	int Send(int sSocket,char* data,int taille);
	int Receive(int sSocket,char* data);

};








#endif