package objetos;

import player.Jugador;
import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import java.util.Scanner;


public class RobotExcavador extends Vehiculo{

    private int capacidadCarga;
    private boolean necesitaReparacion; 
    private boolean creado;   

    /*
    * Constructor del RobotExcavador.
    * Inicializa la capacidad de carga, el estado de reparación y el indicador de creación.
    * @param Ninguno
    * @return void
    */
    public RobotExcavador(){
        this.capacidadCarga = 1000; 
        this.necesitaReparacion = false;
        this.creado = false;
    }

    /*
    * Transfiere todos los objetos almacenados en la bodega del robot hacia la nave.
    * @param nave: NaveExploradora - instancia de la nave que recibirá los objetos.
    * @return void
    */
    public void transferirObjetos(NaveExploradora nave){
        for (Item item : getBodega()) {
            nave.agregarItem(item);
        }
        getBodega().clear();
    }

    /*
    * Permite al robot recolectar recursos según la zona actual del jugador.
    * Calcula la cantidad recolectada, agrega los ítems al inventario del robot y verifica sobrecarga.
    * @param jugador: Jugador - jugador que controla al robot.
    * @param scan: Scanner - objeto para capturar la entrada del usuario.
    * @return void
    */
    public void recolectar(Jugador jugador, Scanner scan){

        if (necesitaReparacion){
            System.out.println("El robot necesita reparaciones antes de volver a recolectar.");
            return;
        }

        System.out.println("El robot comienza a recolectar recursos...");
        Zona zonaActual = jugador.getZonaActual();
        ItemTipo[] recursos;
        int n_min, n_max;
        
        if ( zonaActual instanceof ZonaArrecife ){
            recursos = new ItemTipo[]{ ItemTipo.Cuarzo, ItemTipo.Silicio, ItemTipo.Cobre };
            n_min = 1;
            n_max = 3;
        } else if (zonaActual instanceof ZonaProfunda){
            recursos = new ItemTipo[]{ ItemTipo.Plata, ItemTipo.Oro, ItemTipo.Acero, ItemTipo.Diamante, ItemTipo.Magnetita };
            n_min = 2;
            n_max = 6;
        } else if (zonaActual instanceof ZonaVolcanica){
            recursos = new ItemTipo[]{ ItemTipo.Titanio, ItemTipo.Sulfuro, ItemTipo.Uranio };
            n_min = 3;
            n_max = 8;
        }else{ 
           recursos = new ItemTipo[]{ ItemTipo.Piezas_Metal, ItemTipo.Cables};
            n_min = 3;
            n_max = 8; 
        }
    
        
        for (int i = 0; i < recursos.length; i++){
            System.out.println((i + 1) + ") " + recursos[i]);
        }
        System.out.print("> ");
        int Opcion = scan.nextInt();

        if (Opcion < 1 || Opcion > recursos.length){
            System.out.println("Opción inválida. Cancelando recolección.");
            return;
    
        }
        ItemTipo recursoElegido = recursos[Opcion - 1];
        
        double d = zonaActual.ProfundidadNormalizada(jugador.getProfundidadActual());
        int cantidad = Math.max(1, (int) Math.floor(n_min + (n_max - n_min) * d));

        agregarItem(new Item(recursoElegido, cantidad));
        System.out.println("El robot ha recolectado: " + cantidad + " x " + recursoElegido);

        // Comprobar sobrecarga
        int cargaAcumulada = this.getBodega().stream().mapToInt(Item::getCantidad).sum();

        if (cargaAcumulada > capacidadCarga){
            necesitaReparacion = true;
            System.out.println("El robot ha sobrepasado su capacidad de carga (" + cargaAcumulada + "/" + capacidadCarga + ")");
            System.out.println("Se necesita reparar para poder recolectar nuevamente.");
        }else{
            System.out.println("El robot completó su recolección sin problemas.");
        }
    }

    /*
    * Verifica si el robot ha superado su capacidad de carga y establece su estado como dañado si corresponde.
    * @param cargaActual: int - carga total actual en la bodega del robot.
    * @return void
    */
    public void verificarSobrecarga(int cargaActual){
        if (cargaActual > capacidadCarga){
            necesitaReparacion = true;
            System.out.println("El robot ha superado su límite de carga y necesita reparación.");
        }
    }

