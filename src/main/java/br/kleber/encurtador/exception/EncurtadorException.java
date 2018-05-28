package br.kleber.encurtador.exception;

import java.util.Collection;




public class EncurtadorException extends RuntimeException {
	

	private static final long serialVersionUID = 1L;
	private Collection<String> messages;

	    public EncurtadorException(String msg){
	        super(msg);
	    }


	    public EncurtadorException(String msg, Exception cause){
	        super(msg, cause);
	    }


	    public EncurtadorException(Collection<String> messages){
	        super();
	        this.messages= messages;
	    }


	    public EncurtadorException (Collection<String> messages, Exception cause){
	        super(cause);
	        this.messages= messages;
	    }



}
