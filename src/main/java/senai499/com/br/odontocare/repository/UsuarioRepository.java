package senai499.com.br.odontocare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import senai499.com.br.odontocare.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}