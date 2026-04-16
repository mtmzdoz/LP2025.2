
#include <stdio.h>
#include <stdlib.h>
#include "main.h"
#include "entidades.h"

/*
* Nombre: mover_aliens
* Parámetros: Puntero al juego
* Retorno: void
* Descripción: Moviliza a los aliens según su tipo, evita colisiones y marca la derrota si algún alien llega a y=0
*/
void mover_aliens(Juego *juego){
    Tablero *t = juego->t; //tablero act

    //Recorremos filas y col 
    for (int y = 0; y < t->H; y++){
        for (int x = 0; x < t->W; x++){
            Celda *celda = (Celda*) t->celdas[y][x]; //Celda act
            //Si hay un alien eliminamos para cambiarlo de pos
            if (celda->alien != NULL){
                Alien *alienito = celda->alien;
                celda->alien = NULL;

                int nueva_y = y;
                int nueva_x = x;

                //Mvimiento según Alien
                if (alienito->tipo == 1){ //Drone
                    nueva_y = y - 1;
                }else if(alienito->tipo == 2){ //Skater
                    nueva_y = y - 1;
                    nueva_x = x + alienito->dx;

                    // Para rebotar en bordes y que no deaparezca
                    if (nueva_x < 0){ 
                        alienito->dx = 1; 
                        nueva_x = 0; 
                    }else if(nueva_x >= t->W){ 
                        alienito->dx = -1;
                        nueva_x = t->W - 1; 
                    }
                }else if(alienito->tipo == 3){ //Especial horizontal al azar
                    nueva_y = y - 1;

                    int direccion = (rand() % 2) ? 1 : -1; //1 = derecha, -1 = izquierda
                    nueva_x = x + direccion;

                    //Limites tablero
                    if(nueva_x < 0 || nueva_x >= t->W){
                        nueva_x = x; 
                    }

                }
                //Colisiónes entre Aliens
                if (nueva_y >= 0){
                    Celda *destino = (Celda*) t->celdas[nueva_y][nueva_x];
                    if (destino->alien != NULL){
                        nueva_x = x;
                        nueva_y = y;
                    }
                }
                //Asignar Alien a la nueva pos
                if (nueva_y >= 0){
                    Celda *destino = (Celda*) t->celdas[nueva_y][nueva_x];
                    destino->alien = alienito;
                    alienito->x = nueva_x;
                    alienito->y = nueva_y;

                    //Si llega a y = 0 se marca la derrota y luego se usa en main
                    if (nueva_y == 0){
                        juego->derrota = 1;
                    }
                }  
            }
        }
    }
}

/*
* Nombre: resolver_danos
* Parámetros: Puntero al juego
* Retorno: void
* Descripción: Aplica el daño pendiente a los aliens, actualiza la cantidad de aliens vivos,
libera la memoria de los muertos,  y genera posibles bonus de munición para perforante y especial
*/
void resolver_danos(Juego *juego){
    Tablero *t = juego->t;

    for (int y = 0; y < t->H; y++){ 
        for (int x = 0; x < t->W; x++){ //Recorre filas y col
            Celda *celda = (Celda*)t->celdas[y][x];
            if (celda->alien != NULL && celda->dano_pend > 0){ //Al con daño pendiente
                celda->alien->hp -= celda->dano_pend; //Dcto de vida por el daño
                celda->dano_pend = 0;
                if (celda->alien->hp <= 0){ //Se elimina si se muere

                    //Tirada para el bonus
                    double r = (double)rand() / RAND_MAX;
                
                    //Ammo Perforadora
                    if (r < 0.25){
                        juego->armas.ammo_perforador++;
                        snprintf(juego->mensaje, sizeof(juego->mensaje), "+1 munición perforador\n");
                    //Ammo Especial
                    }else if (r < 0.30){ 
                        juego->armas.ammo_especial++;
                        snprintf(juego->mensaje, sizeof(juego->mensaje), "+1 munición especial\n");
                    }
                    free(celda->alien);
                    celda->alien = NULL;
                    juego->vivos--;
                }
            }
        }
    }
}