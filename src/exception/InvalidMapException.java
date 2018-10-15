package exception;

/**
 * This class is used to handle Invalid map exceptions.
 * @version 1.0.0
 *
 */
public class InvalidMapException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message to be printed as exception
     */
    public InvalidMapException(String message) {
        super(message);
    }
}
