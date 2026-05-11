package com.piedrazul.appointments.shared.exception;

public class DocumentoDuplicadoException extends RuntimeException {
    public DocumentoDuplicadoException(String documento){
    super("El documento " + documento + " ya se encuentra registrado");
    }
}
