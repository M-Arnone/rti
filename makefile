.SILENT:

# Variables
CXX = g++
CXXFLAGS = -c -pipe -g -std=c++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -Wno-deprecated-declarations

# Specify the paths to Homebrew Qt installation
QT_INCLUDE_PATH = /opt/homebrew/opt/qt@5/lib
QT_LIB_PATH = -F/opt/homebrew/opt/qt@5/lib

INCLUDES = -I./UNIX_DOSSIER_FINAL -I./Client \
           -I/opt/homebrew/opt/qt@5/include \
           -I/opt/homebrew/opt/qt@5/include/QtWidgets \
           -I/opt/homebrew/opt/qt@5/include/QtGui \
           -I/opt/homebrew/opt/qt@5/include/QtCore


# Libraries to link against
QT_LIBS = -framework QtWidgets -framework QtGui -framework QtCore

all: CreationBD Serveur Client

Client: Client/mainclient.o Client/windowclient.o Client/moc_windowclient.o Serveur/Tcp.o
	echo "Création de Client"
	$(CXX) -o c $^ $(QT_LIB_PATH) $(QT_LIBS) -lpthread

Client/mainclient.o: Client/mainclient.cpp
	echo "Création de mainclient..."
	$(CXX) $(CXXFLAGS) $(INCLUDES) -o $@ $<

Client/windowclient.o: Client/windowclient.cpp
	echo "Création de windowclient..."
	$(CXX) $(CXXFLAGS) $(INCLUDES) -o $@ $<


Client/moc_windowclient.o : Client/moc_windowclient.cpp
	echo "création de moc_windowclient..." 
	$(CXX) $(CXXFLAGS) $(INCLUDES) -o $@ $<

CreationBD:	BD/CreationBD.cpp
	@echo 'création de la BD'
	g++ -o CreationBD BD/CreationBD.cpp -I/usr/include/mysql -I/opt/homebrew/opt/mysql/include/mysql -m64 -L/opt/homebrew/opt/mysql/lib -L/opt/homebrew/opt/openssl@3/lib -lmysqlclient -lpthread -lz -lm -lssl -lcrypto -ldl

Serveur:	Serveur/Serveur.cpp Serveur/Tcp.o Serveur/OVESP.o Serveur/queries.o
		echo compilation de Serveur..
		g++ -Wall -std=c++11 Serveur/Serveur.cpp Serveur/Tcp.o Serveur/OVESP.o Serveur/queries.o -pthread -o s -I/usr/include/mysql -I/opt/homebrew/opt/mysql/include/mysql -m64 -L/opt/homebrew/opt/mysql/lib -L/opt/homebrew/opt/openssl@3/lib -lmysqlclient -lpthread -lz -lm -lssl -lcrypto -ldl

Serveur/Tcp.o:	Serveur/Tcp.cpp
	echo compilation des librairies de sockets...
	g++ -Wno-unused-parameter -c -pipe -g  -std=c++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I./UNIX_DOSSIER_FINAL -I./Tcp -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I./Tcp -I./Tcp -I/usr/lib64/qt5/mkspecs/linux-g++ -o Serveur/Tcp.o Serveur/Tcp.cpp

Serveur/OVESP.o:	Serveur/OVESP.cpp Serveur/queries.o
		echo création du protocole...
		g++ -Wall -std=c++11 -c Serveur/OVESP.cpp -o Serveur/OVESP.o -I/usr/include/mysql -I/opt/homebrew/opt/mysql/include/mysql -m64 -L/opt/homebrew/opt/mysql/lib -L/opt/homebrew/opt/openssl@3/lib -lmysqlclient -lpthread -lz -lm -lssl -lcrypto -ldl
Serveur/queries.o:	Serveur/queries.c
	echo compilation des requetes... 
	g++ -std=c++11 -c Serveur/queries.c -o Serveur/queries.o -I/usr/include/mysql -I/opt/homebrew/opt/mysql/include/mysql -m64 -L/opt/homebrew/opt/mysql/lib -L/opt/homebrew/opt/openssl@3/lib -lmysqlclient -lpthread -lz -lm -lssl -lcrypto -ldl


clean:
	rm -f Serveur/*.o Client/*.o c CreationBD s

