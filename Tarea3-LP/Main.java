import player.Jugador;
import objetos.Item;
import objetos.ItemTipo;
import objetos.NaveExploradora;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import entorno.NaveEstrellada;
import java.util.Scanner;

public class Main{
    /*
    * Programa principal.
    * Inicia el juego, crea las zonas, el jugador, la nave y controla el flujo del menú principal.
    * @param args: String[] - argumentos de línea de comando (no utilizados).
    * @return void
    */
    public static void main(String[] args){
        Scanner Scan = new Scanner(System.in); //para los inputs

        ZonaVolcanica ZonaVolcanica = new ZonaVolcanica();
        ZonaArrecife ZonaArrecife = new ZonaArrecife();
        ZonaProfunda ZonaProfunda = new ZonaProfunda();
        NaveEstrellada NaveEstrellada = new NaveEstrellada();
        NaveExploradora Nave = new NaveExploradora(ZonaArrecife);
        Jugador jugador = new Jugador(Nave, ZonaArrecife);
        
        NaveEstrellada.setZonaAnterior(null);
        NaveEstrellada.setZonaSiguiente(ZonaArrecife);
        
        ZonaArrecife.setZonaAnterior(NaveEstrellada);
        ZonaArrecife.setZonaSiguiente(ZonaProfunda);
        
        ZonaProfunda.setZonaAnterior(ZonaArrecife);
        ZonaProfunda.setZonaSiguiente(ZonaVolcanica);
        
        ZonaVolcanica.setZonaAnterior(ZonaProfunda);
        ZonaVolcanica.setZonaSiguiente(null);

        boolean jugando = true;
        System.out.println("=== Inicio del juego ===");
        
        while (jugando){
            jugador.verEstadoJugador();
            
            System.out.println("1) Subir o descender en profundidad (a nado)");
            System.out.println("2) Explorar");
            System.out.println("3) Recoger recursos");
            System.out.println("4) Entrar a la Nave Exploradora");
            System.out.println("5) Ver profundidad actual");
            System.out.println("6) Ver oxígeno restante");
            System.out.println("7) Ver inventario");
            if (jugador.getNave().getRobot().isCreado()){
                System.out.println("8) Ocupar Robot Excavador");
            }
            if (jugador.getZonaActual() instanceof NaveEstrellada ){
                System.out.println("9) Reparar nave estrellada");
            }
            System.out.println("0) Salir del juego");
            System.out.print("> ");
            int Opcion = Scan.nextInt();

            switch (Opcion) {
                case 1:
                    System.out.print("Indica profundidad destino (rango zona " + jugador.getZonaActual().getProfundidadMin() + "-" + jugador.getZonaActual().getProfundidadMax() + " m):\n ");
                    int destino = Scan.nextInt();
                    int delta = destino - jugador.getProfundidadActual();
                    jugador.nadar(delta);
                    break;

                case 2:
                    jugador.getZonaActual().explorar(jugador);
                    break;

                case 3:
                    jugador.getZonaActual().recolectar(jugador,Scan);
                    break;

                case 4:
                    NaveExploradora nave = jugador.getNave();
                    nave.setZonaActual(jugador.getZonaActual()); //La nave se ubica en la zona actual
                    nave.entrar(jugador); //Recarga O2 y posiciona jugador en profundidad de anclaje
                    if (jugador.getZonaActual() instanceof NaveEstrellada naveEstrellada){
                        naveEstrellada.resetearAccionesSinTraje();
                    }
                    nave.MenuNave(jugador, Scan);
                    break;
               
                case 5:
                    System.out.println("\nProfundidad actual: " + jugador.getProfundidadActual() + " m");
                    break;

                case 6:
                    System.out.println("\nOxígeno restante: " + jugador.getTanqueOxigeno().getOxigenoRestante());
                    break;

                case 7:
                    jugador.verInventario();
                    break;

                case 8:
                    
                    if (jugador.getNave().getRobot() == null || !jugador.getNave().getRobot().isCreado()){
                        System.out.println("\nNo tienes un robot excavador creado.");
                    }else{
                        jugador.getNave().getRobot().menuRobot(jugador.getNave(), jugador, Scan);
                    }
                    break;

                case 9:
                    if (!(jugador.getZonaActual() instanceof NaveEstrellada)){
                        System.out.println("\nSolo puedes reparar la nave desde la Nave Estrellada.");
                        break;
                    }

                    int Titanio = 0, Acero = 0, Uranio = 0, Sulfuro = 0;
                    for (Item item : jugador.getInventario()) {
                        switch (item.getTipo()) {
                            case Titanio:
                                Titanio = item.getCantidad();
                                break;
                            case Acero:
                                Acero = item.getCantidad();
                                break;
                            case Uranio:
                                Uranio = item.getCantidad();
                                break;
                            case Sulfuro:
                                Sulfuro = item.getCantidad();
                                break;
                            default:
                                break;
                        
                        }
                    }
        
                    if (!jugador.getTienePlanos()){
                        System.out.println("\n --- No posees el plano de la nave necesario para la reparación. --- \n");
                        break;

                    }else if(jugador.getTienePlanos() && (Titanio < 50 || Acero < 30 || Uranio < 15 || Sulfuro < 20)){
                        System.out.println("No tienes los recursos necesario para la reparación.");
                        break;

                    }else if(jugador.getTienePlanos() && Titanio >= 50 && Acero >= 30 && Uranio >= 15 && Sulfuro >= 20){
                        System.out.print(" ¿Deseas reparar la Nave Estrellada? (s/n): ");
                        String confirm = Scan.next().toLowerCase();

                        if (confirm.equals("s")){
                            jugador.removerItem(ItemTipo.Titanio, 50);
                            jugador.removerItem(ItemTipo.Acero, 30);
                            jugador.removerItem(ItemTipo.Uranio, 15);
                            jugador.removerItem(ItemTipo.Sulfuro, 20);
                            jugador.removerItem(ItemTipo.PLANO_NAVE, 1);
                            jugador.setTienePlanos(false);

                            System.out.println("Iniciando reparación...");
                            try { Thread.sleep(2000); } catch (InterruptedException e) {}

                            System.out.println("¡La Nave Estrellada ha sido reparada con éxito!");
                            System.out.println("¡Has escapado del planeta acuático y completado el juego!");
                            System.out.println("🏆¡VICTORY ROYALE!🏆");
                            System.exit(0);

                        }else{
                            System.out.println("Reparación cancelada.");
                            break;
                        }
                    }else{
                        System.out.println("No tienes los recursos necesarios para reparar la nave.");
                        System.out.println("Necesitas: 50 Titanio, 30 Acero, 15 Uranio, 20 Sulfuro y el plano de la nave.");
                        break;
                    }
                    break;
                    
                case 0:
                    System.out.println("Saliendo del juego...");
                    jugando = false;
                    break;

                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
                    break;
            }
        }
        Scan.close();
    } 
}




