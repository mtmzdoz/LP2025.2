package entorno;

import player.Jugador;
import objetos.Item;
import objetos.ItemTipo;
import java.util.EnumSet;
import java.util.Random;
import java.util.Scanner;

public class ZonaVolcanica extends Zona{
    private boolean planoEncontrado; 
    private int presion;
    private Random rand;
    
    /*
    * Constructor de la Zona Volcánica. Inicializa los recursos disponibles, la presión, rango de profundidad, el estado del plano, el generador aleatorio y la zona siguiente.
    * @param Ninguno
    * @return void
    */
    public ZonaVolcanica(){
        super("Zona Volcánica", 1000, 1500, EnumSet.of(ItemTipo.Titanio, ItemTipo.Sulfuro, ItemTipo.Uranio));
        this.planoEncontrado = false;
        this.presion= 10000;
        this.rand= new Random();
    }

     /*
    * Permite al jugador recolectar recursos dentro de la Zona Volcánica. Existe un 20% de probabilidad de perder la conciencia debido a las condiciones extremas.
    * @param jugador: Jugador - el jugador que realiza la acción.
    * @param scan: Scanner - objeto utilizado para capturar la opción de recurso elegida.
    * @return void
    */
    public void recolectar(Jugador jugador, Scanner scan){
        System.out.println("\nRecolectando recursos en la Zona Profunda...");

        if (rand.nextDouble() < 0.20){
            jugador.perdidaConciencia();
            return;
        }
        
        int ProfundidadJugador = jugador.getProfundidadActual();
        double d = ProfundidadNormalizada(ProfundidadJugador); 
        double pres = jugador.getMejoraTanque() ? 0 : calcularPresion(jugador); //Ya que verificamos antes de bajar y si se puede bajar es porque tenemos la mejora piezas tanque

        int Crecolectar = (int) Math.ceil(10 + 6 * d + pres);
        jugador.getTanqueOxigeno().consumirO2(Crecolectar);
        System.out.println("Consumo de oxígeno por recolectar: " + Crecolectar);

        if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
            jugador.derrotaPorOxigeno();
            return;
        }

        System.out.println("¿Qué recurso deseas recolectar?");
        ItemTipo[] Recursos = { ItemTipo.Titanio, ItemTipo.Sulfuro, ItemTipo.Uranio};

        for (int i = 0; i < Recursos.length; i++) {
            System.out.println((i + 1) + ") " + Recursos[i]);
        }
        System.out.print("> ");
        int Opcion = scan.nextInt();

        if (Opcion < 1 || Opcion > Recursos.length) {
            System.out.println("Opción inválida. Cancelando recolección.");
            return;
    
        }

        ItemTipo RecursoElegido = Recursos[Opcion - 1];

        int n_min = 3, n_max = 8;
        int Cantidad = Math.max(1, (int) Math.floor(n_min + (n_max - n_min) * d));

        jugador.agregarItem(new Item(RecursoElegido, Cantidad));
        System.out.println("Has recolectado: " + Cantidad + " x " + RecursoElegido);
    }


    /*
    * Permite al jugador explorar la zona volcánica en busca de recompensas o peligros. Consume oxígeno y puede provocar pérdida de conciencia.
    * @param jugador: Jugador - jugador que realiza la exploración.
    * @return void
    */
    @Override
    public void explorar(Jugador jugador){
        System.out.println("Explorando la Zona Volcánica... ");

        if (rand.nextDouble() < 0.20) {
            jugador.perdidaConciencia();
            return;
        }

        double d = ProfundidadNormalizada(jugador.getProfundidadActual());
        double pres = 0; //Ya que verificamos antes de bajar y si se puede bajar es porque tenemos la mejora piezas tanque 

        int Cexplorar = (int) Math.ceil(12 + 10 * d + pres);
        jugador.getTanqueOxigeno().consumirO2(Cexplorar);
        System.out.println("Consumo de oxígeno por explorar: " + Cexplorar);


        if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
            jugador.derrotaPorOxigeno();
            return;
        }

        otorgarRecompensa(jugador, d);
    }

    /*
    * Asigna una recompensa al jugador luego de explorar la zona. Puede otorgar el plano de la nave estrellada o recursos aleatorios.
    * @param jugador: Jugador - jugador que recibe la recompensa.
    * @param d: double - profundidad normalizada (0 a 1) usada para calcular la cantidad.
    * @return void
    */
    private void otorgarRecompensa(Jugador jugador, double d){

        if (!planoEncontrado && rand.nextDouble() < 0.15){
            System.out.println("¡Has encontrado el Plano de la Nave Estrellada!");
            jugador.agregarItem(new Item(ItemTipo.PLANO_NAVE,1));
            planoEncontrado = true;
            jugador.setTienePlanos(true);

            return;
        }else{
            ItemTipo[] Recursos = {ItemTipo.Titanio, ItemTipo.Sulfuro, ItemTipo.Uranio};
            ItemTipo Recurso = Recursos[rand.nextInt(Recursos.length)];

            int n_min = 2;
            int Cantidad = Math.max(1, (int) Math.floor(n_min * d));

            jugador.agregarItem(new Item(Recurso, Cantidad));
            System.out.println("Has recolectado: " + Cantidad + " x " + Recurso);
        }
    }

    /*
    * Calcula la presión que afecta al jugador dentro de la Zona Volcanica. Si el jugador tiene la mejora del tanque, la presión se anula.
    * @param jugador: Jugador - jugador actual para calcular su presión.
    * @return double - valor de la presión resultante en función de la profundidad.
    */
    public double calcularPresion(Jugador jugador){
        if (jugador.getMejoraTanque()){
            return 0;
        }
        return presion; 
    }

    
}