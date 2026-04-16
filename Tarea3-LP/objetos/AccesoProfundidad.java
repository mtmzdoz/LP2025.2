package objetos;


public interface AccesoProfundidad{
    /*
    * Determina si el objeto puede acceder a una profundidad específica.
    * @param requerido: int - la profundidad o nivel que se desea alcanzar.
    * @return boolean - true si el acceso es posible, false si no se cumplen las condiciones.
    */
    boolean puedeAcceder(int requerido);
}