package entorno;

import player.Jugador;
import objetos.Item;
import objetos.ItemTipo;
import java.util.EnumSet;
import java.util.Random;
import java.util.Scanner;

public class ZonaProfunda extends Zona{
    
    private int presion;
    private Random rand;

    /*
    * Constructor de la Zona Profunda. Inicializa los valores de presión, recursos disponibles, el generador aleatorio, rango de profundidad y zona siguiente.
    * @param Ninguno
    * @return void
    */
    public ZonaProfunda(){
        super("Zona Profunda", 200, 999, EnumSet.of(ItemTipo.Plata, ItemTipo.Oro, ItemTipo.Acero, ItemTipo.Diamante, ItemTipo.Magnetita));
        this.presion = 10;
        this.ZonaSiguiente = null;
        this.rand= new Random();
    }

    /*
    * Permite al jugador recolectar recursos dentro de la Zona Profunda. Calcula el consumo de oxígeno, la cantidad obtenida y agrega los ítems al inventario.
    * @param jugador: Jugador - jugador que realiza la acción de recolección.
    * @param scan: Scanner - objeto usado para capturar la elección de recurso.
    * @return void
    */
    public void recolectar(Jugador jugador, Scanner scan){
        System.out.println("\nRecolectando recursos en la Zona Profunda...");

        int ProfundidadJugador = jugador.getProfundidadActual();
        double d = ProfundidadNormalizada(ProfundidadJugador);
        double pres = jugador.getMejoraTanque() ? 0 : calcularPresion(jugador);

        int Crecolectar = (int) Math.ceil(10 + 6 * d + pres);
        jugador.getTanqueOxigeno().consumirO2(Crecolectar);
        System.out.println("Consumo de oxígeno por recolectar: " + Crecolectar);

        if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
            jugador.derrotaPorOxigeno();
            return;
        }

        System.out.println("¿Qué recurso deseas recolectar?");
        ItemTipo[] Recursos = {ItemTipo.Plata, ItemTipo.Oro, ItemTipo.Acero, ItemTipo.Diamante, ItemTipo.Magnetita};

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

        int n_min = 2, n_max = 6;
        int Cantidad =  Math.max(1, (int) Math.floor(n_min + (n_max - n_min) * d));

        jugador.agregarItem(new Item(RecursoElegido, Cantidad));
        System.out.println("Has recolectado: " + Cantidad + " x " + RecursoElegido);

    }

    /*
    * Permite al jugador explorar la Zona Profunda en busca de recompensas. Consume oxígeno proporcional a la profundidad y puede otorgar ítems aleatorios.
    * @param jugador: Jugador - jugador que realiza la exploración.
    * @return void
    */
    @Override
    public void explorar(Jugador jugador) {
        System.out.println("Explorando la Zona Profunda... ");

        int z = jugador.getProfundidadActual();
        double d = ProfundidadNormalizada(z);
        double pres = jugador.getMejoraTanque() ? 0 : calcularPresion(jugador);

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
    * Otorga una recompensa aleatoria al jugador después de explorar la zona. El recurso obtenido depende de la profundidad normalizada.
    * @param jugador: Jugador - jugador que recibe la recompensa.
    * @param d: double - profundidad normalizada (0 a 1) que afecta la cantidad.
    * @return void
    */
    private void otorgarRecompensa(Jugador jugador, double d){
        ItemTipo[] Recursos = { ItemTipo.Plata, ItemTipo.Oro, ItemTipo.Acero, ItemTipo.Diamante,ItemTipo.Magnetita};

        int n_min = 2; 
        int Cantidad = Math.max(1, (int) Math.floor(n_min * d));
        ItemTipo Recurso = Recursos[rand.nextInt(Recursos.length)];

        jugador.agregarItem(new Item(Recurso, Cantidad));
        System.out.println("Has obtenido " + Cantidad + " x " + Recurso+ "\n");
    }

    /*
    * Calcula la presión que afecta al jugador dentro de la Zona Profunda. Si el jugador tiene la mejora del tanque, la presión se anula.
    * @param jugador: Jugador - jugador actual para calcular su presión.
    * @return double - valor de la presión resultante en función de la profundidad.
    */
    public double calcularPresion(Jugador jugador){
        if (jugador.getMejoraTanque()) {
            return 0;
        }
        double d = ProfundidadNormalizada(jugador.getProfundidadActual());
        return presion + 6 * d; // fórmula de presión en prfunda
    }
}