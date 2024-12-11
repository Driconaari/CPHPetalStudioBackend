package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.FlowerType;
import com.flower.shop.cphpetalstudio.service.FlowerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flower-types")
public class FlowerTypeController {

    private final FlowerTypeService flowerTypeService;

    @Autowired
    public FlowerTypeController(FlowerTypeService flowerTypeService) {
        this.flowerTypeService = flowerTypeService;
    }

    @GetMapping
    public List<FlowerType> getAllFlowerTypes() {
        return flowerTypeService.getAllFlowerTypes();
    }

    @GetMapping("/{id}")
    public FlowerType getFlowerTypeById(@PathVariable Long id) {
        return flowerTypeService.getFlowerTypeById(id);
    }

    @PostMapping
    public FlowerType createFlowerType(@RequestBody FlowerType flowerType) {
        return flowerTypeService.createFlowerType(flowerType);
    }

    @PutMapping("/{id}")
    public FlowerType updateFlowerType(@PathVariable Long id, @RequestBody FlowerType flowerType) {
        return flowerTypeService.updateFlowerType(id, flowerType);
    }

    @DeleteMapping("/{id}")
    public void deleteFlowerType(@PathVariable Long id) {
        flowerTypeService.deleteFlowerType(id);
    }
}