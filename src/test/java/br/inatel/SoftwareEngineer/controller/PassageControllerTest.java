package br.inatel.SoftwareEngineer.controller;

import br.inatel.SoftwareEngineer.domain.passages.Passages;
import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;
import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import br.inatel.SoftwareEngineer.domain.vehicles.VehicleType;
import br.inatel.SoftwareEngineer.repository.PassageRepository;
import br.inatel.SoftwareEngineer.repository.TallStationRepository;
import br.inatel.SoftwareEngineer.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PassageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PassageRepository passageRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TallStationRepository tallStationRepository;

    private Vehicle car;
    private TallStation tallStation;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        car = new Vehicle("AAA-1111", VehicleType.CAR, 2, false);
        car = vehicleRepository.save(car);
        
        tallStation = new TallStation("Pedagio Sul", "Rodovia Dom Pedro", new BigDecimal("5.00"));
        tallStation = tallStationRepository.save(tallStation);
    }

    private String createJson(Integer vehicleId, Integer tallStationId, String location) {
        return String.format(
            "{" +
            "  \"vehicle\": {\"id\": %d}," +
            "  \"tallStation\": {\"id\": %d}," +
            "  \"location\": \"%s\"," +
            "  \"timestamp\": \"2025-01-01T12:00:00\"," +
            "  \"amountPaid\": 10.00," +
            "  \"multiplierApplied\": 1.0" +
            "}", vehicleId, tallStationId, location);
    }
    
    private Passages createSamplePassage() {
        Passages samplePassage = new Passages();
        samplePassage.setVehicle(car);
        samplePassage.setTallStation(tallStation);
        samplePassage.setLocation("BR-116");
        samplePassage.setTimestamp(LocalDateTime.of(2025, 1, 1, 12, 0, 0));
        samplePassage.setAmountPaid(new BigDecimal("10.00"));
        samplePassage.setMultiplierApplied(new BigDecimal("1.0"));
        return samplePassage;
    }

    // Fluxo Normal (5 testes)
    @Test
    void shouldCreatePassageSuccessfully() throws Exception {
        String jsonPayload = createJson(car.getId(), tallStation.getId(), "BR-116");

        mockMvc.perform(post("/api/passages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdatePassageSuccessfully() throws Exception {
        Passages saved = passageRepository.save(createSamplePassage());
        
        String updateJson = createJson(car.getId(), tallStation.getId(), "BR-116 Atualizada");

        mockMvc.perform(put("/api/passages/update/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("BR-116 Atualizada"));
    }

    @Test
    void shouldReturnAllPassages() throws Exception {
        passageRepository.save(createSamplePassage());

        mockMvc.perform(get("/api/passages/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("BR-116"));
    }

    @Test
    void shouldReturnPassageById() throws Exception {
        Passages saved = passageRepository.save(createSamplePassage());

        mockMvc.perform(get("/api/passages/" + saved.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePassageSuccessfully() throws Exception {
        Passages saved = passageRepository.save(createSamplePassage());

        mockMvc.perform(delete("/api/passages/delete/" + saved.getId()))
                .andExpect(status().isOk());

        assertFalse(passageRepository.existsById(saved.getId()));
    }

    // Fluxo de Extensão (5 testes)
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPassage() throws Exception {
        String jsonPayload = createJson(car.getId(), tallStation.getId(), "Inexistente");
        
        mockMvc.perform(put("/api/passages/update/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenGettingAllPassagesEmpty() throws Exception {
        passageRepository.deleteAll(); // ensures empty

        mockMvc.perform(get("/api/passages/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentPassageById() throws Exception {
        mockMvc.perform(get("/api/passages/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPassage() throws Exception {
        mockMvc.perform(delete("/api/passages/delete/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingPassageWithInvalidData() throws Exception {
        String invalidJson = createJson(car.getId(), tallStation.getId(), ""); // Location empty triggers @NotBlank

        mockMvc.perform(post("/api/passages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
