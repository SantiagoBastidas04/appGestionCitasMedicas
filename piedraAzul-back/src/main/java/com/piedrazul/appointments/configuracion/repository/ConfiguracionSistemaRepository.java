package com.piedrazul.appointments.configuracion.repository;

import com.piedrazul.appointments.configuracion.entity.ConfiguracionSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    // Siempre hay un solo registro de configuracion
    ConfiguracionSistema findFirstBy();
}