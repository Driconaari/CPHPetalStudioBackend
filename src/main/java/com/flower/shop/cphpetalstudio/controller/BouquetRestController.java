package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
@CrossOrigin
@RequestMapping("/api/bouquets")
public class BouquetRestController {

    @Autowired
    private BouquetService bouquetService;
    private static final Logger logger = Logger.getLogger(BouquetRestController.class.getName());

    /*
    @GetMapping
    public ResponseEntity<List<Bouquet>> getAllBouquets() {
        logger.info("Fetching all bouquets");
        return ResponseEntity.ok(bouquetService.getAllBouquets());
    }

     */
    @GetMapping("/{id}")
    public ResponseEntity<Bouquet> getBouquetById(@PathVariable Long id) {
        return ResponseEntity.ok(bouquetService.getBouquetById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Bouquet>> searchBouquets(@RequestParam String name) {
        return ResponseEntity.ok(bouquetService.searchBouquets(name));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Bouquet>> getBouquetsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bouquetService.getBouquetsByCategory(category));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Bouquet>> getLatestBouquets(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(bouquetService.getLatestBouquets(limit));

    }

    @GetMapping
    public ResponseEntity<List<Bouquet>> getAllBouquets() {
        return ResponseEntity.ok(bouquetService.getAllBouquets());
    }


}