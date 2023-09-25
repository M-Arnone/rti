.SILENT:
CC = g++ -Wall -m64 -g

all:	Serveur

Serveur:	Serveur.cpp Tcp.o OVESP.o
		echo compilation de Serveur..
		g++ -Wall -std=c++11 Serveur.cpp Tcp.o OVESP.o -pthread -o Serveur


Tcp.o:	Tcp.cpp
		echo compilation des librairies de sockets...
		g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o Tcp.o Tcp.cpp


OVESP.o:	OVESP.cpp
		echo creation du protocole...
		g++ -Wall -std=c++11 -c OVESP.cpp -o OVESP.o


clean:
			rm *.o -f
			rm Serveur -f
