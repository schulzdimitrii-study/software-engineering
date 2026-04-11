package br.inatel.SoftwareEngineer.repository;

import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallStationRepository extends JpaRepository<TallStation, Integer> {
}
