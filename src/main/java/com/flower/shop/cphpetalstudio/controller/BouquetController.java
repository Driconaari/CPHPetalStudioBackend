package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bouquets")
public class BouquetController {

    @Autowired
    private BouquetService bouquetService;

    // GET all bouquets (accessible by both normal users and admins)
    @GetMapping
    public List<Bouquet> getAllBouquets() {
        return bouquetService.getAllBouquets();
    }

    // GET bouquet by ID (accessible by both normal users and admins)
    @GetMapping("/{id}")
    public Bouquet getBouquetById(@PathVariable Long id) {
        return bouquetService.getBouquetById(id);
    }

    // Search bouquets (accessible by both normal users and admins)
    @GetMapping("/search")
    public List<Bouquet> searchBouquets(@RequestParam String name) {
        return bouquetService.searchBouquets(name);
    }

    // Get bouquets by category (accessible by both normal users and admins)
    @GetMapping("/category/{category}")
    public List<Bouquet> getBouquetsByCategory(@PathVariable String category) {
        return bouquetService.getBouquetsByCategory(category);
    }

    // Get latest bouquets (accessible by both normal users and admins)
    @GetMapping("/latest")
    public List<Bouquet> getLatestBouquets(@RequestParam(defaultValue = "5") int limit) {
        return bouquetService.getLatestBouquets(limit);
    }

    // Admin only: Create a new bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Bouquet createBouquet(@RequestBody Bouquet bouquet) {
        return bouquetService.createBouquet(bouquet);
    }

    // Admin only: Update a bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Bouquet updateBouquet(@PathVariable Long id, @RequestBody Bouquet bouquetDetails) {
        return bouquetService.updateBouquet(id, bouquetDetails);
    }

    // Admin only: Delete a bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
    }
}
