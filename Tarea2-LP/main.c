#include <stdio.h>
#include <stdlib.h>  
#include <stdbool.h>
#include <ctype.h>
#include <time.h>    //Para el time del srand 
#include "main.h"
#include "tablero.h"
#include "entidades.h"
#include "spawn.h"

/*
* Nombre: mover_jugador
* Parámetros: Puntero al juego y un char que indica la direccion, 'a' izq y 'd' derecha
* Retorno: Booleano. True si el jugador se movió, false si se quedó en el borde
* Descripción: Moviliza al jugador en el tablero dentro de los límites permitidos
*/
bool mover_jugador(Juego *juego, char direccion){ 
    int posicion_anterior = juego->jugador_x;
    if((direccion == 'a' || direccion == 'A') && juego->jugador_x > 0){
        juego->jugador_x--;
    }else if((direccion == 'd' || direccion == 'D') && juego->jugador_x < juego->t->W - 1){
        juego->jugador_x++;
    }
    return(juego->jugador_x != posicion_anterior); //Si es distinto de de la pos anterior True
}

int main(){
    Juego juego;
    
    printf("====== Space Defender ======\n");
    printf("Controles: a/d mover | Armas: 1=Normal 2=Perforador 3=Especial | Q Salir ");
    printf("\n");
    
    //Validar Caracter
    char dificultad;
    do{
        printf("Dificultad (d=difícil , f=fácil): ");
        scanf(" %c", &dificultad);

        if (dificultad != 'd' && dificultad != 'D' && dificultad != 'f' && dificultad != 'F'){
            printf("Caracter inválido. Por favor ingrese 'd' o 'f'.\n");
        }
    }while (dificultad != 'd' && dificultad != 'D' && dificultad != 'f' && dificultad != 'F');
    
    juego.dificultad = dificultad;

    int W, H;
    if (dificultad == 'f'|| dificultad == 'F'){
        W=5 ; H=15;
    }else{
        W=7 ; H=15;
    }
    
    //Inicializar todo
    srand(time(NULL)); //Para poder hacer que los aliens varien en aparicion
    juego.turno = 1; 
    juego.vivos=0;        
    juego.mensaje[0] = '\0';
    juego.derrota = 0;
    juego.jugador_x = W / 2;  // Jugador inicia en el centro del tablero
    juego.armas.ammo_perforador = 7;  
    juego.armas.ammo_especial = 3;    
    //Arr de punteros a fn
    juego.armas.fn[0] = arma_normal;
    juego.armas.fn[1] = arma_perforador; 
    juego.armas.fn[2] = arma_especial;
    //
    //printf("ANTES SPAWN turno=%d, vivos=%d\n", juego.turno, juego.vivos);
    juego.t = tablero_crear(W, H);
    Pool_dificultad(&juego);
    spawn_inicio(&juego);
    
    int turno_aliens=0;
    bool gameover= false;

    
    while (!gameover){
        
        tablero_imprimir(&juego);

        //Validar Caracter   
        char caracter;
        do{
            printf("Acción (a/d/1/2/3/q): ");
            scanf(" %c", &caracter);
            caracter = tolower(caracter);
            if (caracter != 'a' && caracter != 'd' && caracter != 'q' && caracter != '1' && caracter != '2' && caracter != '3'){
                printf("Entrada inválida. Por favor ingrese 'a', 'd', '1', '2', '3' o 'q'\n");
            }
        }while (caracter != 'a' && caracter != 'd' && caracter != 'q' && caracter != '1' && caracter != '2' && caracter != '3');

        // Para terminar juego q salida
        if (caracter == 'q'){
            break; 
        }
        //Accion armas
        if (caracter >= '1' && caracter <= '3'){
            int arma_id = caracter - '1'; // '1'es id 0 (normal), '2'es id 1(perforar), '3'es id 2 (especial)
            bool disparo_exitoso = disparar_armas(&juego, arma_id);
            resolver_danos(&juego); 
            //No se avanza en caso de fallar el tiro
            if (!disparo_exitoso){ 
                continue; 
            }
        }else{
            bool se_movio = mover_jugador(&juego, caracter);
            //Si ya esta en el borde y se mueve no avanza el turno 
            if (!se_movio){
                continue;
            }
        }

        //Luego de 2 turnos se mueven los aliens
        turno_aliens++;
        if (turno_aliens >= 2){  
           mover_aliens(&juego);
           turno_aliens = 0; 
        }

        spawn_por_turno(&juego);
        
        if (juego.derrota == 1){
            tablero_imprimir(&juego); 
            printf("¡Derrota! Un alien llegó a la base. \n");
            gameover= true;
            
        }else if(juego.vivos == 0){
            tablero_imprimir(&juego);
            printf("¡VICTORY ROYALE!\n");
            gameover= true;
        }
        juego.turno++;
    }
    tablero_cerrar(juego.t);
    return 0;
    
}
