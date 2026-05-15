package br.com.missaoespacial.repository;

import br.com.missaoespacial.model.Satelite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SateliteRepository extends JpaRepository<Satelite, Long> {
}
