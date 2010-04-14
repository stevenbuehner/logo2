package interfaces;

/**
 * Interface fuer Objekte die sich bewegen bzw. animieren koennen
 * @author steven
 * @version 1.0
 */
public interface Movable {

    /**
     *
     * @param Bearbeite die Logic im Objekt, die seit der verstrichenen Zeit
     *  delta vor sich gegangen ist
     */
    public void doLogic(long delta);

    /**
     *
     * @param Bewege das Objekt auf die Koordinaten die es seit dem letzten Zeitpunkt
     * haette weiterwandern muessen. Die Zeitdifferenz seit dem letzten Aufruf ist delta
     */
    public void move(long delta);

}
