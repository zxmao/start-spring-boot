package zxm.boot.exc;

/**
 * @author JunXiong
 * @Date 2017/3/7
 */
public class ControllerException extends RuntimeException{

    private Integer errorCode;

    public ControllerException() {
    }

    public ControllerException(String message){
        super(message);
    }

    public ControllerException(String message, Throwable e){
        super(message, e);
    }

    public ControllerException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public ControllerException(int errorCode, String message, Throwable e){
        super(message, e);
        this.errorCode = errorCode;
    }

    public ControllerException(Throwable e){
        super(e);
    }

    public Integer getErrorCode() {
        return errorCode;
    }
    
}
