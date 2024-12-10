package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.BouquetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BouquetService {

    private final BouquetRepository bouquetRepository;

    @Autowired
    public BouquetService(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findAll();
    }

    public Bouquet getBouquetById(Long id) {
        return bouquetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bouquet not found with id: " + id));
    }

    public List<Bouquet> getFeaturedBouquets() {
        return bouquetRepository.findTop5ByFeaturedTrueOrderByCreatedAtDesc();
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
        bouquet.setFeatured(bouquetDetails.isFeatured());
        bouquet.setStockQuantity(bouquetDetails.getStockQuantity()); // Update stock quantity
        return bouquetRepository.save(bouquet);
    }

    public void deleteBouquet(Long id) {
        bouquetRepository.deleteById(id);
    }

    public List<Bouquet> getBouquetsByCategory(String category) {
        return bouquetRepository.findByCategory(category);
    }

    public List<Bouquet> getLatestBouquets(int limit) {
        return bouquetRepository.findLatestBouquets(limit);
    }

    public void updateBouquetStock(Long id, int quantity) {
        Bouquet bouquet = getBouquetById(id);
        if (bouquet.getStockQuantity() + quantity < 0) {
            throw new RuntimeException("Stock cannot be negative for bouquet: " + bouquet.getName());
        }
        bouquet.setStockQuantity(bouquet.getStockQuantity() + quantity);
        bouquetRepository.save(bouquet);
    }
}
