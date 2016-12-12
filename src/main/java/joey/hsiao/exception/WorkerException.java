package joey.hsiao.exception;

public class WorkerException extends RuntimeException {
    private static final long serialVersionUID = 1616634396592682524L;

    public WorkerException(String msg){
        super(msg);
    }
}
