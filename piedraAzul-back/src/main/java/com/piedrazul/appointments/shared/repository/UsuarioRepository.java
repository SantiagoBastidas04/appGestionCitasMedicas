package com.piedrazul.appointments.shared.repository;

import com.piedrazul.appointments.shared.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Encontrar por username
    Optional<Usuario> findByUsername(String username);

    // Verificar que el username no esté duplicado
    boolean existsByUsername(String username);

}
