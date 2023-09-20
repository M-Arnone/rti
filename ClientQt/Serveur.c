#include "Tcp.h"

int main(){
	
	int b;
	int a = 1234;
	b = ServerSocket(a);
	printf("a:%d  b:%d",a,b);
	return 0;
}