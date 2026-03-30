package com.capgemini.training;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ADDRESS_BODY = """
            {
              "street": "12 Park Street",
              "city": "Kolkata",
              "state": "West Bengal",
              "zipCode": "700016"
            }
            """;

    @Test
    @Order(1)
    void addressEndpoints() throws Exception {
        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ADDRESS_BODY))
                .andExpect(status().isOk());

        mockMvc.perform(get("/address")).andExpect(status().isOk());
        mockMvc.perform(get("/address/1")).andExpect(status().isOk());

        String updateBody = """
                {
                  "street": "45 Salt Lake Sector V",
                  "city": "Kolkata",
                  "state": "West Bengal",
                  "zipCode": "700091"
                }
                """;
        mockMvc.perform(put("/address/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void categoryEndpoints() throws Exception {
        mockMvc.perform(post("/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Dogs\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/categories")).andExpect(status().isOk());
        mockMvc.perform(get("/categories/name/{name}", "Dogs")).andExpect(status().isOk());
        mockMvc.perform(get("/categories/1")).andExpect(status().isOk());

        mockMvc.perform(put("/categories/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Cats\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/categories/name/{name}", "Cats")).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void groomingServiceEndpoints() throws Exception {
        String body = """
                {
                  "name": "Full Grooming",
                  "description": "Bath, nail trimming and hair cutting",
                  "price": 2500.00,
                  "available": true
                }
                """;
        mockMvc.perform(post("/services/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/services")).andExpect(status().isOk());
        mockMvc.perform(get("/services")
                        .param("minPrice", "1000")
                        .param("maxPrice", "5000"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/services/available")).andExpect(status().isOk());
        mockMvc.perform(get("/services/unavailable")).andExpect(status().isOk());
        mockMvc.perform(get("/services/1")).andExpect(status().isOk());

        String update = """
                {
                  "name": "Basic Grooming",
                  "description": "Bath and nail trimming",
                  "price": 1500.00,
                  "available": true
                }
                """;
        mockMvc.perform(put("/services/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void supplierEndpoints() throws Exception {
        String body = """
                {
                  "name": "Pet World Supplier",
                  "contactPerson": "Ramesh Gupta",
                  "phoneNumber": "9988776655",
                  "email": "supplier@example.com",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(post("/suppliers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/suppliers")).andExpect(status().isOk());
        mockMvc.perform(get("/suppliers/name/{name}", "Pet World Supplier")).andExpect(status().isOk());
        mockMvc.perform(get("/suppliers/city/{city}", "Kolkata")).andExpect(status().isOk());
        mockMvc.perform(get("/suppliers/state/{state}", "West Bengal")).andExpect(status().isOk());
        mockMvc.perform(get("/suppliers/1")).andExpect(status().isOk());

        String update = """
                {
                  "name": "Royal Pet Supplier",
                  "contactPerson": "Suresh Kumar",
                  "phoneNumber": "9871234560",
                  "email": "royalpet@example.com",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(put("/suppliers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void petFoodEndpoints() throws Exception {
        String body = """
                {
                  "name": "Pedigree Adult Dog Food",
                  "brand": "Pedigree",
                  "type": "Dry Food",
                  "quantity": 50,
                  "price": 1200.00
                }
                """;
        mockMvc.perform(post("/pet_foods/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pet_foods")).andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods")
                        .param("minPrice", "500")
                        .param("maxPrice", "2000"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods")
                        .param("minQuantity", "10")
                        .param("maxQuantity", "100"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods/search").param("name", "Pedigree")).andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods/food_type/{type}", "Dry Food")).andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods/brand/{brand}", "Pedigree")).andExpect(status().isOk());
        mockMvc.perform(get("/pet_foods/1")).andExpect(status().isOk());

        String update = """
                {
                  "name": "Whiskas Cat Food",
                  "brand": "Whiskas",
                  "type": "Wet Food",
                  "quantity": 30,
                  "price": 900.00
                }
                """;
        mockMvc.perform(put("/pet_foods/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void vaccinationEndpoints() throws Exception {
        String body = """
                {
                  "name": "Rabies Vaccine",
                  "description": "Protection against rabies",
                  "price": 1500.00,
                  "available": true
                }
                """;
        mockMvc.perform(post("/vaccinations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/vaccinations")).andExpect(status().isOk());
        mockMvc.perform(get("/vaccinations")
                        .param("minPrice", "500")
                        .param("maxPrice", "3000"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/vaccinations/available")).andExpect(status().isOk());
        mockMvc.perform(get("/vaccinations/unavailable")).andExpect(status().isOk());
        mockMvc.perform(get("/vaccinations/1")).andExpect(status().isOk());

        String update = """
                {
                  "name": "Distemper Vaccine",
                  "description": "Protection against distemper",
                  "price": 1800.00,
                  "available": true
                }
                """;
        mockMvc.perform(put("/vaccinations/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void employeeEndpoints() throws Exception {
        String body = """
                {
                  "firstName": "Rohit",
                  "lastName": "Das",
                  "position": "Veterinarian",
                  "hireDate": "2024-01-15",
                  "phoneNumber": "9876541230",
                  "email": "rohit@example.com",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employees")).andExpect(status().isOk());
        mockMvc.perform(get("/employees")
                        .param("minHireDate", "2024-01-01")
                        .param("maxHireDate", "2026-12-31"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/employees/name/{name}", "Rohit")).andExpect(status().isOk());
        mockMvc.perform(get("/employees/position/{position}", "Veterinarian")).andExpect(status().isOk());
        mockMvc.perform(get("/employees/1")).andExpect(status().isOk());

        String update = """
                {
                  "firstName": "Ankit",
                  "lastName": "Roy",
                  "position": "Pet Groomer",
                  "hireDate": "2025-02-20",
                  "phoneNumber": "9988123456",
                  "email": "ankit@example.com",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(put("/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void customerEndpoints_beforeTransaction() throws Exception {
        String body = """
                {
                  "firstName": "Ayon",
                  "lastName": "Saha",
                  "email": "ayon@example.com",
                  "phoneNumber": "9876543210",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(post("/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/customers")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/no-transactions")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/name/{first}/{last}", "Ayon", "Saha")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/by_city/{city}", "Kolkata")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/by_state/{state}", "West Bengal")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/1")).andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void petEndpoints() throws Exception {
        String body = """
                {
                  "name": "Tommy",
                  "breed": "Golden Retriever",
                  "age": 2,
                  "price": 45000.00,
                  "description": "Friendly and active dog",
                  "imageUrl": "https://example.com/dog1.jpg",
                  "category": {
                    "categoryId": 1
                  }
                }
                """;
        mockMvc.perform(post("/pets/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pets")).andExpect(status().isOk());
        mockMvc.perform(get("/pets")
                        .param("minPrice", "10000")
                        .param("maxPrice", "50000"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/pets")
                        .param("minAge", "1")
                        .param("maxAge", "5"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pets/food_info/1")).andExpect(status().isOk());
        mockMvc.perform(get("/pets/suppliers/1")).andExpect(status().isOk());
        mockMvc.perform(get("/pets/transaction_history/1")).andExpect(status().isOk());
        mockMvc.perform(get("/pets/1/vaccinations")).andExpect(status().isOk());
        mockMvc.perform(get("/pets/1/employees")).andExpect(status().isOk());
        mockMvc.perform(get("/pets/1/services")).andExpect(status().isOk());

        String update = """
                {
                  "name": "Bruno",
                  "breed": "German Shepherd",
                  "age": 3,
                  "price": 55000.00,
                  "description": "Strong and loyal dog",
                  "imageUrl": "https://example.com/dog2.jpg",
                  "category": {
                    "categoryId": 1
                  }
                }
                """;
        mockMvc.perform(put("/pets/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void transactionHistoryEndpoints() throws Exception {
        String body = """
                {
                  "customerId": 1,
                  "petId": 1,
                  "transactionDate": "2026-03-28",
                  "amount": 45000.00,
                  "status": "Success"
                }
                """;
        mockMvc.perform(post("/transaction_history/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/transaction_history")).andExpect(status().isOk());
        mockMvc.perform(get("/transaction_history")
                        .param("minDate", "2026-01-01")
                        .param("maxDate", "2026-12-31"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/transaction_history/by_customer/1")).andExpect(status().isOk());
        mockMvc.perform(get("/transaction_history/success")).andExpect(status().isOk());
        mockMvc.perform(get("/transaction_history/failed")).andExpect(status().isOk());
        mockMvc.perform(get("/transaction_history/1")).andExpect(status().isOk());
    }

    @Test
    @Order(11)
    void customerEndpoints_afterTransaction() throws Exception {
        mockMvc.perform(get("/customers/transactions/1")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/transactions_status/{status}", "Success")).andExpect(status().isOk());
        mockMvc.perform(get("/customers/pets/1")).andExpect(status().isOk());

        String update = """
                {
                  "firstName": "Rahul",
                  "lastName": "Sharma",
                  "email": "rahul@example.com",
                  "phoneNumber": "9123456780",
                  "address": {
                    "addressId": 1
                  }
                }
                """;
        mockMvc.perform(put("/customers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    void petRelationPosts() throws Exception {
        mockMvc.perform(post("/pets/1/services/1")).andExpect(status().isOk());
        mockMvc.perform(post("/pets/1/foods/1")).andExpect(status().isOk());
        mockMvc.perform(post("/pets/1/suppliers/1")).andExpect(status().isOk());
        mockMvc.perform(post("/pets/1/vaccinations/1")).andExpect(status().isOk());
        mockMvc.perform(post("/pets/1/employees/1")).andExpect(status().isOk());
    }

    @Test
    @Order(13)
    void postResponsesContainExpectedJson() throws Exception {
        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "street": "Test Lane",
                                  "city": "TestCity",
                                  "state": "TS",
                                  "zipCode": "11111"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").exists());

        mockMvc.perform(post("/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Birds\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").exists());
    }
}
