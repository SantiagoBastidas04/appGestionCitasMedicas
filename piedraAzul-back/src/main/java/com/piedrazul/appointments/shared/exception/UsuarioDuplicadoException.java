package com.piedrazul.appointments.shared.exception;

public class UsuarioDuplicadoException extends RuntimeException{

    public UsuarioDuplicadoException(String userName){
        super( "El usuario "+ userName + " ya se encuentra registrado");
    }
}
