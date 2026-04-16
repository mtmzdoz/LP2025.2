package player;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import objetos.Item;
import objetos.ItemTipo;
import objetos.AccesoProfundidad;
import objetos.NaveExploradora;
import entorno.NaveEstrellada;
import entorno.Zona;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;

public class Jugador implements AccesoProfundidad{
    
    //Del diagrama
    private Oxigeno tanqueOxigeno;             
    private List<Item> inventario;             
    private Zona zonaActual;                   
    private int profundidadActual; 
    private boolean tienePlanos;            
    private NaveExploradora nave;                   
    private boolean trajeTermico;              
    private boolean mejoraTanque;   
    private boolean enNave = false;           

    /*
    * Constructor del jugador. Inicializa los valores base de oxígeno, inventario, zona, nave y mejoras posibñes.
    * @param naveInicial: NaveExploradora - la nave con la que comienza el jugador.
    * @param zonaInicial: Zona - la zona inicial donde se ubica el jugador.
    * @return void
    */
    public Jugador(NaveExploradora naveInicial, Zona zonaInicial){
        this.tanqueOxigeno = new Oxigeno();
        this.inventario = new ArrayList<>();
        this.nave = naveInicial;
        this.zonaActual = zonaInicial;
        this.profundidadActual = 0; 
        this.tienePlanos = false;
        this.trajeTermico = false;
        this.mejoraTanque = false;
    }

    /*
    * Determina si el jugador puede acceder a una profundidad específica 
    * @param requerido: int - la profundidad requerida o destino.
    * @return boolean - true si puede acceder, false si no cumple los requisitos.
    */
    @Override
    public boolean puedeAcceder(int requerido){
        Zona zona = this.zonaActual;
        if (zona instanceof ZonaVolcanica && !trajeTermico){
            System.out.println("No puedes acceder a la Zona Volcánica sin el traje térmico.");
            return false;
        }
    
        return true;
    }

    /*
    * Muestra el estado actual del jugador en consola: zona, oxígeno y profundidad.
    * @param Ninguno
    * @return void
    */
    public void verEstadoJugador(){
        System.out.println("Zona Actual: " + zonaActual.nombre + " | O2: " + tanqueOxigeno.getOxigenoRestante() + " | Profundidad: " + profundidadActual + "m");
    }

    /*
    * Permite al jugador nadar hacia una nueva profundidad dentro de la zona actual.
    * Consume oxígeno proporcional a la distancia y presión.
    * @param delta: int - diferencia entre la profundidad actual y la deseada.
    * @return void
    */
    public void nadar(int delta){
        Zona zona = this.getZonaActual();
        int profundidadInicial = this.profundidadActual;
        int destino = this.profundidadActual + delta;

        if (destino < zona.getProfundidadMin() || destino > zona.getProfundidadMax()){
            System.out.println("No puedes nadar fuera del rango de esta zona (" + zona.getProfundidadMin() + " - " + zona.getProfundidadMax() + " m).");
            return;
        }

        double d = zona.ProfundidadNormalizada(profundidadInicial);
        int distanciaRecorrida = Math.abs(destino - profundidadInicial);

        double presion = 0;
        if (!this.mejoraTanque){
            if (zona instanceof ZonaProfunda){
                presion = ((ZonaProfunda) zona).calcularPresion(this); // devuelve 10 + 6*d
            }else if (zona instanceof ZonaVolcanica){
                System.out.println("No se puede descender a la Zona Volcánica sin el módulo de profundidad");
                return;
            }
        }
        // Consumo de O2
        int Cmover = (int) Math.ceil((3 + 3*d) * Math.abs(distanciaRecorrida) / 50.0 );
        this.tanqueOxigeno.consumirO2(Cmover);

        if (this.getTanqueOxigeno().getOxigenoRestante() <= 0){
            this.derrotaPorOxigeno();
            return;
        }

        this.setProfundidadActual(destino);
        System.out.println("\nTe moviste a " + destino + " m. Presión:" + presion + " | Oxígeno consumido: " + Cmover + ". Oxígeno restante: " + this.tanqueOxigeno.getOxigenoRestante()+"\n");
    }
    
