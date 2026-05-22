package com.piedrazul.appointments.auth.service;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.paciente.entity.Paciente;

public interface IAuthService {
     Paciente registrarPaciente(RegistroPacienteRequest request);

}
