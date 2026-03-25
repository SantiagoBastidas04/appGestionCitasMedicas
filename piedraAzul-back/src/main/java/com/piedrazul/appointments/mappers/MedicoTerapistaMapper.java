package com.piedrazul.appointments.mappers;

import com.piedrazul.appointments.DTOs.request.MedicoTerapistaRequest;
import com.piedrazul.appointments.DTOs.response.MedicoTerapistaResponse;
import com.piedrazul.appointments.entities.MedicoTerapista;
import org.springframework.stereotype.Component;

@Component
public class MedicoTerapistaMapper {

    // Request a Entidad
    public MedicoTerapista toEntity(MedicoTerapistaRequest request) {
        MedicoTerapista medico = new MedicoTerapista(
                request.getUsername(),
                request.getPassword(),
                request.getNombres(),
                request.getApellidos(),
                true,
                request.getEspecialidad(),
                request.getIntervaloCitas(),
                request.getHoraInicio(),
                request.getHoraFin()
        );
        medico.setDiasAtencion(request.getDiasAtencion());
        return medico;
    }

    // Entidad a Response
    public MedicoTerapistaResponse toResponse(MedicoTerapista medico) {
        MedicoTerapistaResponse response = new MedicoTerapistaResponse();
        response.setId(medico.getId());
        response.setNombres(medico.getNombres());
        response.setApellidos(medico.getApellidos());
        response.setEspecialidad(medico.getEspecialidad());
        response.setIntervaloCitas(medico.getIntervaloCitas());
        response.setHoraInicio(medico.getHoraInicio());
        response.setHoraFin(medico.getHoraFin());
        response.setDiasAtencion(medico.getDiasAtencion());
        response.setActivo(medico.isActivo());
        return response;
    }
}
