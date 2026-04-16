package objetos; 

import entorno.Zona;
import entorno.ZonaVolcanica;
import player.Jugador;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class NaveExploradora extends Vehiculo implements AccesoProfundidad{

    private int profundidadSoportada; 
    private ModuloProfundidad moduloInstalado;
    private int profundidadAnclaje;
    private Zona ZonaActual;
    private List<Item> inventarioNave = new ArrayList<>();


    /*
    * Determina si la nave puede acceder a cierta profundidad.
    * @param requerido: int - profundidad a verificar.
    * @return boolean - true si la nave puede acceder, false si excede su límite sin módulo.
    */
    @Override
    public boolean puedeAcceder(int requerido){
        if (moduloInstalado != null){
            return true;
        }
        // Sin módulo solo se permite hasta 500 m
        return requerido <= 500;
        
    }

    public class ModuloProfundidad{ 
        private int profundidadExtra; 
        
        /*
        * Constructor del módulo de profundidad.
        * Aumenta la profundidad soportada en 1000 metros al instalarse.
        * @param Ninguno
        * @return void
        */
        public ModuloProfundidad(){
            this.profundidadExtra = 1000;
        }
        /*
        * Incrementa la profundidad máxima soportada por la nave.
        * @param Ninguno
        * @return void
        */
        // Método diagrama
        public void aumentarProfundidad(){
            profundidadSoportada += this.profundidadExtra; 
        }
        // Getter
        /*
        * Devuelve la cantidad adicional de profundidad que aporta el módulo.
        * @param Ninguno
        * @return int - metros extra de profundidad.
        */
        public int getProfundidadExtra(){
            return profundidadExtra;
        }
    }

    /*
    * Constructor de la Nave Exploradora.
    * Inicializa el anclaje, la profundidad soportada y crea el robot asociado.
    * @param ZonaInicial: Zona - zona donde se ubica inicialmente la nave.
    * @return void
    */
    public NaveExploradora(Zona ZonaInicial){
        super();
        this.profundidadSoportada = 500;
        this.moduloInstalado = null;
        this.ZonaActual = ZonaInicial;
        this.profundidadAnclaje = ZonaInicial.getProfundidadMin();
        this.inventarioNave = new ArrayList<>();
        this.robot = new RobotExcavador(); 

    }
    /*
    * Instala un módulo de profundidad en la nave, aumentando su límite máximo.
    * @param modulo: ModuloProfundidad - módulo que se instalará.
    * @return void
    */
    // Método para instalar el módulo (llama al método del módulo)
    public void instalarModulo(ModuloProfundidad modulo){
        if (this.moduloInstalado == null){
            modulo.aumentarProfundidad(); 
            this.moduloInstalado = modulo;
            System.out.println("\n--- Módulo instalado. Profundidad soportada: " + this.profundidadSoportada + "m. ---\n");
        }
    }
    
    /*
    * Permite al jugador entrar a la nave.
    * Rellena el oxígeno y establece la profundidad de anclaje.
    * @param jugador: Jugador - jugador que ingresa a la nave.
    * @return void
    */
    public void entrar(Jugador jugador){
        jugador.setEnNave(true);
        jugador.getTanqueOxigeno().recargarCompleto();
        jugador.setProfundidadActual(profundidadAnclaje);
        System.out.println("\n === Nave Exploradora ==="); 
        System.out.println("El oxígeno ha sido recargado: " + jugador.getTanqueOxigeno().getOxigenoRestante() + " | Profundidad de anclaje actual: " + profundidadAnclaje + " m\n");
    }

    /*
    * Permite al jugador salir de la nave hacia el agua.
    * @param jugador: Jugador - jugador que sale de la nave.
    * @return void
    */
    public void salir(Jugador jugador){
        jugador.setEnNave(false);
        jugador.setProfundidadActual(profundidadAnclaje);
        System.out.println("\n=== Has salido de la Nave Exploradora a " + profundidadAnclaje + " m. ===\n");
    }

    /*
    * Mueve la nave a una nueva profundidad dentro del rango permitido de la zona actual.
    * @param nuevaProfundidad: int - nueva profundidad deseada.
    * @return void
    */
    public void moverNave(int nuevaProfundidad){
        if (ZonaActual == null){
            System.out.println("La nave no está asignada a ninguna zona.");
            return;
        }

        if (nuevaProfundidad < ZonaActual.getProfundidadMin() || nuevaProfundidad > ZonaActual.getProfundidadMax()){
            System.out.println("No se puede mover la nave fuera del rango permitido (" + ZonaActual.getProfundidadMin() + "-" + ZonaActual.getProfundidadMax() + " m).");
            return;
        }
        System.out.println("Moviendo nave desde " + profundidadAnclaje + " m a " + nuevaProfundidad + " m (sin costo de O2).");
        this.profundidadAnclaje = nuevaProfundidad;
    }

     /*
    * Muestra el menú principal de la nave con las opciones de gestión.
    * Permite crear objetos, mover la nave, almacenar recursos o salir.
    * @param jugador: Jugador - jugador actual.
    * @param Scan: Scanner - entrada de usuario.
    * @return void
    */
    public void MenuNave(Jugador jugador, Scanner Scan){
                
        boolean enNave = true;
        while (enNave) {
            System.out.println("=== Menú Nave Exploradora ===");
            System.out.println("1) Ajustar profundidad de nave (anclaje)");
            System.out.println("2) Crear objetos");
            System.out.println("3) Guardar TODOS los objetos del jugador en la nave");
            System.out.println("4) Moverse a otra zona ");
            System.out.println("5) Ver inventario de la nave");
            System.out.println("6) Salir de la nave (volver al agua en el anclaje)");
            System.out.print("> ");
            int opcionNave = Scan.nextInt();

            switch (opcionNave){
                case 1:
                    System.out.println("Ingresa la nueva profundidad de anclaje (" + this.getZonaActual().getProfundidadMin() + "-" + this.getZonaActual().getProfundidadMax() + " m): ");
                    int nuevoAnclaje = Scan.nextInt();

                    // Validar que esté dentro del rango
                    if (nuevoAnclaje < this.getZonaActual().getProfundidadMin() || nuevoAnclaje > this.getZonaActual().getProfundidadMax()){
                        System.out.println("\nProfundidad inválida para esta zona.");
                    } else if(nuevoAnclaje > 500 && this.getModuloInstalado() == null){
                       
                        System.out.println("\nNo puedes anclar la nave a más de 500m sin el módulo de profundidad.\n");
                    }else{
                        this.setProfundidadAnclaje(nuevoAnclaje);
                        System.out.println("\nNuevo anclaje establecido en " + nuevoAnclaje + " m.");
                    }
                    break;
                case 2:
                    CrearObjetos(jugador, Scan);
                    break;
                case 3:
                    transferirObjetos(jugador);
                    break;
                case 4:
                    Zona zonaActual = this.getZonaActual();
                    String nombreSiguiente = (zonaActual.getZonaSiguiente() != null) ? zonaActual.getZonaSiguiente().nombre : "No hay";
                    String nombreAnterior = (zonaActual.getZonaAnterior() != null) ? zonaActual.getZonaAnterior().nombre : "No hay";
                    
                    System.out.println("1) Ir a zona siguiente (" + nombreSiguiente + ")");
                    System.out.println("2) Volver a zona anterior (" + nombreAnterior + ")");
                    System.out.print("> ");
                    int Opcion = Scan.nextInt();
           
                    if (Opcion == 1){
                        Zona siguienteZona = jugador.getZonaActual().getZonaSiguiente();        
                            if(siguienteZona != null){
                                int minProf = siguienteZona.getProfundidadMin();
                                    if (jugador.puedeAcceder(minProf) || this.puedeAcceder(minProf)){
                                        jugador.setZonaActual(siguienteZona);
                                        this.setZonaActual(siguienteZona);
                                        this.setProfundidadAnclaje(minProf);
                                        jugador.setProfundidadActual(minProf);
                                        System.out.println("nViajando a " + siguienteZona.nombre + " ...");
                                        System.out.println("=== Has llegado a " + siguienteZona.nombre + "( anclaje:"+ minProf + "). ===\n");
                                    }else{
                                        System.out.println("No puedes acceder a esta zona aún (profundidad mínima " + minProf + " m).\n");
                                    }
                            }else{
                                System.out.println("No hay zona siguiente.");
                            }
                    }else if(Opcion == 2){
                        Zona anteriorZona = jugador.getZonaActual().getZonaAnterior();
                            if (anteriorZona != null){
                                int minProf = anteriorZona.getProfundidadMin();
                                jugador.setZonaActual(anteriorZona);
                                this.setZonaActual(anteriorZona);
                                this.setProfundidadAnclaje(minProf);
                                jugador.setProfundidadActual(minProf);
                                System.out.println("\nRegresando a " + anteriorZona.nombre + "...");
                                System.out.println("=== Has vuelto a " + anteriorZona.nombre + " (anclaje:" + minProf + "). ===\n");
                            }else{
                                System.out.println("No hay zona anterior.");
                            }
                    }else{
                        System.out.println("Opción inválida. Intenta de nuevo.");
                    }
                    break;
                case 5:
                    System.out.println("\n=== Inventario de la Nave ===");
                    if (this.getBodega().isEmpty()){
                        System.out.println("La nave no tiene recursos guardados.\n");
                    }else{
                        for (Item item : this.getBodega()){
                            System.out.println("- " + item.getTipo() + " x" + item.getCantidad());
                        }
                        System.out.print("¿Deseas retirar algún objeto? (s/n): ");
                        String respuesta = Scan.next().toLowerCase();

                        if (respuesta.equals("s")){
                            this.retirarObjetos(jugador, Scan);
                        }else{
                            System.out.println("\nVolviendo al menú principal...\n");
                        }   
                    }
                        break;
                case 6:

                    if (this.getZonaActual() instanceof ZonaVolcanica){
                        if (!jugador.getMejoraTanque() && !jugador.isTrajeTermico()){
                            System.out.println("No puedes salir de la nave: Necesitas el tanque mejorado y el traje térmico antes de intentar salira esta zona");
                            break;
                        } else if(!jugador.getMejoraTanque()){
                            System.out.println("No puedes salir aún: Necesitas instalar la mejora del tanque antes de salir a esta zona.");
                        }else if(!jugador.isTrajeTermico()){
                            System.out.println("No puedes salir aún: Necesitas el traje térmico antes de salir a esta zona.");
                            break;
                        }
                    }

                    this.salir(jugador);
                    jugador.setEnNave(false);
                    enNave = false;
                    break;
                default:
            }
        }
        if (Scan.hasNextLine()){
            Scan.nextLine(); //Limpia linea por si acasoo
        }
    }

    /*
    * Permite al jugador crear o mejorar objetos desde la nave.
    * Incluye tanque, módulo, traje térmico, robot y mejora de oxígeno.
    * @param jugador: Jugador - jugador que realiza la creación.
    * @param scan: Scanner - entrada del usuario.
    * @return void
    */
    public void CrearObjetos(Jugador jugador, Scanner scan){
        System.out.println("\n=== Mesa de Crafteo ===");

        int PiezasTanque = 0;
        int Cuarzo = 0, Silicio = 0, Cobre =0, Plata = 0, Oro = 0, Acero = 0, Diamante = 0, Magnetita = 0;
        boolean puedeInstalarModulo = false;

        for (Item item : jugador.getInventario()){
            switch (item.getTipo()) {
                case PIEZA_TANQUE:
                    PiezasTanque = item.getCantidad();
                    break;
                case MODULO_PROFUNDIDAD:
                    puedeInstalarModulo = true;
                    break;
                case Cuarzo:
                    Cuarzo = item.getCantidad();
                    break;
                case Silicio: 
                    Silicio = item.getCantidad();
                    break;
                case Cobre:
                    Cobre = item.getCantidad();
                case Plata: 
                    Plata = item.getCantidad();
                    break;
                case Oro: 
                    Oro = item.getCantidad();
                    break;
                case Acero:
                    Acero = item.getCantidad();
                    break;
                case Diamante:
                    Diamante = item.getCantidad();
                    break;
                case Magnetita:
                    Magnetita = item.getCantidad();
                    break;
                default:
                    break;
            }
        }


        System.out.println("1) Mejorar tanque (requiere 3 Piezas Tanque)");
        System.out.println("2) Instalar Módulo Profundidad en la nave");
        System.out.println("3) Crear traje térmico");
        System.out.println("4) Crear Robot");
        
        if (jugador.getMejoraTanque()){
            System.out.println("5) Mejorar capacidad de oxígeno (+30)");
        }
    
            
        System.out.println("0) Cancelar");
        System.out.print("> ");
        int Opcion = scan.nextInt();

        switch (Opcion){
            case 1:
                if (jugador.getMejoraTanque()){
                    System.out.println("\n--- Ya tienes instalada la mejora del tanque. ---\n");
                }else if( PiezasTanque == 3){
                    mejorarTanque(jugador);
                }else{
                    System.out.println("\nNo tienes Piezas Tanque suficientes para poder mejorar el tanque\n");
                }
                break;
            case 2:
                if (this.getModuloInstalado() != null){
                    System.out.println("\n--- Ya tienes instalado el módulo de profundidad. ---\n");
                }else if( !puedeInstalarModulo){
                    System.out.println("\nNo tienes el módulo profundidad en el inventario para poder mejorar la Nave Exploradora\n");
                }else{
                    ModuloProfundidad modulo = new ModuloProfundidad();
                    this.instalarModulo(modulo);
                    jugador.getInventario().removeIf(i -> i.getTipo() == ItemTipo.MODULO_PROFUNDIDAD);
                }
                break;

            case 3:
                if (jugador.isTrajeTermico()){ 
                    System.out.println("\n--- Ya posees un traje térmico. ---\n");
                }else if(Silicio >= 10 && Oro >= 3 && Cuarzo >= 5){
                    jugador.setTrajeTermico(true);
                    jugador.removerItem(ItemTipo.Silicio, 10);
                    jugador.removerItem(ItemTipo.Oro, 3);
                    jugador.removerItem(ItemTipo.Cuarzo, 5);
                    System.out.println("\nHas creado un traje térmico. ¡Ahora podrás resistir el calor!\n");
                }else{
                    System.out.println("\nNo tienes suficientes recursos para crear el traje térmico.\n");
                }
                break;
            case 4:
                if (getRobot().isCreado()){
                    System.out.println("\n--- Ya has creado el Robot Excavador. ----\n");
                } else if(Cobre >= 15 && Magnetita >= 10 && Diamante >=5 && Acero>= 20){
                    getRobot().setCreado(true);
                    jugador.removerItem(ItemTipo.Cobre, 15);
                    jugador.removerItem(ItemTipo.Magnetita, 10);
                    jugador.removerItem(ItemTipo.Diamante, 5);
                    jugador.removerItem(ItemTipo.Acero, 20);
                    System.out.println("\nHas construido el Robot Excavador. Capacidad de carga: 1000 unidades.\n");
                }else{
                    System.out.println("\nNo tienes suficientes recursos para crear el robot.\n");
                }
                break;
                
            case 5:
                if (!jugador.getMejoraTanque()){
                    System.out.println("\nPrimero debes mejorar el tanque para poder aumentar su capacidad.\n");
                }else if(Plata >= 10 && Cuarzo >= 15){
                    jugador.getTanqueOxigeno().aumentarCapacidad(30);
                    jugador.removerItem(ItemTipo.Plata, 10);
                    jugador.removerItem(ItemTipo.Cuarzo, 15);
                    System.out.println("\nHas mejorado tu tanque de oxígeno (+30 capacidad).\n");
                }else{
                    System.out.println("\nNo tienes suficientes recursos para aumentar la capacidad.\n");
                }   
                break;
                
            case 0:
                System.out.println("Creación cancelada.\n");
                break;
            default:
                System.out.println("Opción inválida. Intenta de nuevo.");
                break;
        }

    }

    /*
    * Permite al jugador retirar objetos almacenados en la nave.
    * @param jugador: Jugador - jugador que recibe los objetos.
    * @param scan: Scanner - entrada de usuario.
    * @return void
    */
    public void retirarObjetos(Jugador jugador, Scanner scan){
        System.out.println("=== Inventario de la Nave ===");
        for (int i = 0; i < getBodega().size(); i++) {
            Item item = getBodega().get(i);
            System.out.println((i + 1) + ") " + item.getTipo() + " x" + item.getCantidad());
        }

        System.out.print("Selecciona el número del objeto a retirar (0 para cancelar): ");

        String entrada = scan.next();
        if (!entrada.matches("\\d+")) {  // expresión regular: solo dígitos
            System.out.println("Opción inválida. Debes ingresar un número.");
            return; 
        }

        int Opcion = Integer.parseInt(entrada);
        if (Opcion == 0 || Opcion > getBodega().size()){
            return;
        } 

        Item seleccionado = getBodega().get(Opcion - 1);
        System.out.print("¿Cuántas unidades deseas retirar? (máx " + seleccionado.getCantidad() + "): ");
        int cantidad = scan.nextInt();

        if (cantidad <= 0 || cantidad > seleccionado.getCantidad()){
            System.out.println("Cantidad inválida.");
            return;
        }

        jugador.agregarItem(new Item(seleccionado.getTipo(), cantidad));
        seleccionado.setCantidad(seleccionado.getCantidad() - cantidad);
        if (seleccionado.getCantidad() == 0){
            getBodega().remove(seleccionado);
        }
        System.out.println("Has retirado " + cantidad + " x " + seleccionado.getTipo() + " del inventario.");
    
    }  

    /*
    * Mejora el tanque del jugador duplicando su capacidad de oxígeno.
    * @param jugador: Jugador - jugador que mejora su tanque.
    * @return void
    */
    private void mejorarTanque(Jugador jugador){
        for (Item item : jugador.getInventario()) {
            if (item.getTipo() == ItemTipo.PIEZA_TANQUE){
                item.setCantidad(item.getCantidad() - 3);
                if (item.getCantidad() <= 0) {
                    jugador.getInventario().remove(item);
                }
                break;
            }
        }

        jugador.setMejoraTanque(true);
        jugador.getTanqueOxigeno().duplicarCapacidad();

        System.out.println("\n¡Has mejorado tu tanque!");
        System.out.println("La capacidad del oxígeno fue duplicada y la presión es anulada en zonas profundas y volcánicas.\n");
    }

    //Getter
    /*
    * Obtiene la profundidad máxima actualmente soportada por la nave.
    * @param Ninguno
    * @return int - profundidad soportada en metros.
    */
    public int getProfundidadSoportada(){ 
        return profundidadSoportada; 
    }
    /*
    * Obtiene la profundidad actual de anclaje de la nave.
    * @param Ninguno
    * @return int - profundidad de anclaje en metros.
    */
    public int getProfundidadAnclaje(){
        return profundidadAnclaje;
    }
    /*
    * Obtiene la zona en la que actualmente se encuentra la nave.
    * @param Ninguno
    * @return Zona - referencia a la zona actual.
    */
    public Zona getZonaActual(){
        return ZonaActual;
    }
    // Getter para el módulo instalado
    /*
    * Obtiene el módulo de profundidad instalado en la nave (si existe).
    * @param Ninguno
    * @return ModuloProfundidad - módulo instalado o null si no hay.
    */
    public ModuloProfundidad getModuloInstalado(){
        return moduloInstalado;
    }
    /*
    * Obtiene el inventario interno de la nave (bodega propia de la nave).
    * @param Ninguno
    * @return List<Item> - lista de ítems almacenados en la nave.
    */
    public List<Item> getInventarioNave(){
        return inventarioNave;
    }

    //Setter
    /*
    * Establece la zona actual donde se ubica la nave.
    * @param Zona: Zona - nueva zona a asignar.
    * @return void
    */
    public void setZonaActual(Zona Zona){
        this.ZonaActual = Zona;
    }
    /*
    * Define la profundidad de anclaje de la nave.
    * @param profundidad: int - nueva profundidad de anclaje en metros.
    * @return void
    */
    public void setProfundidadAnclaje(int profundidad){
        this.profundidadAnclaje = profundidad;
    } 
}