    /*
    * Muestra el menú principal de control del robot, permitiendo al jugador realizar distintas acciones:
    * recolectar, transferir objetos, ver inventario, mejorar o reparar.
    * @param nave: NaveExploradora - nave asociada al jugador.
    * @param jugador: Jugador - jugador que utiliza el robot.
    * @param scan: Scanner - para capturar las opciones del jugador.
    * @return void
    */
    public void menuRobot(NaveExploradora nave, Jugador jugador, Scanner scan){
        boolean salir = false;

        while (!salir) {
            System.out.println("=== Robot Excavador ===");
            System.out.println("Capacidad de carga: " + capacidadCarga + " unidades");
            System.out.println("Estado: " + (necesitaReparacion ? " Necesita reparación" : "Operativo"));
            System.out.println("Inventario: " + (getBodega().isEmpty() ? "Vacío" : getBodega().size() + " tipos de recursos"));
            System.out.println("1) Recolectar recursos");
            System.out.println("2) Transferir objetos a la nave");
            System.out.println("3) Ver inventario del robot");
            System.out.println("4) Mejorar capacidad de carga");
            System.out.println("5) Reparar robot");
            System.out.println("0) Dejar de usar el robot");
            System.out.print("> ");
            int Opcion = scan.nextInt();


            int Titanio = 0, Magnetita = 0, Cuarzo = 0;
            int Cables = 0, Piezas_Metal = 0;
            for (Item item : jugador.getInventario()){
                switch (item.getTipo()){
                    case Cuarzo:
                        Cuarzo = item.getCantidad();
                        break;
                    case Magnetita:
                        Magnetita = item.getCantidad();
                        break;
                    case Titanio:
                        Titanio = item.getCantidad();
                        break;
                    default:
                        break;
                }
            }
            switch (Opcion){
                case 1:
                    if (necesitaReparacion){
                        System.out.println("El robot necesita reparase para poder seguir recolectando.");
                    }else{
                        recolectar(jugador, scan);
                    }
                    break;

                case 2:
                    if (getBodega().isEmpty()){
                        System.out.println("El inventario está vacío.");
                    }else{
                        transferirObjetos(nave);
                        System.out.println("Recursos transferidos a la nave.");
                    }
                    break;

                case 3:
                    if (getBodega().isEmpty()){
                        System.out.println("El inventario del robot está vacío.");
                    }else{
                        System.out.println("=== Inventario del Robot ===");
                        for (Item item : getBodega()) {
                            System.out.println("- " + item.getTipo() + " x" + item.getCantidad());
                        }
                    }
                    break;
                case 4:
                
                    if (Titanio >= 10 && Cuarzo >= 20){
                        capacidadCarga += (int)(capacidadCarga * 0.25);
                        System.out.println("Capacidad de carga aumentada en 25%. Nueva capacidad: " + capacidadCarga);
                        jugador.removerItem(ItemTipo.Titanio, 10);
                        jugador.removerItem(ItemTipo.Cuarzo, 20);
                    }else{
                        System.out.println("No tienes los recursos suficientes para mejorar la capacidad del robot.");
                    }
                    break;
                case 5:
                    if (!necesitaReparacion){
                        System.out.println("El robot no necesita reparación.");
                        return;
                    }

                    if (Cables >= 4 && Piezas_Metal >= 3 && Magnetita >= 5){
                        jugador.removerItem(ItemTipo.Cables, 4);
                        jugador.removerItem(ItemTipo.Piezas_Metal, 3);
                        jugador.removerItem(ItemTipo.Magnetita, 5);
                    
                        necesitaReparacion = false;
                        System.out.println("El Robot Excavador fue reparado exitosamente."); 
                    }else{
                        System.out.println("No tienes los recursos suficientes para reparar el robot.");
                    }   
                    break;
                case 0:
                    System.out.println("Has dejado de usar el robot.");
                    salir = true;
                    break;

                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
                    break;
            }
        }
    }


    /*
    * Devuelve la capacidad máxima de carga del robot.
    * @param Ninguno
    * @return int - capacidad de carga en unidades.
    */
    public int getCapacidadCarga(){
        return capacidadCarga;
    }

    /*
    * Indica si el robot necesita reparaciones.
    * @param Ninguno
    * @return boolean - true si requiere reparación, false si está operativo.
    */
    public boolean NecesitaReparacion(){ 
        return necesitaReparacion; 
    }

    /*
    * Indica si el robot ha sido creado y está disponible para su uso.
    * @param Ninguno
    * @return boolean - true si fue creado, false si no.
    */
    public boolean isCreado(){ 
        return creado; 
    }

    //Setter

     /*
    * Establece una nueva capacidad de carga para el robot.
    * @param capacidadCarga: int - nueva capacidad a asignar.
    * @return void
    */
    public void setCapacidadCarga(int capacidadCarga){
        this.capacidadCarga = capacidadCarga;
    }

    /*
    * Cambia el estado de reparación del robot.
    * @param estado: boolean - true si necesita reparación, false si está reparado.
    * @return void
    */
    public void setNecesitaReparacion(boolean estado){ 
        this.necesitaReparacion = estado;
    }

     /*
    * Define si el robot ha sido creado.
    * @param creado: boolean - true si fue creado, false en caso contrario.
    * @return void
    */
    public void setCreado(boolean creado){ 
        this.creado = creado; 
    }
}