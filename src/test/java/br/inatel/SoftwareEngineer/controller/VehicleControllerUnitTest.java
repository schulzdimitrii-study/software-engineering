package br.inatel.SoftwareEngineer.controller;

import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import br.inatel.SoftwareEngineer.domain.vehicles.VehicleType;
import br.inatel.SoftwareEngineer.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleControllerUnitTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void createVehicleShouldReturnSavedVehicle() {
        Vehicle input = new Vehicle("ABC-1234", VehicleType.CAR, 2, false);
        Vehicle saved = new Vehicle("ABC-1234", VehicleType.CAR, 2, false);
        saved.setId(1);

        when(vehicleRepository.save(input)).thenReturn(saved);

        ResponseEntity<Vehicle> response = vehicleController.createVehicle(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void updateVehicleShouldReturnUpdatedWhenEntityExists() {
        Integer id = 10;
        Vehicle existing = new Vehicle("AAA-1111", VehicleType.CAR, 2, false);
        existing.setId(id);
        Vehicle updatePayload = new Vehicle("BBB-2222", VehicleType.TRUCK, 4, false);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(existing));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(id, updatePayload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("BBB-2222", response.getBody().getPlate());
    }

    @Test
    void updateVehicleShouldReturnNotFoundWhenEntityDoesNotExist() {
        Integer id = 99;
        Vehicle updatePayload = new Vehicle("BBB-2222", VehicleType.TRUCK, 4, false);

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(id, updatePayload);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getVehiclesShouldReturnListWhenNotEmpty() {
        Vehicle v1 = new Vehicle("AAA-1111", VehicleType.CAR, 2, false);
        Vehicle v2 = new Vehicle("CCC-3333", VehicleType.TRUCK, 6, false);

        when(vehicleRepository.findAll()).thenReturn(List.of(v1, v2));

        ResponseEntity<List<Vehicle>> response = vehicleController.getVehicles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getVehiclesShouldReturnNotFoundWhenEmpty() {
        when(vehicleRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Vehicle>> response = vehicleController.getVehicles();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getVehicleByIdShouldReturnEntityWhenExists() {
        Integer id = 7;
        Vehicle existing = new Vehicle("DDD-4444", VehicleType.CAR, 2, false);
        existing.setId(id);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(existing));

        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void getVehicleByIdShouldReturnNotFoundWhenMissing() {
        Integer id = 404;

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteVehicleShouldReturnOkWhenExists() {
        Integer id = 11;

        when(vehicleRepository.existsById(id)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(id);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(vehicleRepository).deleteById(id);
    }

    @Test
    void deleteVehicleShouldReturnNotFoundWhenMissing() {
        Integer id = 12;

        when(vehicleRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(vehicleRepository, never()).deleteById(id);
    }
}
