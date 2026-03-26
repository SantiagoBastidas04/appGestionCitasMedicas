package com.piedrazul.appointments.services;

import com.piedrazul.appointments.DTOs.request.ConfiguracionMedicoRequest;
import com.piedrazul.appointments.entities.ConfiguracionSistema;
import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.repositories.ConfiguracionSistemaRepository;
import com.piedrazul.appointments.repositories.MedicoTerapistaRepository;
import com.piedrazul.appointments.services.interfaces.IConfiguracionSistemaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracionSistemaService implements IConfiguracionSistemaService {

    private final ConfiguracionSistemaRepository configuracionRepository;
    private final MedicoTerapistaRepository medicoRepository;

    @Override
    public ConfiguracionSistema obtenerConfiguracion() {
        return configuracionRepository.findFirstBy();
    }

    @Override
    @Transactional
    public ConfiguracionSistema actualizarVentanaSemanas(Integer semanas) {
        ConfiguracionSistema config = configuracionRepository.findFirstBy();
        if (config == null) {
            config = new ConfiguracionSistema(semanas);
        } else {
            config.setVentanaSemanas(semanas);
        }
        return configuracionRepository.save(config);
    }

    @Override
    @Transactional
    public MedicoTerapista configurarMedico(Long medicoId, ConfiguracionMedicoRequest request) {
        MedicoTerapista medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Médico no encontrado con id: " + medicoId
                ));

        medico.setHoraInicio(request.getHoraInicio());
        medico.setHoraFin(request.getHoraFin());
        medico.setIntervaloCitas(request.getIntervaloCitas());
        medico.setDiasAtencion(request.getDiasAtencion());
        return medicoRepository.save(medico);
    }
}