package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bouquets")
public class BouquetController {

    @Autowired
    private BouquetService bouquetService;

    @GetMapping
    public List<Bouquet> getAllBouquets() {
        return bouquetService.getAllBouquets();
    }

    @GetMapping("/{id}")
    public Bouquet getBouquetById(@PathVariable Long id) {
        return bouquetService.getBouquetById(id);
    }

    @PostMapping
    public Bouquet createBouquet(@RequestBody Bouquet bouquet) {
        return bouquetService.createBouquet(bouquet);
    }

    @PutMapping("/{id}")
    public Bouquet updateBouquet(@PathVariable Long id, @RequestBody Bouquet bouquetDetails) {
        return bouquetService.updateBouquet(id, bouquetDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
    }

    @GetMapping("/search")
    public List<Bouquet> searchBouquets(@RequestParam String name) {
        return bouquetService.searchBouquets(name);
    }

    @GetMapping("/category/{category}")
    public List<Bouquet> getBouquetsByCategory(@PathVariable String category) {
        return bouquetService.getBouquetsByCategory(category);
    }

    @GetMapping("/latest")
    public List<Bouquet> getLatestBouquets(@RequestParam(defaultValue = "5") int limit) {
        return bouquetService.getLatestBouquets(limit);
    }
}