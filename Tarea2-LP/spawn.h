#ifndef SPAWN_H
#define SPAWN_H



typedef struct PoolAliens {
    int especial; 
    int skater; 
    int drone;
    int vivos_tope; /* tope de aliens simultaneos en pantalla */
} PoolAliens;
void spawn_inicio(struct Juego *juego);
void Pool_dificultad(struct Juego *juego);
void spawn_por_turno(struct Juego *juego);
#endif