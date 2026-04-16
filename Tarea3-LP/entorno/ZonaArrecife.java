package entorno;

import player.Jugador;
import objetos.Item;
import objetos.ItemTipo;
import java.util.EnumSet;
import java.util.Random; 
import java.util.Scanner;

public class ZonaArrecife extends Zona{
    
    private int piezasTanque;
    private int presion;
    private Random rand; 
    
    
    /*
    * Constructor de la Zona Arrecife. Inicializa los recursos disponibles, la presión base, rango de 
    * profundidad, el generador aleatorio, el número de piezas del tanque y la zona siguiente.
    * @param Ninguno
    * @return void
    */
    public ZonaArrecife(){
        super("Zona Arrecife", 0, 199, EnumSet.of(ItemTipo.Cuarzo, ItemTipo.Silicio, ItemTipo.Cobre));
        this.piezasTanque = 3;
        this.presion = 0;
        this.rand = new Random();
        this.ZonaSiguiente = null; 
    }

    /*
    * Permite al jugador recolectar recursos dentro de la Zona Arrecife. Calcula el consumo de oxígeno y la cantidad de recursos obtenidos según la profundidad.
    * @param jugador: Jugador - el jugador que realiza la recolección.
    * @param scan: Scanner - entrada de usuario para seleccionar el tipo de recurso.
    * @return void
    */
    @Override
    public void recolectar(Jugador jugador, Scanner scan) {
        System.out.println("\nRecolectando recursos en la Zona Arrecife...");

        int ProfundidadJugador= jugador.getProfundidadActual();
        double d = ProfundidadNormalizada(ProfundidadJugador);// d de la formula 
        double pres = calcularPresion(jugador);// Presión en arrecife siempre 0 pero igual usamos formula por si se quiere cambiar 

        int Crecolectar = (int) Math.ceil(10 + 6*d + pres);
        jugador.getTanqueOxigeno().consumirO2(Crecolectar);
        System.out.println("Consumo de oxígeno por recolectar: " + Crecolectar);

        if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
            jugador.derrotaPorOxigeno();
            return;
        }

        System.out.println("¿Qué recurso deseas recolectar?");
        ItemTipo[] Recursos = {ItemTipo.Cuarzo, ItemTipo.Silicio, ItemTipo.Cobre};
        for (int i = 0; i < Recursos.length; i++){
            System.out.println((i + 1) + ") " + Recursos[i]);
        }
        System.out.print("> ");
        int Opcion = scan.nextInt();

        if (Opcion < 1 || Opcion > Recursos.length){
            System.out.println("Opción inválida. Cancelando recolección.");
            return;
        }

        ItemTipo RecursoElegido = Recursos[Opcion - 1];

        int n_min = 1, n_max = 3;
        int Cantidad = Math.max(1, (int) Math.floor(n_min + (n_max - n_min) * d));

        jugador.agregarItem(new Item(RecursoElegido, Cantidad));
        System.out.println("\nHas recolectado: " + Cantidad + " x " + RecursoElegido + "\n");
    }

    /*
    * Permite al jugador explorar la Zona Arrecife. Puede otorgar piezas del tanque o recursos comunes según probabilidad.
    * @param jugador: Jugador - jugador que realiza la exploración.
    * @return void
    */
    @Override
    public void explorar(Jugador jugador){
        System.out.println("\nExplorando la Zona Arrecife... ");

        int ProfundidadJugador = jugador.getProfundidadActual();
        double d = ProfundidadNormalizada(ProfundidadJugador); 
        double pres = jugador.getMejoraTanque() ? 0 : 0;

        int Cexplorar = (int) Math.ceil(12 + 10*d + pres);
        jugador.getTanqueOxigeno().consumirO2(Cexplorar);
        
        System.out.println("Consumo de oxígeno por explorar: " + Cexplorar);

        if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
            jugador.derrotaPorOxigeno();
            return; 
        }
       otorgarRecompensa(jugador,d);
    }

    /*
    * Asigna una recompensa al jugador tras explorar la zona. Existe una probabilidad del 30% de encontrar una pieza del tanque, en caso contrario, otorga recursos comunes.
    * @param jugador: Jugador - jugador que recibe la recompensa.
    * @param d: double - profundidad normalizada (0 a 1) usada para calcular la cantidad.
    * @return void
    */
    private void otorgarRecompensa(Jugador jugador, double d){
        double prob= rand.nextDouble();

        if (piezasTanque > 0 && prob < 0.3) {
            System.out.println("¡Encontraste una Pieza Tanque!");
            jugador.agregarItem(new Item(ItemTipo.PIEZA_TANQUE, 1));
            piezasTanque--;
        } else {
            int n_min = 1;
            int Cantidad = Math.max(1, (int) Math.floor(n_min * d));

            ItemTipo[] Recursos = {ItemTipo.Cuarzo, ItemTipo.Silicio, ItemTipo.Cobre};
            ItemTipo Recurso = Recursos[rand.nextInt(Recursos.length)];
            jugador.agregarItem(new Item(Recurso, Cantidad));
            System.out.println("Has recolectado: " + Cantidad + " x " + Recurso );
        }
    }

    /*
    * Calcula la presión que afecta al jugador dentro de la Zona Arrecife.
    * En esta zona la presión siempre es 0, pero se deja el método por consistencia.
    * @param jugador: Jugador - jugador actual en la zona.
    * @return double - valor de la presión (0 en este caso).
    */
    public double calcularPresion(Jugador jugador){
        if (jugador.getMejoraTanque()) {
            return 0;
        }
        return presion;
    }

    //Getter
    /*
    * Devuelve el valor actual de la presión en la zona.
    * @param Ninguno
    * @return int - presión base de la zona.
    */
    public int getPresion(){ 
        return presion; 
    }
    /*
    * Devuelve la cantidad de piezas de tanque restantes por encontrar.
    * @param Ninguno
    * @return int - número de piezas disponibles.
    */
    public int getPiezasTanque(){
        return piezasTanque;
    }
    //Setter
    /*
    * Establece un nuevo valor para la cantidad de piezas de tanque.
    * @param piezasTanque: int - nuevo número de piezas disponibles.
    * @return void
    */
    public void setPiezasTanque(int piezasTanque){
        this.piezasTanque = piezasTanque; 
    }
}