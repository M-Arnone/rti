.SILENT:
CC = g++ -Wall -m64 -g

all:	CreationBD

CreationBD:	CreationBD.cpp
	@echo 'création de la BD'
	g++ -o CreationBD CreationBD.cpp -I/usr/include/mysql -I/opt/homebrew/opt/mysql/include/mysql -m64 -L/opt/homebrew/opt/mysql/lib -L/opt/homebrew/opt/openssl@3/lib -lmysqlclient -lpthread -lz -lm -lssl -lcrypto -ldl

clean:
	rm -f Serveur/*.o Client/*.o c CreationBD s

