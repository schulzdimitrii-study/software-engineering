package br.inatel.SoftwareEngineer.controller;

import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;
import br.inatel.SoftwareEngineer.repository.TallStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tall-station")
public class TallStationController {

    @Autowired
    private TallStationRepository tallStationRepository;

    @PostMapping("/create")
    public ResponseEntity<TallStation> createTallStation(@Valid @RequestBody TallStation tallStation) {
        TallStation saved = tallStationRepository.save(tallStation);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TallStation> updateTallStation(@PathVariable Integer id,
            @Valid @RequestBody TallStation tallStationData) {
        Optional<TallStation> existingTallStation = tallStationRepository.findById(id);
        if (existingTallStation.isPresent()) {
            tallStationData.setId(id);
            TallStation updated = tallStationRepository.save(tallStationData);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<TallStation>> getTallStations() {
        List<TallStation> tallStations = tallStationRepository.findAll();
        if (tallStations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tallStations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallStation> getTallStationById(@PathVariable Integer id) {
        Optional<TallStation> tallStation = tallStationRepository.findById(id);
        return tallStation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTallStation(@PathVariable Integer id) {
        if (tallStationRepository.existsById(id)) {
            tallStationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
