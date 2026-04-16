package player;

public class Oxigeno{
    private int oxigenoRestante;    
    private int capacidadMaxima;    

    /*
    * Constructor que inicializa la capacidad máxima y el oxígeno restante.
    * @param Ninguno
    * @return void
    */
    public Oxigeno(){
        this.capacidadMaxima = 60; 
        this.oxigenoRestante = 60;
    }

    /*
    * Duplica la capacidad máxima del tanque de oxígeno y lo recarga por completo.
    * @param Ninguno
    * @return void
    */
    public void duplicarCapacidad(){
        this.capacidadMaxima *= 2;
        this.oxigenoRestante = capacidadMaxima;
    }

    /*
    * Reduce el oxígeno restante en una cantidad determinada, sin permitir valores negativos.
    * @param unidades: int - cantidad de oxígeno a consumir.
    * @return void
    */
    public void consumirO2(int unidades){
        this.oxigenoRestante = Math.max(0, this.oxigenoRestante - unidades);
    }
    
    /*
    * Recarga completamente el tanque de oxígeno hasta su capacidad máxima.
    * @param Ninguno
    * @return void
    */
    public void recargarCompleto(){
        this.oxigenoRestante = this.capacidadMaxima;
    }

    /*
    * Aumenta la capacidad máxima del tanque en una cantidad específica(+30) y recarga el oxígeno.
    * @param extra: int - cantidad adicional de capacidad a agregar.
    * @return void
    */
    public void aumentarCapacidad(int extra){
        this.capacidadMaxima += extra;
        this.oxigenoRestante = this.capacidadMaxima; 
    }

    //Getter
    /*
    * Obtiene la cantidad de oxígeno restante.
    * @param Ninguno
    * @return int - oxígeno actual restante.
    */
    public int getOxigenoRestante(){ 
        return oxigenoRestante; 
    }
     /*
    * Obtiene la capacidad máxima del tanque de oxígeno.
    * @param Ninguno
    * @return int - capacidad máxima del tanque.
    */
    public int getCapacidadMaxima(){ 
        return capacidadMaxima;
    }
    //Setter
    /*
    * Establece una nueva capacidad máxima del tanque de oxígeno.
    * @param capacidadMaxima: int - nueva capacidad máxima a asignar.
    * @return void
    */
    public void setCapacidadMaxima(int capacidadMaxima){ 
        this.capacidadMaxima = capacidadMaxima;
    }
}