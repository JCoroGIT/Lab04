package domain;

public class NeoFlixException extends Exception {

    public static final String TO_BE_IMPLEMENTED = "Funcionalidad pendiente de implementación";
    public static final String VALUE_UNKNOWN = "Valor no reconocido";
    public static final String DATA_ERROR = "Error en los datos";
    public static final String CONTENT_EMPTY = "No hay contenido";
    public static final String TITLE_EXISTS = "El nombre ya existe";
    public static final String NOT_NUMERIC = "Los valores deben ser numéricos";
    public static final String EPISODE_NOT_FOUND = "El episodio no existe";
    
    
    public NeoFlixException() {
        super("Ha ocurrido un error en la aplicación");
    }

    
    public NeoFlixException(String mensaje) {
        super(mensaje);
    }

    
    public NeoFlixException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
