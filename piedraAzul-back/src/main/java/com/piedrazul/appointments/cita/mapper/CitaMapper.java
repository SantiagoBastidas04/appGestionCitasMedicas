package com.piedrazul.appointments.cita.mapper;

import com.piedrazul.appointments.cita.dto.CitaResponse;
import com.piedrazul.appointments.cita.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponse toResponse(Cita cita) {
        CitaResponse response = new CitaResponse();
        response.setId(cita.getId());
        response.setMedicoId(cita.getMedico().getId());
        response.setNombreMedico(
                cita.getMedico().getNombres() + " " + cita.getMedico().getApellidos()
        );
        response.setPacienteId(cita.getPaciente().getId());
        response.setNombrePaciente(
                cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos()
        );
        response.setNumeroDocumentoPaciente(cita.getPaciente().getNumeroDocumento());
        response.setCelularPaciente(cita.getPaciente().getCelular());
        response.setFecha(cita.getFecha());
        response.setHora(cita.getHora());
        response.setEstado(cita.getEstado());
        response.setObservacion(cita.getObservacion());
        response.setRegistradoPor(
                cita.getRegistradoPor().getNombres() + " " + cita.getRegistradoPor().getApellidos()
        );
        return response;
    }
}