    /*
    * Agrega un nuevo ítem al inventario del jugador o incrementa su cantidad si ya existe.
    * @param nuevo: Item - el ítem a agregar.
    * @return void
    */
    public void agregarItem(Item nuevo){
        if (nuevo == null){
            return;
        }
        for (Item item: inventario){
            if (item.getTipo() == nuevo.getTipo()){
                item.setCantidad(item.getCantidad() + nuevo.getCantidad());
                System.out.println("Sumado: " + nuevo.getCantidad() + " x " + item.getTipo() + " (total: " + item.getCantidad() + ")\n");
                return;
            }
        }
        inventario.add(nuevo);
        System.out.println("Agregado: " + nuevo.getCantidad() + " x " + nuevo.getTipo() );
    }

    /*
    * Elimina una cantidad específica de un tipo de ítem del inventario. Si la cantidad llega a cero, se remueve completamente.
    * @param tipo: ItemTipo - tipo de ítem a remover.
    * @param cantidad: int - cantidad a eliminar.
    * @return void
    */
    public void removerItem(ItemTipo tipo, int cantidad){
        for (int i = 0; i < inventario.size(); i++){
            Item item = inventario.get(i);
            if (item.getTipo() == tipo){
                item.setCantidad(item.getCantidad() - cantidad);
                if (item.getCantidad() <= 0) inventario.remove(i);{
                    return;
                }
            }
        }
    }

    /*
    * Muestra en consola todos los ítems actuales del inventario del jugador.
    * @param Ninguno
    * @return void
    */
    public void verInventario(){
        if (inventario.isEmpty()){
            System.out.println("\n--- Tu inventario está vacío. --- \n");
            return;
        }

        System.out.println("\n=== Inventario del jugador ===");
        for (Item item : inventario){
            System.out.println("- " + item.getTipo() + " x" + item.getCantidad());
        }
        System.out.println();
    }

    /*
    * Maneja la derrota del jugador por falta de oxígeno, reiniciando su estado y posición.
    * @param Ninguno
    * @return void
    */
    public void derrotaPorOxigeno(){
        System.out.println("¡Te has quedado sin oxígeno!");
        System.out.println("Pierdes todo tu inventario y reapareces en la Nave Exploradora...");
        inventario.removeIf(item -> item.getTipo() != ItemTipo.PIEZA_TANQUE  && item.getTipo() != ItemTipo.MODULO_PROFUNDIDAD && item.getTipo() != ItemTipo.PLANO_NAVE);

        // Reaparecer en la nave
        this.zonaActual = nave.getZonaActual(); 
        this.profundidadActual = nave.getProfundidadAnclaje();
        this.tanqueOxigeno.recargarCompleto();
        this.enNave = true;
        nave.entrar(this);
        nave.MenuNave(this, new Scanner(System.in));
        
    }

    /*
    * Maneja la derrota del jugador por sofocación dentro de la nave.
    * @param Ninguno
    * @return void
    */
    public void derrotaPorSofocacion(){
        System.out.println("¡Has sucumbido al calor dentro de la nave!");
        System.out.println("Pierdes todo tu inventario y reapareces en la Nave Exploradora...");
        inventario.removeIf(item -> item.getTipo() != ItemTipo.PIEZA_TANQUE  && item.getTipo() != ItemTipo.MODULO_PROFUNDIDAD && item.getTipo() != ItemTipo.PLANO_NAVE);

        this.zonaActual = nave.getZonaActual(); 
        this.profundidadActual = nave.getProfundidadAnclaje();
        this.tanqueOxigeno.recargarCompleto();
        this.enNave = true;

        if (zonaActual instanceof NaveEstrellada naveEstrellada) {
            naveEstrellada.resetearAccionesSinTraje();
        }
        nave.entrar(this);
        nave.MenuNave(this, new Scanner(System.in));
    }

    /*
    * Maneja el evento donde el jugador pierde la consciencia por calor extremo.
    * Reinicia su estado y posición.
    * @param Ninguno
    * @return void
    */
    public void perdidaConciencia(){
        System.out.println("El calor te abruman... ¡pierdes la consciencia!");
        System.out.println("Despiertas nuevamente en la Nave Exploradora... pero has perdido tu inventario.");
        inventario.removeIf(item -> item.getTipo() != ItemTipo.PIEZA_TANQUE  && item.getTipo() != ItemTipo.MODULO_PROFUNDIDAD && item.getTipo() != ItemTipo.PLANO_NAVE);

        this.zonaActual = nave.getZonaActual(); 
        this.profundidadActual = nave.getProfundidadAnclaje();
        this.tanqueOxigeno.recargarCompleto();
        this.enNave = true;
        nave.entrar(this);
        nave.MenuNave(this, new Scanner(System.in));
    }

