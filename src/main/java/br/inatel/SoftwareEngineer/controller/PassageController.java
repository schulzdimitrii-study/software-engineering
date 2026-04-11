package br.inatel.SoftwareEngineer.controller;

import br.inatel.SoftwareEngineer.domain.passages.Passages;
import br.inatel.SoftwareEngineer.repository.PassageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passages")
public class PassageController {

    @Autowired
    private PassageRepository passageRepository;

    @PostMapping("/create")
    public ResponseEntity<Passages> createPassage(@Valid @RequestBody Passages passage) {
        Passages saved = passageRepository.save(passage);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Passages> updatePassage(@PathVariable Integer id, @Valid @RequestBody Passages passageData) {
        Optional<Passages> existingPassage = passageRepository.findById(id);
        if (existingPassage.isPresent()) {
            passageData.setId(id);
            Passages updated = passageRepository.save(passageData);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Passages>> getPassages() {
        List<Passages> passages = passageRepository.findAll();
        if (passages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(passages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passages> getPassageById(@PathVariable Integer id) {
        Optional<Passages> passage = passageRepository.findById(id);
        return passage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePassage(@PathVariable Integer id) {
        if (passageRepository.existsById(id)) {
            passageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
