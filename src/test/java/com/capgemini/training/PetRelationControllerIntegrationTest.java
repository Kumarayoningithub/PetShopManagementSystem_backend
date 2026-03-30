package com.capgemini.training;

import com.capgemini.training.controller.PetRelationController;
import com.capgemini.training.dto.EmployeeDTO;
import com.capgemini.training.dto.GroomingServiceDTO;
import com.capgemini.training.dto.PetDTO;
import com.capgemini.training.dto.VaccinationDTO;
import com.capgemini.training.exception.GlobalExceptionHandler;
import com.capgemini.training.service.EmployeeService;
import com.capgemini.training.service.GroomingServiceService;
import com.capgemini.training.service.PetService;
import com.capgemini.training.service.VaccinationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetRelationController.class)
@Import(GlobalExceptionHandler.class)
class PetRelationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private GroomingServiceService groomingService;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private VaccinationService vaccinationService;

    private PetDTO buildPet(int id, String name, String breed) {
        PetDTO dto = new PetDTO();
        dto.setPetId(id);
        dto.setName(name);
        dto.setBreed(breed);
        dto.setAge(2);
        dto.setPrice(new BigDecimal("45000.00"));
        dto.setCategoryName("Dogs");
        return dto;
    }

    private GroomingServiceDTO buildGrooming(int id, String name) {
        GroomingServiceDTO g = new GroomingServiceDTO();
        g.setServiceId(id);
        g.setName(name);
        g.setDescription("Full service");
        g.setPrice(2500.0);
        g.setAvailable(true);
        return g;
    }

    private VaccinationDTO buildVaccination(int id, String name) {
        VaccinationDTO v = new VaccinationDTO();
        v.setVaccinationId(id);
        v.setName(name);
        v.setDescription("Core");
        v.setPrice(1500.0);
        v.setAvailable(true);
        return v;
    }

    private EmployeeDTO buildEmployee(int id, String first, String last) {
        EmployeeDTO e = new EmployeeDTO();
        e.setEmployeeId(id);
        e.setFirstName(first);
        e.setLastName(last);
        e.setPosition("Veterinarian");
        e.setHireDate(LocalDate.of(2024, 1, 15));
        e.setPhoneNumber("9876541230");
        e.setEmail("vet@example.com");
        return e;
    }

    @Test
    @DisplayName("Jackson ObjectMapper is available for DTO serialization")
    void objectMapper_isInjected() {
        assertNotNull(objectMapper);
    }

    @Test
    @DisplayName("POST assign food returns PetDTO 200")
    void assignFood_success_returns200() throws Exception {
        when(petService.assignFood(1, 10)).thenReturn(buildPet(1, "Tommy", "Golden Retriever"));

        mockMvc.perform(post("/pets/1/foods/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.name").value("Tommy"));
    }

    @Test
    @DisplayName("POST assign food when pet not found returns 404")
    void assignFood_failure_returns404() throws Exception {
        when(petService.assignFood(99, 1)).thenThrow(new RuntimeException("Pet not found"));

        mockMvc.perform(post("/pets/99/foods/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST assign supplier returns PetDTO 200")
    void assignSupplier_success_returns200() throws Exception {
        when(petService.assignSupplier(1, 5)).thenReturn(buildPet(1, "Bruno", "German Shepherd"));

        mockMvc.perform(post("/pets/1/suppliers/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.breed").value("German Shepherd"));
    }

    @Test
    @DisplayName("POST assign supplier when supplier not found returns 404")
    void assignSupplier_failure_returns404() throws Exception {
        when(petService.assignSupplier(1, 99)).thenThrow(new RuntimeException("Supplier not found"));

        mockMvc.perform(post("/pets/1/suppliers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST assign grooming service success returns message")
    void assignService_success_returns200() throws Exception {
        doNothing().when(groomingService).assignService(1, 2);

        mockMvc.perform(post("/pets/1/services/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service assigned"));
    }

    @Test
    @DisplayName("POST assign grooming service failure returns 404")
    void assignService_failure_returns404() throws Exception {
        doThrow(new RuntimeException("Service not found")).when(groomingService).assignService(1, 99);

        mockMvc.perform(post("/pets/1/services/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET services by pet returns 200")
    void getServicesByPet_returns200() throws Exception {
        when(groomingService.getServicesByPet(1)).thenReturn(List.of(buildGrooming(1, "Full Grooming")));

        mockMvc.perform(get("/pets/1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Full Grooming"));
    }

    @Test
    @DisplayName("GET services by pet empty returns 200")
    void getServicesByPet_empty_returns200() throws Exception {
        when(groomingService.getServicesByPet(2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pets/2/services"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET services by pet when pet not found returns 404")
    void getServicesByPet_notFound_returns404() throws Exception {
        when(groomingService.getServicesByPet(99)).thenThrow(new RuntimeException("Pet not found"));

        mockMvc.perform(get("/pets/99/services"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST assign vaccination success returns message")
    void assignVaccination_success_returns200() throws Exception {
        doNothing().when(vaccinationService).assignToPet(1, 3);

        mockMvc.perform(post("/pets/1/vaccinations/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vaccination assigned to pet"));
    }

    @Test
    @DisplayName("POST assign vaccination failure returns 404")
    void assignVaccination_failure_returns404() throws Exception {
        doThrow(new RuntimeException("Vaccination not found")).when(vaccinationService).assignToPet(1, 99);

        mockMvc.perform(post("/pets/1/vaccinations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET vaccinations by pet returns 200")
    void getVaccinationsByPet_returns200() throws Exception {
        when(vaccinationService.getVaccinationsByPet(1)).thenReturn(List.of(buildVaccination(1, "Rabies Vaccine")));

        mockMvc.perform(get("/pets/1/vaccinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rabies Vaccine"));
    }

    @Test
    @DisplayName("POST assign employee to pet success returns message")
    void assignEmployeeToPet_success_returns200() throws Exception {
        doNothing().when(employeeService).assignEmployeeToPet(1, 4);

        mockMvc.perform(post("/pets/1/employees/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee assigned"));
    }

    @Test
    @DisplayName("POST assign employee to pet failure returns 404")
    void assignEmployeeToPet_failure_returns404() throws Exception {
        doThrow(new RuntimeException("Employee not found")).when(employeeService).assignEmployeeToPet(1, 99);

        mockMvc.perform(post("/pets/1/employees/99"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("GET employees by pet returns 200")
    void getEmployeesByPet_returns200() throws Exception {
        when(employeeService.getEmployeesByPet(1)).thenReturn(List.of(buildEmployee(1, "Priya", "Sen")));

        mockMvc.perform(get("/pets/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Priya"));
    }

    @Test
    @DisplayName("GET employees by pet when pet not found returns 404")
    void getEmployeesByPet_notFound_returns404() throws Exception {
        when(employeeService.getEmployeesByPet(99)).thenThrow(new RuntimeException("Pet not found"));

        mockMvc.perform(get("/pets/99/employees"))
                .andExpect(status().isNotFound());
    }
}
