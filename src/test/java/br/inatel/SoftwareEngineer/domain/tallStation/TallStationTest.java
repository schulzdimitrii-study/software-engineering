package br.inatel.SoftwareEngineer.domain.tallStation;

import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import br.inatel.SoftwareEngineer.domain.vehicles.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TallStationTest {

    private TallStation tallStation;

    @BeforeEach
    void setUp() {
        tallStation = new TallStation("Pedágio Principal", new BigDecimal("5.00"));
    }

    @Test
    void calculateSixAxlesTruck() {
        Vehicle truck = new Vehicle("ABC-1234", VehicleType.TRUCK, 6, false);
        
        BigDecimal result = tallStation.calculateTall(truck, LocalTime.of(10, 0));

        assertEquals(new BigDecimal("30.00"), result);
    }

    @Test
    void peakHour() {
        Vehicle car = new Vehicle("XYZ-9876", VehicleType.CAR, 2, false);

        BigDecimal normalResult = tallStation.calculateTall(car, LocalTime.of(10, 0));
        assertEquals(new BigDecimal("10.00"), normalResult);

        BigDecimal peakResult = tallStation.calculateTall(car, LocalTime.of(8, 0));
        assertEquals(new BigDecimal("12.00"), peakResult);
    }

    @Test
    void emergencyVehicles() {
        Vehicle ambulance = new Vehicle("SOS-1920", VehicleType.CAR, 2, true);
        
        BigDecimal result = tallStation.calculateTall(ambulance, LocalTime.of(8, 0));

        assertEquals(new BigDecimal("0.00"), result);
    }
}
