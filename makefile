.SILENT:
CC = g++ -Wall -m64 -g

all:	CreationBD Serveur Client

Client: Client/mainclient.o Client/windowclient.o Client/moc_windowclient.o Serveur/Tcp.o
	echo création de Client
	g++ -Wno-unused-parameter -o c Client/mainclient.o Client/windowclient.o Client/moc_windowclient.o Serveur/Tcp.o /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread

Client/mainclient.o : Client/mainclient.cpp
	echo création de mainclient..
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I./UNIX_DOSSIER_FINAL -I./Client -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I./Client -I./Client -I/usr/lib64/qt5/mkspecs/linux-g++ -o Client/mainclient.o Client/mainclient.cpp

Client/windowclient.o : Client/windowclient.cpp
	echo création de windowclient..
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I./UNIX_DOSSIER_FINAL -I./Client -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I./Client -I./Client -I/usr/lib64/qt5/mkspecs/linux-g++ -o Client/windowclient.o Client/windowclient.cpp

Client/moc_windowclient.o : Client/moc_windowclient.cpp
	echo création de moc_windowclient...
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I./UNIX_DOSSIER_FINAL -I./Client -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I./Client -I./Client -I/usr/lib64/qt5/mkspecs/linux-g++ -o Client/moc_windowclient.o Client/moc_windowclient.cpp

CreationBD:	BD/CreationBD.cpp
	echo création de la BD
	g++ -o CreationBD BD/CreationBD.cpp -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

Serveur:	Serveur/Serveur.cpp Serveur/Tcp.o Serveur/OVESP.o Serveur/queries.o
		echo compilation de Serveur..
		g++ -Wall -std=c++11 Serveur/Serveur.cpp Serveur/Tcp.o Serveur/OVESP.o Serveur/queries.o -pthread -o s -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

Serveur/Tcp.o:	Serveur/Tcp.cpp
	echo compilation des librairies de sockets...
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I./UNIX_DOSSIER_FINAL -I./Tcp -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I./Tcp -I./Tcp -I/usr/lib64/qt5/mkspecs/linux-g++ -o Serveur/Tcp.o Serveur/Tcp.cpp

Serveur/OVESP.o:	Serveur/OVESP.cpp Serveur/queries.o
		echo création du protocole...
		g++ -Wall -std=c++11 -c Serveur/OVESP.cpp -o Serveur/OVESP.o -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

Serveur/queries.o:	Serveur/queries.c
	echo compilation des requetes... 
	g++ -std=c++11 -c Serveur/queries.c -o Serveur/queries.o -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl


clean:
	rm -f Serveur/*.o Client/*.o c CreationBD s

