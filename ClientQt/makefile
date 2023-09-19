.SILENT:
CC = g++ -Wall -m64 -g

all:	Client CreationBD Serveur


Client:	mainclient.o windowclient.o moc_windowclient.o
		echo creation de ClientQt
		g++ -Wno-unused-parameter -o Client mainclient.o windowclient.o moc_windowclient.o /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread

mainclient.o :	mainclient.cpp
		echo creation de mainclient..
		g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o mainclient.o mainclient.cpp

windowclient.o :	windowclient.cpp
		echo creaiton de windowclient..
		g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o windowclient.o windowclient.cpp

moc_windowclient.o :	moc_windowclient.cpp
		echo creation de moc_windowclient...
		g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o moc_windowclient.o moc_windowclient.cpp

CreationBD:	CreationBD.cpp
			echo creation de la BD
			g++ -o CreationBD CreationBD.cpp -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl


Serveur:	Serveur.cpp Tcp.cpp
			echo compilation de Serveur..
			g++ -Wall -std=c++11 Serveur.cpp Tcp.cpp -o Serveur

Tcp.o:	Tcp.cpp
		echo compilation des librairies de sockets...
		g++  -Wall -g -std=gnu++11 Tcp.cpp -o Tcp.o


clean:
			rm *.o -f
			rm Client
