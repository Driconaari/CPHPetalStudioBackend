package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.FlowerType;
import com.flower.shop.cphpetalstudio.repository.FlowerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerTypeService {

    private final FlowerTypeRepository flowerTypeRepository;

    @Autowired
    public FlowerTypeService(FlowerTypeRepository flowerTypeRepository) {
        this.flowerTypeRepository = flowerTypeRepository;
    }

    public List<FlowerType> getAllFlowerTypes() {
        return flowerTypeRepository.findAll();
    }

    public FlowerType getFlowerTypeById(Long id) {
        return flowerTypeRepository.findById(id).orElse(null);
    }

    public FlowerType createFlowerType(FlowerType flowerType) {
        return flowerTypeRepository.save(flowerType);
    }

    public FlowerType updateFlowerType(Long id, FlowerType flowerType) {
        FlowerType existingFlowerType = flowerTypeRepository.findById(id).orElse(null);
        if (existingFlowerType != null) {
            existingFlowerType.setName(flowerType.getName());
            existingFlowerType.setSeason(flowerType.getSeason());
            existingFlowerType.setOccasion(flowerType.getOccasion());
            return flowerTypeRepository.save(existingFlowerType);
        }
        return null;
    }

    public void deleteFlowerType(Long id) {
        flowerTypeRepository.deleteById(id);
    }
}