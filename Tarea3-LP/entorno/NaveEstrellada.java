package entorno;
import player.Jugador;
import objetos.Item;
import objetos.ItemTipo;
import java.util.EnumSet;
import java.util.Random;
import java.util.Scanner;

public class NaveEstrellada extends Zona{
    private boolean moduloEncontrado; 
    private Random rand;
    private int AccionesSinTraje;

    /*
    * Constructor de la Nave Estrellada. Inicializa los recursos disponibles, rango de profundidad, el generador 
    * aleatorio, modulo de profundidad y las acciones posibes sin traje térmico.
    * @param Ninguno
    * @return void
    */
    public NaveEstrellada(){
        super("Nave Estrellada", 0, 0, EnumSet.noneOf(ItemTipo.class)); 
        this.moduloEncontrado = false;
        this.rand = new Random();
        this.AccionesSinTraje = 0;
    }

    /*
    * Permite al jugador recolectar recursos dentro de la Nave Estrellada. Calcula la cantidad recolectada y aplica penalización por calor si no posee traje térmico.
    * @param jugador: Jugador - jugador que realiza la recolección.
    * @param scan: Scanner - entrada usada para seleccionar el tipo de recurso.
    * @return void
    */
    public void recolectar(Jugador jugador, Scanner scan){
        System.out.println("Recolectando recursos en la Nave Estrellada...");

        double d = ProfundidadNormalizada(jugador.getProfundidadActual());
        int n_min = 1, n_max = 4;
        int Cantidad = Math.max(1, (int) Math.floor(n_min + (n_max - n_min) * d));

        ItemTipo[] Recursos = { ItemTipo.Cables, ItemTipo.Piezas_Metal };
        System.out.println("¿Qué recurso deseas recolectar?");
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
        jugador.agregarItem(new Item(RecursoElegido, Cantidad));
        System.out.println("Has recolectado: " + Cantidad + " x " + RecursoElegido);

        if (!jugador.isTrajeTermico()){
            AccionesSinTraje++;
            if (AccionesSinTraje == 1){
                System.out.println("El calor dentro de la nave es insoportable sin traje térmico...");
                System.out.println("Si vuelves a intentar explorar o recolectar, morirás por sofocación.");
                System.out.println("Te recomiendo volver a la Nave Exploradora o crear el traje térmico antes de continuar.");
                AccionesSinTraje++; 

            }else if(AccionesSinTraje > 1){
                System.out.println("Has ignorado el calor... ¡Has sucumbido a la sofocación!");
                jugador.derrotaPorSofocacion();
                return;
            }
        }
    }

    /*
    * Permite al jugador explorar la Nave Estrellada en busca de componentes raros. Puede otorgar el Módulo de Profundidad y castigar la falta de traje térmico.
    * @param jugador: Jugador - jugador que realiza la exploración.
    * @return void
    */
    @Override
    public void explorar(Jugador jugador){
        System.out.println("\nExplorando el interior de la Nave Estrellada...");

        if (!jugador.isTrajeTermico()){
            AccionesSinTraje++;
            if (AccionesSinTraje == 1){
                System.out.println("El calor dentro de la nave es insoportable sin traje térmico...");
                System.out.println("Si vuelves a intentar explorar o recolectar, morirás por sofocación.");
                System.out.println("Te recomiendo volver a la Nave Exploradora o crear el traje térmico antes de continuar.");
                AccionesSinTraje++; 

            }else if(AccionesSinTraje > 1){
                System.out.println("Has ignorado el calor... ¡Has sucumbido a la sofocación!");
                jugador.derrotaPorSofocacion();
                return;
            }
        }

        if (!(jugador.getZonaActual() instanceof NaveEstrellada)){
            return;
        }

        if (!moduloEncontrado && rand.nextDouble() < 0.25){
            System.out.println("¡Has encontrado el Módulo de Profundidad!");
            jugador.agregarItem(new Item(ItemTipo.MODULO_PROFUNDIDAD, 1));
            moduloEncontrado = true;
            return;
        }

        double d = ProfundidadNormalizada(jugador.getProfundidadActual());
        int n_min = 1;
        int Cantidad = Math.max(1, (int) Math.floor(n_min * d));

        if (Cantidad < 1){ 
            Cantidad = 1;
        }
     
        ItemTipo[] Recursos = { ItemTipo.Cables, ItemTipo.Piezas_Metal };
        ItemTipo Recurso = Recursos[rand.nextInt(Recursos.length)];

        jugador.agregarItem(new Item(Recurso, Cantidad));
        System.out.println("Has recolectado: " + Cantidad + " x " + Recurso + "\n");
    }

    /*
    * Reinicia el contador de acciones peligrosas realizadas sin traje térmico.
    * Se llama normalmente al cambiar de zona o reaparecer.
    * @param Ninguno
    * @return void
    */
    public void resetearAccionesSinTraje(){
        this.AccionesSinTraje = 0;
    }


   
}

