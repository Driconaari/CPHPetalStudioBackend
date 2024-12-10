package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.ApiResponse;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final BouquetService bouquetService;

    @Autowired
    public ShopController(BouquetService bouquetService) {
        this.bouquetService = bouquetService;
    }

    @GetMapping
    public String getShopPage(@RequestParam(required = false) BigDecimal maxPrice,
                              @RequestParam(required = false) BigDecimal minPrice,
                              @RequestParam(required = false) String category,
                              Model model) {
        List<Bouquet> bouquets = bouquetService.getBouquets(maxPrice, minPrice, category);
        model.addAttribute("bouquets", bouquets);
        return "shop";
    }

    @GetMapping("/api/bouquets")
    @ResponseBody
    public ResponseEntity<List<Bouquet>> getAllBouquets(
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) String category) {
        List<Bouquet> bouquets = bouquetService.getBouquets(maxPrice, minPrice, category);
        return ResponseEntity.ok(bouquets);
    }

    @GetMapping("/api/bouquets/{id}")
    @ResponseBody
    public ResponseEntity<?> getBouquetById(@PathVariable Long id) {
        try {
            Bouquet bouquet = bouquetService.getBouquetById(id);
            return ResponseEntity.ok(bouquet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to retrieve bouquet: " + e.getMessage(), false));
        }
    }
}