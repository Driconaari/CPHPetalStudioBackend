package com.flower.shop.cphpetalstudio.service;

import com.example.flowershop.model.Bouquet;
import com.example.flowershop.repository.BouquetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BouquetService {

    @Autowired
    private com.flower.shop.cphpetalstudio.repository.BouquetRepository bouquetRepository;

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findAll();
    }

    public Bouquet getBouquetById(Long id) {
        return bouquetRepository.findById(id).orElseThrow(() -> new RuntimeException("Bouquet not found"));
    }

    public Bouquet saveBouquet(Bouquet bouquet) {
        return bouquetRepository.save(bouquet);
    }

    public void deleteBouquet(Long id) {
        bouquetRepository.deleteById(id);
    }
}