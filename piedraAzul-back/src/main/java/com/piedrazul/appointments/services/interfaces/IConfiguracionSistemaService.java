package com.piedrazul.appointments.services.interfaces;

import com.piedrazul.appointments.DTOs.request.ConfiguracionMedicoRequest;
import com.piedrazul.appointments.entities.ConfiguracionSistema;
import com.piedrazul.appointments.entities.MedicoTerapista;


public interface IConfiguracionSistemaService {

    // Configuracion global
    ConfiguracionSistema obtenerConfiguracion();

    // Actualizcion de ventana de semanas
    ConfiguracionSistema actualizarVentanaSemanas(Integer semanas);

    // Configuracion por medico
    MedicoTerapista configurarMedico(Long medicoId, ConfiguracionMedicoRequest request);
}