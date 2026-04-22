package com.piedrazul.appointments.paciente.mapper;


import com.piedrazul.appointments.paciente.dto.PacienteRequest;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    // Request → Entidad
    public Paciente toEntity(PacienteRequest request) {
        return new Paciente(
                request.getNombres(),
                request.getApellidos(),
                request.getNumeroDocumento(),
                request.getCelular(),
                request.getGenero(),
                request.getFechaNacimiento(),
                request.getCorreoElectronico()
        );
    }

    // Entidad → Response
    public PacienteResponse toResponse(Paciente paciente) {
        PacienteResponse response = new PacienteResponse();
        response.setId(paciente.getId());
        response.setNombres(paciente.getNombres());
        response.setApellidos(paciente.getApellidos());
        response.setNumeroDocumento(paciente.getNumeroDocumento());
        response.setCelular(paciente.getCelular());
        response.setGenero(paciente.getGenero());
        response.setFechaNacimiento(paciente.getFechaNacimiento());
        response.setCorreoElectronico(paciente.getCorreoElectronico());
        return response;
    }
}