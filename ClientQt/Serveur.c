#include "Tcp.h"

int main(){

	int sServeur = ServerSocket(1234);
	int sService = Accept(sServeur,NULL);
	return 0;
}