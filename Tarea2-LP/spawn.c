#include <stdio.h>
#include <stdlib.h>
#include "main.h"
#include "spawn.h"

/*
* Nombre: Pool_dificultad
* Parámetros: Puntero al juego
* Retorno: void 
* Descripción: Segun dificultad se inicializa el inventario de aliens y el tope de vivos 
*/
void Pool_dificultad(Juego *juego){
    if (juego->dificultad == 'f'){  //Ez
        juego->pool.drone = 8;
        juego->pool.skater = 4;
        juego->pool.especial = 3;   
        juego->pool.vivos_tope = 6; 
    } else{  // Dificil
        juego->pool.drone = 10;
        juego->pool.skater = 6;
        juego->pool.especial = 4;   
        juego->pool.vivos_tope = 8;
    }
}

/*
* Nombre: spawn_inicio
* Parámetros: Puntero al juego
* Retorno: void
* Descripción: según la dificultad genera y posiciona los aliens iniciales en la fila superior al comenzar la partida 
*/
void spawn_inicio(Juego *juego){
    int y = juego->t->H - 1; //Parte superior tablero
    int AliensIniciales;

    if (juego->dificultad == 'f' || juego->dificultad == 'F'){
        AliensIniciales = 2; 
    }else{
        AliensIniciales = 3; 
    }

    //Colocar aliens iniciales
    for (int i = 0; i < AliensIniciales; i++){
        int x;
         //Se busca una columna vacía al azar
        do{
            x = rand() % juego->t->W;
        }while (((Celda*)juego->t->celdas[y][x])->alien != NULL);

        //Elegir tipo según prob
        int tipo = 0;
        double prob_drone = 0.5;
        double prob_skater = 0.3; 
        //prob_especial = 0.2; 
        double r = (double)rand() / RAND_MAX;

        if (r < prob_drone && juego->pool.drone > 0){
            tipo = 1;
        }else if(r < prob_drone + prob_skater && juego->pool.skater > 0){
            tipo = 2;
        }else if(juego->pool.especial > 0){
            tipo = 3;
        }

        if (tipo!= 0){
            //Creamos el Alien
            Alien *alienito = malloc(sizeof(Alien));
            alienito->x = x;
            alienito->y = y;
            alienito->tipo = tipo;

            if (tipo == 1){          //Drone
                alienito->hp = 2;
                alienito->dx = 0;             
            } else if(tipo == 2){   //Skater
                alienito->hp = 1;
                alienito->dx = 1;      //zig-zag
            } else{                   //Especial
                alienito->hp = 2;
                alienito->dx = 0;      //No tiene patron zigzag, ya que es al azar en entidad.c
            }
        

            //Celda en el tablero
            Celda *celda = (Celda*)juego->t->celdas[y][x];
            celda->alien = alienito;


            //Act inventario
            if (tipo == 1){
                juego->pool.drone--;
            }else if (tipo == 2){
                juego->pool.skater--;
            }else{ 
                juego->pool.especial--;
            }
            //printf("DEBUG: Spawn alien tipo=%d en x=%d y=%d\n");
            juego->vivos++;
            //printf("DEBUG: vivos ahora=%d\n", juego->vivos);
        }
    }
}

/*
* Nombre: spawn_por_turno
* Parámetros: Puntero al juego
* Retorno: void
* Descripción: Genera 1 o 2 aliens por turno en la fila superior despues de comenzar, respetando
               turnos y tope de vivos
*/
void spawn_por_turno(Juego *juego){
    //Antes de spawnear se revisa el inv
    int InventarioTotal = juego->pool.drone + juego->pool.skater + juego->pool.especial;
    if (InventarioTotal == 0){
        return; 
    }
    
    //Tope de simultaneos
    if (juego->vivos >= juego->pool.vivos_tope){
        return; 
    }
    
    //Aliens a spawnear despues del sembrado inicial, 1 o 2
    int AliensXSpawnear = 1;
    int probabilidad = rand() % 100;
    if (probabilidad < 30){ //30% de probabilidad para el segundo
        AliensXSpawnear = 2;
    }
    
    int y = juego->t->H - 1; //Sup
    
    //Spawear aliens 
    for (int i = 0; i < AliensXSpawnear; i++){
        //Como entramos al for revisamos de nuevo el inventario hasta que se acabe o haga tope
        InventarioTotal = juego->pool.drone + juego->pool.skater + juego->pool.especial;
        if (InventarioTotal == 0 || juego->vivos >= juego->pool.vivos_tope){
            break; 
        }
        
        int x;
        int intentos = 0;
        //Asignacion de columnas 
        do{
            x = rand() % juego->t->W;
            intentos++;
            if (intentos > juego->t->W * 2){
                return; 
            }
        }while (((Celda*)juego->t->celdas[y][x])->alien != NULL);
        
        //Igual que antes
        int tipo = 0;
        double prob_drone = 0.5;
        double prob_skater = 0.3; 
        double r = (double)rand() / RAND_MAX;

        if (r < prob_drone && juego->pool.drone > 0){
            tipo = 1;
        }else if(r < prob_drone + prob_skater && juego->pool.skater > 0){
            tipo = 2;
        }else if(juego->pool.especial > 0){
            tipo = 3;
        }
        
        if (tipo!= 0){
            Alien *alienito = malloc(sizeof(Alien));
            alienito->x = x;
            alienito->y = y;
            alienito->tipo = tipo;

            if (tipo == 1){         
                alienito->hp = 2;
                alienito->dx = 0;            
            }else if(tipo == 2){   
            alienito->hp = 1;
            alienito->dx = 1;             
            }else{                   
                alienito->hp = 2;
                alienito->dx = 0;             
            }
            
            Celda *celda = (Celda*)juego->t->celdas[y][x];
            celda->alien = alienito;
        
        
            if (tipo == 1){
                juego->pool.drone--;
            }else if(tipo == 2){
                juego->pool.skater--;
            }else{
                juego->pool.especial--;
            }
        
            juego->vivos++;
        }
    }
}