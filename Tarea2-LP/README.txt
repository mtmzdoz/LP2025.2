Nota: 95
Nombre: Matilde Vásquez 
Rol: 202473652-3
WSL: Ubuntu-24.04


-Control de inputs
Si se ingresan varios caracteres para la acción, como 'dd' o '111', cada caracter se consumira como un 
turno independiente, es decir, primero se procesara el primer 'd' y luego el segundo 'd', consumiendo 2 turnos.
Además de que a veces se aceptan caracteres como ':a', '.a', ',a' o 'fq'(aceptandose el q)
Por lo que hay que tener cuidado al momento de ingresar los caracteres. 
¡¡Debe ser 1 caracter por acción para la correcta ejecución!!

-Inicio e Inventario Aliens
Según dificultad se asumio que los aliens iniciales se restaban del inventario total
Entonces cuando se inicia: 
Fácil -> Aliens vivos: 2 Restantes Inv.: 13. Total 15 Aliens por partida
Difícil -> Aliens vivos: 3 Restantes Inv.: 17. Total 20 Aliens por partida

-Colisiones
Si bien cada alien avanza una casilla cada dos turnos, en el caso de que 2 aliens se quisieran mover 
a la misma casilla solo 1 de ellos avanza y uno queda en el mismo lugar

- Movimiento jugador: Se asumio que el jugador se moverá siempre dentro de los márgenes
permitidos. Pero de todas maneras si este quisiera avanzar del tablero, no se cuenta como turno
-Armas:Si un arma no impacta a un alien, se consume la munición pero no se cuenta el turno
-Extras: Cuando un alien muere hay 25% de prob que de +1 ammo perforante y un 5% de +1 ammo especial

--Especiales--
Arma Especial: Es una "onda expansiva" de daño 1 y que también hace 1 de daño a sus vecinos 
Alien Especial: Se mueve horizontalmente al azar y rebota en caso de llegar a un borde, hay veces que si no tiene donde moverse 
por alguna colision con otro alien comienza a avanzar verticalmente (hacia adelante)
