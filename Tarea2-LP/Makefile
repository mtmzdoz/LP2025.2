CC = gcc #Compilador, en c++ era g++ -o juego main.cpp ... 
CFLAGS = -Wall -g #Opciones para compilar

OBJ = main.o tablero.o spawn.o entidades.o armas.o # Lista de arch 

juego: $(OBJ)	# Objetivo principal, se complia a partir de los archivos. Se genera la ejecucion. make
	$(CC) $(CFLAGS) -o juego $(OBJ) 

%.o: %.c #regla generica para compilar de .c a .o
	$(CC) $(CFLAGS) -c $<

#Limpia los archivos compilados, make clean
clean:
	rm -f *.o juego
