package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, Long> {
    List<Bouquet> findByNameContainingIgnoreCase(String name);
    List<Bouquet> findByCategory(String category);
    List<Bouquet> findTop5ByFeaturedTrueOrderByCreatedAtDesc();

    @Query("SELECT b FROM Bouquet b ORDER BY b.createdAt DESC LIMIT :limit")
    List<Bouquet> findLatestBouquets(@Param("limit") int limit);

    // New methods
    List<Bouquet> findByPriceLessThanEqual(BigDecimal maxPrice);
    List<Bouquet> findByPriceGreaterThan(BigDecimal minPrice);

    List<Bouquet> findByOccasion(String occasion);

}