#ifndef TABLERO_H
#define TABLERO_H

#include "entidades.h"



typedef struct Tablero {
    int W, H;
    void ***celdas; /* celdas[y][x] -> (void*) que apunta a Celda* */
} Tablero;

struct Juego; // forward declaration

struct Tablero* tablero_crear(int ancho, int alto);
void tablero_imprimir(struct Juego *juego);
void tablero_cerrar(struct Tablero *tablero);

#endif