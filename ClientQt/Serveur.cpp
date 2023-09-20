#include "Tcp.h"

int main(){
	Tcp t;
	int b;
	int a = 1234;
	b = t.ServerSocket(a);
	printf("a:%d  b:%d",a,b);
	return 0;
}