    // Getter
    /*
    * Retorna el tanque de oxígeno del jugador.
    * @param Ninguno
    * @return Oxigeno - instancia del tanque de oxígeno.
    */
    public Oxigeno getTanqueOxigeno(){
        return tanqueOxigeno; 
    }

    /*
    * Indica si el jugador posee la mejora del tanque de oxígeno.
    * @param Ninguno
    * @return boolean - true si tiene la mejora, false en caso contrario.
    */
    public boolean getMejoraTanque(){
        return mejoraTanque; 
    }

    /*
    * Obtiene la zona actual donde se encuentra el jugador.
    * @param Ninguno
    * @return Zona - zona actual.
    */
    public Zona getZonaActual(){
        return zonaActual;
    }

     /*
    * Devuelve la profundidad actual del jugador.
    * @param Ninguno
    * @return int - profundidad actual en metros.
    */
    public int getProfundidadActual(){
        return profundidadActual; 
    }

     /*
    * Retorna la nave exploradora asociada al jugador.
    * @param Ninguno
    * @return NaveExploradora - nave actual del jugador.
    */
    public NaveExploradora getNave(){ 
        return nave; 
    }

    /*
    * Indica si el jugador se encuentra actualmente dentro de la nave.
    * @param Ninguno
    * @return boolean - true si está en la nave, false si está fuera.
    */
    public boolean isEnNave(){
        return enNave;
    }

    /*
    * Retorna el inventario completo del jugador.
    * @param Ninguno
    * @return List<Item> - lista de ítems del jugador.
    */
    public List<Item> getInventario(){
        return inventario;
    }

    /*
    * Indica si el jugador tiene equipado el traje térmico.
    * @param Ninguno
    * @return boolean - true si posee el traje térmico, false si no.
    */
    public boolean isTrajeTermico(){
        return trajeTermico;
    }

    /*
    * Indica si el jugador posee los planos de la nave estrellada.
    * @param Ninguno
    * @return boolean - true si tiene los planos, false si no.
    */
    public boolean getTienePlanos(){
        return tienePlanos;
    }

    //Setter
    /*
    * Asigna una nueva zona al jugador. Si la zona es una nave estrellada, resetea sus acciones sin traje.
    * @param zonaActual: Zona - nueva zona a establecer.
    * @return void
    */
    public void setZonaActual(Zona zonaActual){ 
        this.zonaActual = zonaActual;
        //Desde o A nave estrellada resetamos por si acaso
        if (zonaActual instanceof NaveEstrellada naveEstrellada){
            naveEstrellada.resetearAccionesSinTraje();
        }
    }

    /*
    * Define la nueva profundidad actual del jugador.
    * @param profundidadActual: int - nueva profundidad.
    * @return void
    */
    public void setProfundidadActual(int profundidadActual){ 
        this.profundidadActual = profundidadActual; 
    }

    /*
    * Cambia el estado de si el jugador está o no dentro de la nave.
    * @param enNave: boolean - true si está en la nave, false si está fuera.
    * @return void
    */
    public void setEnNave(boolean enNave) {
        this.enNave = enNave;
    }

    /*
    * Establece un nuevo tanque de oxígeno para el jugador.
    * @param nuevoTanque: Oxigeno - tanque a asignar.
    * @return void
    */
    public void setTanqueOxigeno(Oxigeno nuevoTanque){
        this.tanqueOxigeno = nuevoTanque;
    }

    /*
    * Activa o desactiva la mejora del tanque de oxígeno.
    * @param mejoraTanque: boolean - true para activar, false para desactivar.
    * @return void
    */
    public void setMejoraTanque(boolean mejoraTanque){
        this.mejoraTanque = mejoraTanque;
    }

    /*
    * Activa o desactiva el uso del traje térmico del jugador.
    * @param trajeTermico: boolean - true si el jugador tiene el traje térmico.
    * @return void
    */
    public void setTrajeTermico(boolean trajeTermico){
        this.trajeTermico = trajeTermico;
    }

     /*
    * Define si el jugador posee los planos de la nave.
    * @param tienePlanos: boolean - true si los posee, false si no.
    * @return void
    */
    public void setTienePlanos(boolean tienePlanos){
        this.tienePlanos = tienePlanos;
    }
}