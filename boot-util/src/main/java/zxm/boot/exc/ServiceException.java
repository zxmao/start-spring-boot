package zxm.boot.exc;

/**
 * @author JunXiong
 * @Date 2017/3/7
 */
public class ServiceException extends RuntimeException{

    private Integer errorCode;

    public ServiceException() {
    }

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Throwable e){
        super(message, e);
    }

    public ServiceException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, String message, Throwable e){
        super(message, e);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable e){
        super(e);
    }

    public Integer getErrorCode() {
        return errorCode;
    }
    
}
