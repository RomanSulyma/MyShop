package net.exception;

//Abstract class for Exception
public abstract class AbstractException extends IllegalArgumentException {

    private final int code;

    public AbstractException(String s, int code) {
        super(s);
        this.code = code;
    }

    public AbstractException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public AbstractException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
