/**
 * 
 */
package zxm.boot.exc;

public class HandlerException extends RuntimeException {

	private static final long serialVersionUID = -7374602897432840720L;
	
	public HandlerException() {
	}
	
	public HandlerException(String message){
		super(message);
	}

	public HandlerException(String message, Throwable e){
		super(message, e);
	}
	
	public HandlerException(Throwable e){
		super(e);
	}
}
