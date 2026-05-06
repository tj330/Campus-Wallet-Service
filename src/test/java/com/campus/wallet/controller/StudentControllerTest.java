package com.campus.wallet.controller;

import com.campus.wallet.entity.Student;
import com.campus.wallet.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(com.campus.wallet.config.SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_shouldReturnListOfStudents() throws Exception {
        Student student = Student.builder()
                .admissionNo("ST001")
                .name("Rahul")
                .department("CS")
                .email("rahul@campus.edu")
                .balance(1000.0)
                .build();
        when(studentRepository.findAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].admissionNo").value("ST001"))
                .andExpect(jsonPath("$[0].name").value("Rahul"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_shouldReturnStudent() throws Exception {
        Student student = Student.builder()
                .admissionNo("ST001")
                .name("Rahul")
                .department("CS")
                .email("rahul@campus.edu")
                .balance(1000.0)
                .build();
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/ST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admissionNo").value("ST001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(studentRepository.findById("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/INVALID"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_shouldReturnCreatedStudent() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(Student.builder()
                .admissionNo("ST001")
                .name("Rahul")
                .department("CS")
                .email("rahul@campus.edu")
                .balance(1000.0)
                .build());

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "admissionNo": "ST001",
                        "name": "Rahul",
                        "department": "CS",
                        "email": "rahul@campus.edu",
                        "balance": 1000.0
                    }
                    """)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admissionNo").value("ST001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_shouldReturnUpdatedStudent() throws Exception {
        Student student = Student.builder()
                .admissionNo("ST001")
                .name("Rahul")
                .department("CS")
                .email("rahul@campus.edu")
                .balance(1000.0)
                .build();
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/students/ST001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "admissionNo": "ST001",
                        "name": "rahul Updated",
                        "department": "IT",
                        "email": "rahul@campus.edu",
                        "balance": 1500.0
                    }
                    """)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("rahul Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturn204() throws Exception {
        when(studentRepository.existsById("ST001")).thenReturn(true);

        mockMvc.perform(delete("/students/ST001")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAll_shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isUnauthorized());
    }
}
