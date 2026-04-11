package br.inatel.SoftwareEngineer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;
import br.inatel.SoftwareEngineer.repository.TallStationRepository;

@ExtendWith(MockitoExtension.class)
public class TallStationControllerUnitTest {
    @Mock
    private TallStationRepository tallStationRepository;

    @InjectMocks
    private TallStationController tallStationController;

    private TallStation createSampleTallStation() {
        TallStation tallStation = new TallStation();
        tallStation.setId(1);
        tallStation.setName("Tall Station 1");
        tallStation.setLocation("BR-116");
        tallStation.setBasePrice(BigDecimal.valueOf(10.05));
        return tallStation;
    }

    @Test
    void createTallStationShouldReturnHttpStatusOk() {
        TallStation tallStation = new TallStation();
        tallStation.setName("Tall Station 1");
        tallStation.setLocation("BR-116");
        tallStation.setBasePrice(BigDecimal.valueOf(10.05));

        when(tallStationRepository.save(tallStation)).thenReturn(tallStation);

        ResponseEntity<TallStation> response = tallStationController.createTallStation(tallStation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createTallStationShouldReturnValidBody() {
        TallStation tallStation = new TallStation();
        tallStation.setName("Tall Station 1");
        tallStation.setLocation("BR-116");
        tallStation.setBasePrice(BigDecimal.valueOf(5.05));

        when(tallStationRepository.save(tallStation)).thenReturn(tallStation);

        ResponseEntity<TallStation> response = tallStationController.createTallStation(tallStation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTallStationShouldReturnHttpStatusOk() {
        Integer id = 1;
        TallStation tallStation = new TallStation();
        tallStation.setName("Tall Station 1");
        tallStation.setLocation("BR-116");
        tallStation.setBasePrice(BigDecimal.valueOf(10.05));

        when(tallStationRepository.findById(id)).thenReturn(Optional.of(tallStation));
        when(tallStationRepository.save(tallStation)).thenReturn(tallStation);

        ResponseEntity<TallStation> response = tallStationController.updateTallStation(id, tallStation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTallStationShouldReturnHttpStatusNotFoundWhenIdDoesNotExist() {
        when(tallStationRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<TallStation> response = tallStationController.updateTallStation(1, createSampleTallStation());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTallStationsShouldReturnHttpStatusOkWhenListHasItems() {
        when(tallStationRepository.findAll()).thenReturn(List.of(createSampleTallStation()));
        ResponseEntity<List<TallStation>> response = tallStationController.getTallStations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTallStationsShouldReturnHttpStatusNotFoundWhenListIsEmpty() {
        when(tallStationRepository.findAll()).thenReturn(List.of());
        ResponseEntity<List<TallStation>> response = tallStationController.getTallStations();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTallStationByIdShouldReturnHttpStatusOkWhenIdExists() {
        when(tallStationRepository.findById(1)).thenReturn(Optional.of(createSampleTallStation()));
        ResponseEntity<TallStation> response = tallStationController.getTallStationById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTallStationByIdShouldReturnHttpStatusNotFoundWhenIdDoesNotExist() {
        when(tallStationRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<TallStation> response = tallStationController.getTallStationById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTallStationShouldReturnHttpStatusOkWhenIdExists() {
        when(tallStationRepository.existsById(1)).thenReturn(true);
        doNothing().when(tallStationRepository).deleteById(1);
        ResponseEntity<Void> response = tallStationController.deleteTallStation(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteTallStationShouldReturnHttpStatusNotFoundWhenIdDoesNotExist() {
        when(tallStationRepository.existsById(1)).thenReturn(false);
        ResponseEntity<Void> response = tallStationController.deleteTallStation(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
