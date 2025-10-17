package com.campus.wallet.controller;

import com.campus.wallet.dto.StudentDTO;
import com.campus.wallet.entity.Student;
import com.campus.wallet.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;

    @GetMapping
    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(s -> StudentDTO.builder()
                        .admissionNo(s.getAdmissionNo())
                        .name(s.getName())
                        .department(s.getDepartment())
                        .email(s.getEmail())
                        .balance(s.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/{admissionNo}")
    public ResponseEntity<StudentDTO> get(@PathVariable String admissionNo) {
        return studentRepository.findById(admissionNo)
                .map(s -> ResponseEntity.ok(StudentDTO.builder()
                        .admissionNo(s.getAdmissionNo())
                        .name(s.getName())
                        .department(s.getDepartment())
                        .email(s.getEmail())
                        .balance(s.getBalance())
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StudentDTO create(@RequestBody StudentDTO dto) {
        Student s = Student.builder()
                .admissionNo(dto.getAdmissionNo())
                .name(dto.getName())
                .department(dto.getDepartment())
                .email(dto.getEmail())
                .balance(dto.getBalance())
                .build();
        studentRepository.save(s);
        return dto;
    }

    @PutMapping("/{admissionNo}")
    public ResponseEntity<StudentDTO> update(@PathVariable String admissionNo, @RequestBody StudentDTO dto) {
        return studentRepository.findById(admissionNo)
                .map(s -> {
                    s.setName(dto.getName());
                    s.setDepartment(dto.getDepartment());
                    s.setEmail(dto.getEmail());
                    s.setBalance(dto.getBalance());
                    studentRepository.save(s);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{admissionNo}")
    public ResponseEntity<Void> delete(@PathVariable String admissionNo) {
        if (studentRepository.existsById(admissionNo)) {
            studentRepository.deleteById(admissionNo);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
