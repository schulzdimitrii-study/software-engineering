package br.inatel.SoftwareEngineer.domain.passages;

import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;
import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import br.inatel.SoftwareEngineer.domain.vehicles.VehicleType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PassagesTest {

    private Validator validator;
    private Vehicle car;
    private TallStation tallStation;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        car = new Vehicle("ABC-1234", VehicleType.CAR, 2, false);
        tallStation = new TallStation("Pedágio Principal", "Rodovia BR-116", new BigDecimal("5.00"));
        now = LocalDateTime.now();
    }

    // Fluxo Normal
    @Test
    void shouldCreatePassageWithValidConstructor() {
        Passages passage = new Passages(car, "BR-116 Km 50", now, new BigDecimal("10.00"), new BigDecimal("1.0"));
        assertNotNull(passage);
        assertEquals("ABC-1234", passage.getVehicle().getPlate());
    }

    @Test
    void shouldSetAndGetLocation() {
        Passages passage = new Passages();
        passage.setLocation("Nova Localizacao");
        assertEquals("Nova Localizacao", passage.getLocation());
    }

    @Test
    void shouldSetAndGetAmountPaid() {
        Passages passage = new Passages();
        passage.setAmountPaid(new BigDecimal("15.50"));
        assertEquals(new BigDecimal("15.50"), passage.getAmountPaid());
    }

    @Test
    void shouldSetAndGetMultiplierApplied() {
        Passages passage = new Passages();
        passage.setMultiplierApplied(new BigDecimal("1.2"));
        assertEquals(new BigDecimal("1.2"), passage.getMultiplierApplied());
    }

    @Test
    void shouldSetAndGetVehicleAndTallStation() {
        Passages passage = new Passages();
        passage.setVehicle(car);
        passage.setTallStation(tallStation);
        assertEquals(car, passage.getVehicle());
        assertEquals(tallStation, passage.getTallStation());
    }

    // Fluxo de Extensão
    @Test
    void shouldYieldErrorWhenVehicleIsNull() {
        Passages passage = new Passages(null, "BR-116 Km 50", now, new BigDecimal("10.00"), new BigDecimal("1.0"));
        passage.setTallStation(tallStation);
        Set<ConstraintViolation<Passages>> violations = validator.validate(passage);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Vehicle is required")));
    }

    @Test
    void shouldYieldErrorWhenTallStationIsNull() {
        Passages passage = new Passages(car, "BR-116 Km 50", now, new BigDecimal("10.00"), new BigDecimal("1.0"));
        Set<ConstraintViolation<Passages>> violations = validator.validate(passage);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("TallStation is required")));
    }

    @Test
    void shouldYieldErrorWhenLocationIsBlank() {
        Passages passage = new Passages(car, "", now, new BigDecimal("10.00"), new BigDecimal("1.0"));
        passage.setTallStation(tallStation);
        Set<ConstraintViolation<Passages>> violations = validator.validate(passage);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Location is required")));
    }

    @Test
    void shouldYieldErrorWhenTimestampIsNull() {
        Passages passage = new Passages(car, "Loc", null, new BigDecimal("10.00"), new BigDecimal("1.0"));
        passage.setTallStation(tallStation);
        Set<ConstraintViolation<Passages>> violations = validator.validate(passage);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Timestamp is required")));
    }

    @Test
    void shouldYieldErrorWhenAmountPaidIsNegative() {
        Passages passage = new Passages(car, "Loc", now, new BigDecimal("-5.00"), new BigDecimal("1.0"));
        passage.setTallStation(tallStation);
        Set<ConstraintViolation<Passages>> violations = validator.validate(passage);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount paid must be positive or zero")));
    }
}
