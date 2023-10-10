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
#include <cstring>
#include <stdbool.h> 
#include <signal.h>
#include <map>
#include <sstream>
#include <fstream>
#include <vector>

int ServerSocket(int port);
int Accept(int sEcoute,char *ipClient);
int ClientSocket(char* ipServeur,int portServeur);
int Send(int sSocket,char* data,int taille);
int Receive(int sSocket,char* data);

std::map<std::string, int> loadConfig(const std::string& filename); 








#endif