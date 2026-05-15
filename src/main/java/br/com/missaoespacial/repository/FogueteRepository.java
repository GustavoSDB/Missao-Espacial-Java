package br.com.missaoespacial.repository;

import br.com.missaoespacial.model.Foguete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FogueteRepository extends JpaRepository<Foguete, Long> {
}
