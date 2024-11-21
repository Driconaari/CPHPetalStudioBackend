package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bouquets")
public class BouquetController {

    @Autowired
    private BouquetService bouquetService;

    @GetMapping
    public ResponseEntity<List<Bouquet>> getAllBouquets() {
        return ResponseEntity.ok(bouquetService.getAllBouquets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bouquet> getBouquetById(@PathVariable Long id) {
        return ResponseEntity.ok(bouquetService.getBouquetById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bouquet> createBouquet(@RequestBody Bouquet bouquet) {
        return ResponseEntity.ok(bouquetService.createBouquet(bouquet));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bouquet> updateBouquet(@PathVariable Long id, @RequestBody Bouquet bouquetDetails) {
        return ResponseEntity.ok(bouquetService.updateBouquet(id, bouquetDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
        return ResponseEntity.ok().build();
    }
}