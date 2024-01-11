package com.emse.spring.automacorp.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void whenLoggedAsUser_thenCanAccessApiAndNotConsole() throws Exception {
        mockMvc.perform(get("/api/buildings"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/console/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void whenLoggedAsAdmin_thenCanAccessApiAndConsole() throws Exception {
        mockMvc.perform(get("/api/buildings"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/console/"))
                .andExpect(status().isNotFound()); //If this is not forbidden, then the ADMIN can access the console.
    }

}
