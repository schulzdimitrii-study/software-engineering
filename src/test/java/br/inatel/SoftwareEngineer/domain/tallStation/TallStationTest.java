package br.inatel.SoftwareEngineer.domain.tallStation;

import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import br.inatel.SoftwareEngineer.domain.vehicles.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TallStationTest {

    private TallStation tallStation;

    @BeforeEach
    void setUp() {
        tallStation = new TallStation("Pedágio Principal", "Rodovia BR-116", new BigDecimal("5.00"));
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

    @Test
    void calculateStandardFare() {
        Vehicle car = new Vehicle("BAG-2A07", VehicleType.CAR, 2, false);
        BigDecimal result = tallStation.calculateTall(car, LocalTime.of(10, 0));
        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void calculateMotorcycleOffPeak() {
        Vehicle motorcycle = new Vehicle("MOT-1234", VehicleType.MOTORCYCLE, 2, false);

        BigDecimal result = tallStation.calculateTall(motorcycle, LocalTime.of(14, 0));

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void calculateAtMorningBoundaryStart() {
        Vehicle car = new Vehicle("MOR-7000", VehicleType.CAR, 2, false);

        BigDecimal result = tallStation.calculateTall(car, LocalTime.of(7, 0));

        assertEquals(new BigDecimal("12.00"), result);
    }

    @Test
    void calculateAtEveningBoundaryStart() {
        Vehicle car = new Vehicle("EVE-1700", VehicleType.CAR, 2, false);

        BigDecimal result = tallStation.calculateTall(car, LocalTime.of(17, 0));

        assertEquals(new BigDecimal("12.00"), result);
    }

    @Test
    void axlesFallbackWhenZero() {
        Vehicle car = new Vehicle("ZER-0000", VehicleType.CAR, 0, false);

        BigDecimal result = tallStation.calculateTall(car, LocalTime.of(10, 0));

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void axlesFallbackWhenNull() {
        Vehicle car = new Vehicle("NUL-0000", VehicleType.CAR, null, false);

        BigDecimal result = tallStation.calculateTall(car, LocalTime.of(10, 0));

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void notPeakAtMorningBoundaryEnd() {
        assertFalse(tallStation.isPeakHour(LocalTime.of(9, 0)));
    }

    @Test
    void notPeakAtEveningBoundaryEnd() {
        assertFalse(tallStation.isPeakHour(LocalTime.of(19, 0)));
    }

    @Test
    void peakAtMorningInsideRange() {
        assertTrue(tallStation.isPeakHour(LocalTime.of(8, 30)));
    }

    @Test
    void peakAtEveningInsideRange() {
        assertTrue(tallStation.isPeakHour(LocalTime.of(18, 15)));
    }

}
