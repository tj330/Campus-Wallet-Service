package com.campus.wallet.controller;

import com.campus.wallet.dto.StoreDTO;
import com.campus.wallet.entity.Store;
import com.campus.wallet.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreRepository storeRepository;

    @GetMapping
    public List<StoreDTO> getAll() {
        return storeRepository.findAll().stream()
                .map(s -> StoreDTO.builder()
                        .storeId(s.getStoreId())
                        .storeName(s.getStoreName())
                        .category(s.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> get(@PathVariable Integer storeId) {
        return storeRepository.findById(storeId)
                .map(s -> ResponseEntity.ok(StoreDTO.builder()
                        .storeId(s.getStoreId())
                        .storeName(s.getStoreName())
                        .category(s.getCategory())
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StoreDTO create(@RequestBody StoreDTO dto) {
        Store s = Store.builder()
                .storeName(dto.getStoreName())
                .category(dto.getCategory())
                .build();
        storeRepository.save(s);
        dto.setStoreId(s.getStoreId());
        return dto;
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreDTO> update(@PathVariable Integer storeId, @RequestBody StoreDTO dto) {
        return storeRepository.findById(storeId)
                .map(s -> {
                    s.setStoreName(dto.getStoreName());
                    s.setCategory(dto.getCategory());
                    storeRepository.save(s);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer storeId) {
        if (storeRepository.existsById(storeId)) {
            storeRepository.deleteById(storeId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
