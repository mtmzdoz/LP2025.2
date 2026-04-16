package entorno;

import player.Jugador;
import objetos.ItemTipo;
import java.util.EnumSet;
import java.util.Scanner;


public abstract class Zona{
    
    public String nombre; 
    private int profundidadMin;            
    private int profundidadMax;            
    private EnumSet<ItemTipo> recursos;  
    protected Zona ZonaSiguiente;  
    protected Zona ZonaAnterior;

    /*
    * Constructor de la clase Zona. Inicializa los valores de nombre, rango de profundidad y recursos disponibles.
    * @param nombre: String - nombre descriptivo de la zona.
    * @param min: int - profundidad mínima de la zona.
    * @param max: int - profundidad máxima de la zona.
    * @param recursosDisponibles: EnumSet<ItemTipo> - conjunto de recursos que se pueden encontrar.
    * @return void
    */
    public Zona(String nombre, int min, int max, EnumSet<ItemTipo> recursosDisponibles){
        this.nombre = nombre;
        this.profundidadMin = min;
        this.profundidadMax = max;
        this.recursos = recursosDisponibles;
    }

    /*
    * Define la acción de entrar a una zona. Puede ser sobreescrita por las subclases para aplicar efectos específicos.
    * @param jugador: Jugador - jugador que entra a la zona.
    * @return void
    */
    public void entrar(Jugador jugador){
    }

    /* Permite al jugador explorar la zona. Método abstracto que debe ser implementado por cada tipo de zona.
    * @param jugador: Jugador - jugador que realiza la exploración.
    * @return void
    */
    public void explorar(Jugador jugador){
    }

    /*
    * Permite al jugador recolectar recursos en la zona.Método abstracto que debe ser implementado por cada subclase de zona.
    * @param jugador: Jugador - jugador que realiza la recolección.
    * @param scan: Scanner - entrada para seleccionar recursos.
    * @return void
    */
    public void recolectar(Jugador jugador, Scanner scan){
    }

    /*
    * Calcula el valor normalizado de la profundidad actual dentro del rango de la zona.
    * Se usa para escalar variables como la cantidad de recursos o el consumo de oxígeno.
    * @param num: int - profundidad actual del jugador.
    * @return double - valor normalizado entre 0 y 1.
    */
    public double ProfundidadNormalizada(int num){
        double divisor = Math.max(1, this.profundidadMax - this.profundidadMin);
        return (double) (num - this.profundidadMin) / divisor;
    }

    // Getter
    /*
    * Devuelve la profundidad mínima de la zona.
    * @param Ninguno
    * @return int - profundidad mínima.
    */
    public int getProfundidadMin(){ 
        return profundidadMin; 
    }

    /*
    * Devuelve la profundidad máxima de la zona.
    * @param Ninguno
    * @return int - profundidad máxima.
    */
    public int getProfundidadMax(){
        return profundidadMax; 
    }

    /*
    * Devuelve los recursos disponibles en la zona.
    * @param Ninguno
    * @return EnumSet<ItemTipo> - conjunto de tipos de ítems que se pueden recolectar.
    */
    public EnumSet<ItemTipo> getRecursos(){ 
        return recursos; 
    } 
    /*
    * Devuelve la referencia a la zona siguiente.
    * @param Ninguno
    * @return Zona - referencia a la zona más profunda conectada.
    */
    public Zona getZonaSiguiente(){
        return ZonaSiguiente; 
    }

    
    /*
    * Devuelve la referencia a la zona anterior.
    * @param Ninguno
    * @return Zona - referencia a la zona más superficial conectada.
    */
    public Zona getZonaAnterior(){
        return ZonaAnterior;
    }

    //Setter 
    /*
    * Establece la referencia a la zona siguiente (más profunda).
    * @param ZonaSig: Zona - objeto de tipo Zona que se encuentra a mayor profundidad.
    * @return void
    */
    public void setZonaSiguiente(Zona ZonaSig){
        this.ZonaSiguiente = ZonaSig;
    }

    /*
    * Establece la referencia a la zona anterior (más superficial).
    * @param zonaAnterior: Zona - objeto de tipo Zona que se encuentra a menor profundidad.
    * @return void
    */
    public void setZonaAnterior(Zona zonaAnterior){
        this.ZonaAnterior = zonaAnterior;
    }
}