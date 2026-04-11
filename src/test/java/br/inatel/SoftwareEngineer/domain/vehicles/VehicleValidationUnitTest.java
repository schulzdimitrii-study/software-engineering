package br.inatel.SoftwareEngineer.domain.vehicles;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleValidationUnitTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void validVehicleShouldHaveNoViolations() {
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, 2, false);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidPlateShouldGenerateViolation() {
        Vehicle vehicle = new Vehicle("ABC1234", VehicleType.CAR, 2, false);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertEquals("plate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void nullVehicleTypeShouldGenerateViolation() {
        Vehicle vehicle = new Vehicle("ABC-1234", null, 2, false);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertEquals("type", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void axlesLowerThanMinimumShouldGenerateViolation() {
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, 1, false);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertEquals("axles", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void axlesHigherThanMaximumShouldGenerateViolation() {
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, 10, false);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertEquals("axles", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void nullEmergencyFlagShouldGenerateViolation() {
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, 2, null);

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertEquals("is_emergency", violations.iterator().next().getPropertyPath().toString());
    }
}
