package com.ackermansoftware.forthorrors.gameobjects;

public class ExhaustedPoolException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExhaustedPoolException() {
		super();
	}

	public ExhaustedPoolException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ExhaustedPoolException(String detailMessage) {
		super(detailMessage);
	}

	public ExhaustedPoolException(Throwable throwable) {
		super(throwable);
	}

}
