#include <stdio.h>
#include <stdlib.h>
#include "tablero.h"
#include "entidades.h"
#include "main.h" 

/*
* Nombre: tablero_crear
* Parámetros: Ancho y alto del tablero
* Retorno: Puntero al tablero creado
* Descripción: Crea un tablero dinámico con todas las celdas inicializadas, sin aliens y sin daño pendiente
*/
Tablero* tablero_crear(int ancho, int alto){
    Tablero *tabla = malloc(sizeof(Tablero)); //Memoria para el tablero
    tabla->W= ancho;
    tabla->H= alto;

    tabla->celdas = malloc(alto * sizeof(void **)); //Memoria filas
    for (int y = 0; y < alto; y++) { 
        tabla->celdas[y] = malloc(ancho * sizeof(void *)); //memoria columnas
        for (int x = 0; x < ancho; x++) {
            Celda *celda = malloc(sizeof(Celda)); //Se crea la celda
            celda->alien = NULL;   
            celda->dano_pend = 0;  
            tabla->celdas[y][x] = (void*) celda; //Se guarda el puntero de la celda
        }
    }
    return tabla; //Se devuelve tablero
}
/*
* Nombre: tablero_imprimir
* Parámetros: Puntero al juego
* Retorno: void
* Descripción: Imprime el tablero con los aliens y jugador, además del HUD turno, vivos, inventario y munición y pos del jugador
*/
void tablero_imprimir(struct Juego *juego){
    //system("clear");
    //printf("DENTRO IMPRIMIR TAB turno=%d, vivos=%d\n", juego->turno, juego->vivos);
    Tablero *t = juego->t; //Tablero actual
    
    printf("================\n");
    for (int y = t->H - 1; y >= 0; y--){ // De arriba hacia abajo
        for (int x = 0; x < t->W; x++){
            Celda *celda = (Celda*) t->celdas[y][x]; //La celda
                if (celda->alien == NULL){
                    printf("[ ]");  //Vacio
                } else if (celda->alien->tipo == 1){
                    printf("[D]");  //Drone
                } else if (celda->alien->tipo == 2){
                    printf("[S]");  //Skater
                } else if (celda->alien->tipo == 3){
                    printf("[E]");  //Especial
                }  
        }
        printf("\n");
    }

    for (int x = 0; x < juego->jugador_x; x++){
        printf("   ");
    }
    printf(" ^ \n");  // jugador

    printf("================\n");
    // HUD 
    printf("Turno: %d | Aliens vivos: %d | Restantes inv.: %d | Munición (1)NOR:inf (2)PER:%d (3)ESP:%d | Jugador x= %d\n", 
        juego->turno, juego->vivos, 
        juego->pool.drone + juego->pool.skater + juego->pool.especial, 
        juego->armas.ammo_perforador, juego->armas.ammo_especial, juego->jugador_x);
    //Mensaje de las armas 
    if (juego->mensaje[0] != '\0'){
        printf("%s", juego->mensaje);
        juego->mensaje[0] = '\0'; // Limpiar después de mostrar
    }
}

/*
* Nombre: tablero_cerrar
* Parámetros: Puntero al tablero
* Retorno: void
* Descripción: Libera toda la memoria asociada al tablero y sus celdas, incluyendo aliens 
*/
void tablero_cerrar(Tablero *tabla){
    for (int y = 0; y < tabla->H; y++){ //Fila
        for (int x = 0; x < tabla->W; x++){ //Columnas
            Celda *celda = (Celda*) tabla->celdas[y][x]; 
            if (celda->alien != NULL){
                free(celda->alien); 
            }
            free(celda); //Free celda
        }
        free(tabla->celdas[y]); //Free columnas 
    }
    free(tabla->celdas); //Free filas
    free(tabla);
}

