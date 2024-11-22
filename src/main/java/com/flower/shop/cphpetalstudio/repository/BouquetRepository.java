package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, Long> {

    List<Bouquet> findByNameContainingIgnoreCase(String name);

    List<Bouquet> findTop5ByFeaturedTrueOrderByCreatedAtDesc();

    List<Bouquet> findByCategory(String category);

    List<Bouquet> findTopNByOrderByCreatedAtDesc(int limit);
}