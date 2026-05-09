package com.piedrazul.appointments.shared.exception;

public class CorreoDuplicadoException extends RuntimeException{
    public CorreoDuplicadoException(String correo){
        super("El correo " + correo + " se encuentra duplicado");
    }
}
