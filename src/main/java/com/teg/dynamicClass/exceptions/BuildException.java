package com.teg.dynamicClass.exceptions;
public class BuildException extends RuntimeException{

	private static final long serialVersionUID = -3418272280398021636L;
	
	public BuildException(String message){
		super(message);
	}
	
	public BuildException(String message, Throwable throwable){
		super(message, throwable);
	}

}
