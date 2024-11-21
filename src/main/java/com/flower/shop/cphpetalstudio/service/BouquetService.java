package com.flower.shop.cphpetalstudio.service;


import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.BouquetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BouquetService {

    @Autowired
    private BouquetRepository bouquetRepository;

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findAll();
    }

    public Bouquet getBouquetById(Long id) {
        return bouquetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bouquet not found with id: " + id));
    }

    public List<Bouquet> searchBouquets(String name) {
        return bouquetRepository.findByNameContainingIgnoreCase(name);
    }

    public Bouquet createBouquet(Bouquet bouquet) {
        return bouquetRepository.save(bouquet);
    }

    public Bouquet updateBouquet(Long id, Bouquet bouquetDetails) {
        Bouquet bouquet = getBouquetById(id);
        bouquet.setName(bouquetDetails.getName());
        bouquet.setDescription(bouquetDetails.getDescription());
        bouquet.setPrice(bouquetDetails.getPrice());
        bouquet.setImageUrl(bouquetDetails.getImageUrl());
        return bouquetRepository.save(bouquet);
    }

    public void deleteBouquet(Long id) {
        bouquetRepository.deleteById(id);
    }
}