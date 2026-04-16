#include "armas.h"
#include "main.h" 
#include <stdio.h>
#include <stdlib.h>

/*
* Nombre: disparar_armas
* Parámetros: Puntero al juego e ID del arma a disparar (0 normal, 1 perforador, 2 especial)
* Retorno: Booleano. True si el disparo fue exitoso (impactó algún alien), en caso contrario false
* Descripción: Llama a la función del arma correspondiente y ejecuta el disparo
*/
bool disparar_armas(struct Juego *juego, int arma_id){
    if (arma_id < 0 || arma_id > 2){ //Si el id de arma es <0 o >2 es invalido
        return false; 
    }
    //Se guarda el arma segun id en el juego
    return juego->armas.fn[arma_id](juego);
}

/*
* Nombre: arma_normal
* Parámetros: Puntero al juego
* Retorno: Booleano. True si impacta algún alien, false en caso contrario
* Descripción: Disparo recto de daño 1 en la columna del jugador (munición ilimitada)
*/
bool arma_normal(struct Juego *juego){
    int x = juego->jugador_x; //Ubi jugador
    Tablero *t = juego->t;
    
    //Se busca al primer alien que este en la columna que esta el jugador
    for (int y = 1; y < t->H; y++){ //Desde y=1 hacia arriba 
        Celda *celda = (Celda*)t->celdas[y][x];
        if (celda->alien != NULL){
            celda->dano_pend += 1; // Se marca daño
            snprintf(juego->mensaje,sizeof(juego->mensaje),"¡Disparo normal impactó!\n");
            return true;
        }
    }
    
    snprintf(juego->mensaje,sizeof(juego->mensaje),"Disparo normal no impactó ningún alien...\n");
    return false; 
}

/*
* Nombre: arma_perforador
* Parámetros: Puntero al juego
* Retorno: Booleano. True si impacta algún alien, false en caso contrario
* Descripción: Disparo que atraviesa toda la columna y hace daño 1 a todos los aliens en ella, consume munición perforadora
*/
bool arma_perforador(struct Juego *juego){
    //Se revisa la ammo
    if (juego->armas.ammo_perforador <= 0){
        snprintf(juego->mensaje,sizeof(juego->mensaje),"¡Sin munición perforadora!\n");
        return false;
    }
    
    int x = juego->jugador_x; //Ubi
    Tablero *t = juego->t;
    bool impacto = false; //Flag para el mensaje de daño
    
    //Se recorre toda la col para marcar el daño a los aliens en la columna
    for (int y = 1; y < t->H; y++){ //desde y=1 hacia arriba
        Celda *celda = (Celda*)t->celdas[y][x];
        if (celda->alien != NULL){
            celda->dano_pend += 1; 
            impacto= true;
        }
    }

    juego->armas.ammo_perforador--;
    
    if (impacto){
        snprintf(juego->mensaje, sizeof(juego->mensaje), "¡Disparo perforador impactó!\n");
        return true;
    }else{
        snprintf(juego->mensaje, sizeof(juego->mensaje), "Disparo perforador no impactó ningún alien...\n");
        return false;
    }
}

/*
* Nombre: arma_especial
* Parámetros: Puntero al juego
* Retorno: Booleano. True si impacta algún alien, false en caso contrario
* Descripción: Onda expansiva de daño 1 al primer alien en la columna y que además hace daño de 1 a sus vecinos 
*/
bool arma_especial(struct Juego *juego){
    if (juego->armas.ammo_especial <= 0){
        snprintf(juego->mensaje,sizeof(juego->mensaje),"¡Sin munición especial!\n");
        return false;
    }

    int x = juego->jugador_x;
    Tablero *t = juego->t;
    bool impacto= false;

    //Se ubica al alien en la columna en la que esta el jugador
    int y_objetivo = -1; // Inicializado -1 = no encontrado
    for (int y = 1; y < t->H; y++){ //desde y=1
        Celda *celda = (Celda*)t->celdas[y][x];
        if (celda->alien != NULL){
            y_objetivo = y;
            break; //Se encuentra el alien y se guarda la pos
        }
    }

    if (y_objetivo != -1){ //Si se encontro un alien
        //Revisamos la fila del alien objetivo
        for (int dx = -1; dx <= 1; dx++){ //si x=2, 1. 2-1 = 1 ataque pos, 2. 2 + 0 = ataque pos, 3. 2+1= ataquepos
            int ataquepos = x + dx;
            //Limites del tablero, cuando queda ataquepos -1 lo salta
            if (ataquepos < 0 || ataquepos >= t->W){
                continue;
            }
            Celda *celda = (Celda*)t->celdas[y_objetivo][ataquepos]; //Celda donde estan el alien en la col y sus vecinos
            if (celda->alien != NULL){
                celda->dano_pend += 1; 
                impacto= true;; 
            }
        }
    }
   
    juego->armas.ammo_especial--;
    if (impacto){
        snprintf(juego->mensaje, sizeof(juego->mensaje), "¡Onda expansiva impactó!\n");
        return true;
    }else{
        snprintf(juego->mensaje, sizeof(juego->mensaje), "Onda expansiva no impactó ningún alien...\n");
        return false;
    }
    
}

