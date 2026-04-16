package objetos;

import player.Jugador;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase abstracta base para los vehículos.
 */
public abstract class Vehiculo{
    
    private List<Item> bodega;
    protected RobotExcavador robot;

    /*
    * Constructor por defecto del vehículo.
    * Inicializa la bodega como una lista vacía y el robot como nulo.
    * @param Ninguno
    * @return void
    */
    public Vehiculo(){
        this.bodega = new ArrayList<>();
        this.robot = null;
    }

    /*
    * Transfiere todos los objetos del inventario del jugador a la bodega del vehículo.
    * Si el jugador no tiene recursos, muestra un mensaje y no realiza acción.
    * @param jugador: Jugador - jugador que transfiere sus recursos al vehículo.
    * @return void
    */
    public void transferirObjetos(Jugador jugador){
        if (jugador.getInventario().isEmpty()){
            System.out.println("\nNo tienes recursos que guardar.\n");
            return;
        }
        for (Item itemJugador : jugador.getInventario()){
            agregarItem(itemJugador);
        }
        jugador.getInventario().clear();
        System.out.println("\nTodos tus recursos han sido guardados en el inventario de la Nave Exploradora.\n");
    }

    /*
    * Agrega un ítem a la bodega del vehículo. Si el tipo ya existe, incrementa su cantidad.
    * Este método se reutiliza también para retirar o recibir objetos.
    * @param nuevo: Item - ítem que se agregará a la bodega.
    * @return void
    */
    protected void agregarItem(Item nuevo){
        for (Item item : bodega) {
            if (item.getTipo() == nuevo.getTipo()) {
                item.setCantidad(item.getCantidad() + nuevo.getCantidad());
                return;
            }
        }
        bodega.add(new Item(nuevo.getTipo(), nuevo.getCantidad()));
    }

    //Getter
    /*
    * Retorna la lista completa de ítems almacenados en la bodega del vehículo.
    * @param Ninguno
    * @return List<Item> - lista de ítems de la bodega.
    */
    public List<Item> getBodega(){ 
        return bodega; 
    }

    /*
    * Devuelve el robot excavador asociado al vehículo.
    * @param Ninguno
    * @return RobotExcavador - instancia actual del robot o null si no existe.
    */
    public RobotExcavador getRobot(){
        return robot;
    }

    
    /*
    * Asigna un nuevo robot excavador al vehículo.
    * @param robot: RobotExcavador - robot a asociar con el vehículo.
    * @return void
    */
    public void setRobot(RobotExcavador robot){
        this.robot = robot;
    }

}