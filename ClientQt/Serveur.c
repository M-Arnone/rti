#include "Tcp.h"

int main(){

	int b;
	int a = 1234;
	b = ServerSocket(a);
	Accept(b,NULL);
	return 0;
}