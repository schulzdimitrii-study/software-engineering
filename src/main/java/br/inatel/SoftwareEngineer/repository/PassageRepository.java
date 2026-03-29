package br.inatel.SoftwareEngineer.repository;

import br.inatel.SoftwareEngineer.domain.passages.Passages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PassageRepository extends JpaRepository<Passages, UUID> {
}
