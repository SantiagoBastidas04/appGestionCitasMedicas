package com.piedrazul.appointments.configuracion.service;

import com.piedrazul.appointments.medico.dto.ConfiguracionMedicoRequest;
import com.piedrazul.appointments.configuracion.entity.ConfiguracionSistema;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;


public interface IConfiguracionSistemaService {

    // Configuracion global
    ConfiguracionSistema obtenerConfiguracion();

    // Actualizcion de ventana de semanas
    ConfiguracionSistema actualizarVentanaSemanas(Integer semanas);

    // Configuracion por medico
    MedicoTerapista configurarMedico(Long medicoId, ConfiguracionMedicoRequest request);
}