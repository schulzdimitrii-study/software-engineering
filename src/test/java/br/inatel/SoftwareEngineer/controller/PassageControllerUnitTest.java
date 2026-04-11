package br.inatel.SoftwareEngineer.controller;

import br.inatel.SoftwareEngineer.domain.passages.Passages;
import br.inatel.SoftwareEngineer.repository.PassageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassageControllerUnitTest {

    @Mock
    private PassageRepository passageRepository;

    @InjectMocks
    private PassageController passageController;

    private Passages createSamplePassage() {
        Passages p = new Passages();
        p.setId(1);
        p.setLocation("BR-116");
        p.setAmountPaid(new BigDecimal("10.00"));
        return p;
    }

    @Test
    void createPassageShouldReturnHttpStatusOk() {
        Passages input = new Passages();
        input.setLocation("BR-116");
        when(passageRepository.save(input)).thenReturn(createSamplePassage());

        ResponseEntity<Passages> response = passageController.createPassage(input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createPassageShouldReturnValidBody() {
        Passages input = new Passages();
        when(passageRepository.save(input)).thenReturn(createSamplePassage());

        ResponseEntity<Passages> response = passageController.createPassage(input);
        assertNotNull(response.getBody());
    }

    @Test
    void updatePassageShouldReturnHttpStatusOk() {
        Integer id = 1;
        when(passageRepository.findById(id)).thenReturn(Optional.of(createSamplePassage()));
        when(passageRepository.save(any(Passages.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Passages> response = passageController.updatePassage(id, new Passages());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updatePassageShouldApplyNewLocationCorrectly() {
        Integer id = 1;
        Passages updatePayload = new Passages();
        updatePayload.setLocation("BR-277");

        when(passageRepository.findById(id)).thenReturn(Optional.of(createSamplePassage()));
        when(passageRepository.save(any(Passages.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Passages> response = passageController.updatePassage(id, updatePayload);
        assertEquals("BR-277", response.getBody().getLocation());
    }

    @Test
    void getPassagesShouldReturnHttpStatusOkWhenListHasItems() {
        when(passageRepository.findAll()).thenReturn(List.of(createSamplePassage()));
        ResponseEntity<List<Passages>> response = passageController.getPassages();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPassagesShouldReturnExactListSize() {
        Passages p2 = createSamplePassage();
        p2.setId(2);
        when(passageRepository.findAll()).thenReturn(List.of(createSamplePassage(), p2));

        ResponseEntity<List<Passages>> response = passageController.getPassages();
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getPassageByIdShouldReturnHttpStatusOk() {
        when(passageRepository.findById(1)).thenReturn(Optional.of(createSamplePassage()));
        ResponseEntity<Passages> response = passageController.getPassageById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPassageByIdShouldReturnMatchingLocation() {
        when(passageRepository.findById(1)).thenReturn(Optional.of(createSamplePassage()));
        ResponseEntity<Passages> response = passageController.getPassageById(1);
        assertEquals("BR-116", response.getBody().getLocation());
    }

    @Test
    void deletePassageShouldReturnHttpStatusOk() {
        when(passageRepository.existsById(1)).thenReturn(true);
        doNothing().when(passageRepository).deleteById(1);

        ResponseEntity<Void> response = passageController.deletePassage(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePassageShouldInvokeRepositoryDeleteMethod() {
        when(passageRepository.existsById(1)).thenReturn(true);
        passageController.deletePassage(1);
        verify(passageRepository, times(1)).deleteById(1);
    }

    @Test
    void updatePassageShouldReturnNotFoundWhenMissing() {
        when(passageRepository.findById(99)).thenReturn(Optional.empty());
        ResponseEntity<Passages> response = passageController.updatePassage(99, new Passages());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updatePassageShouldGuaranteeSaveIsNotCalledWhenMissing() {
        when(passageRepository.findById(99)).thenReturn(Optional.empty());
        passageController.updatePassage(99, new Passages());
        verify(passageRepository, never()).save(any());
    }

    @Test
    void updatePassageShouldHaveNullBodyOnNotFound() {
        when(passageRepository.findById(99)).thenReturn(Optional.empty());
        ResponseEntity<Passages> response = passageController.updatePassage(99, new Passages());
        assertNull(response.getBody());
    }

    @Test
    void getPassagesShouldReturnNotFoundWhenEmpty() {
        when(passageRepository.findAll()).thenReturn(List.of());
        ResponseEntity<List<Passages>> response = passageController.getPassages();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPassagesShouldHaveNullBodyWhenEmpty() {
        when(passageRepository.findAll()).thenReturn(List.of());
        ResponseEntity<List<Passages>> response = passageController.getPassages();
        assertNull(response.getBody());
    }

    @Test
    void getPassageByIdShouldReturnNotFoundWhenMissing() {
        when(passageRepository.findById(404)).thenReturn(Optional.empty());
        ResponseEntity<Passages> response = passageController.getPassageById(404);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPassageByIdShouldHaveNullBodyWhenMissing() {
        when(passageRepository.findById(404)).thenReturn(Optional.empty());
        ResponseEntity<Passages> response = passageController.getPassageById(404);
        assertNull(response.getBody());
    }

    @Test
    void deletePassageShouldReturnNotFoundWhenMissing() {
        when(passageRepository.existsById(404)).thenReturn(false);
        ResponseEntity<Void> response = passageController.deletePassage(404);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePassageShouldGuaranteeDeleteByIdIsNotCalledWhenMissing() {
        when(passageRepository.existsById(404)).thenReturn(false);
        passageController.deletePassage(404);
        verify(passageRepository, never()).deleteById(anyInt());
    }

    @Test
    void deletePassageShouldHaveNullBodyOnNotFound() {
        when(passageRepository.existsById(404)).thenReturn(false);
        ResponseEntity<Void> response = passageController.deletePassage(404);
        assertNull(response.getBody());
    }

}
