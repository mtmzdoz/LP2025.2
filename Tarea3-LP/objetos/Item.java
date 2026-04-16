package objetos;

public class Item{
   //privados + / públicos -
    private ItemTipo tipo;
    private int cantidad;

    /*
    * Constructor principal del ítem.
    * Inicializa el tipo y la cantidad asignada.
    * @param tipo: ItemTipo - tipo de ítem a crear.
    * @param cantidad: int - número de unidades del ítem.
    * @return void
    */
    public Item(ItemTipo tipo, int cantidad){
        this.tipo = tipo;
        this.cantidad = cantidad;
        
    }

    /*
    * Constructor secundario que crea un ítem del tipo especificado con cantidad por defecto de 1.
    * @param tipo: ItemTipo - tipo de ítem a crear.
    * @return void
    */
    public Item(ItemTipo tipo){
        this(tipo, 1);
    }

    // Getter
    /*
    * Obtiene el tipo de ítem.
    * @param Ninguno
    * @return ItemTipo - tipo del ítem (ejemplo: Cobre, Oro, etc.).
    */
    public ItemTipo getTipo(){
        return tipo;
    }

    /*
    * Devuelve la cantidad actual de unidades del ítem.
    * @param Ninguno
    * @return int - cantidad de unidades disponibles.
    */
    public int getCantidad(){
        return cantidad;
    }
    
    // Setter 
    /*
    * Modifica la cantidad actual de unidades del ítem.
    * @param cantidad: int - nueva cantidad a establecer.
    * @return void
    */
    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }  
}
