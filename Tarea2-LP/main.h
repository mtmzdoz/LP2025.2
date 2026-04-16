#ifndef MAIN_H
#define MAIN_H
#include <stdbool.h>
#include "armas.h"
#include "spawn.h"
#include "tablero.h"


typedef struct Juego {
    Tablero *t; /* encapsula W/H y la politica de memoria/render */
    Armas armas;
    PoolAliens pool;
    int dificultad;
    int turno, vivos, jugador_x;
    int derrota; //Para terminar el programa cuando llegan a la base
    char mensaje[200]; //Para poder mostrar el mensaje de las armas
 }Juego;

 bool mover_jugador(struct Juego *juego, char direccion);

 #endif