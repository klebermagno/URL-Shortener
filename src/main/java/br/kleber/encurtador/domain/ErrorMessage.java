package br.kleber.encurtador.domain;

public class ErrorMessage {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	public ErrorMessage(String m){
		message = m;
	}
}
