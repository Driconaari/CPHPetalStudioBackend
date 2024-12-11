package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.FlowerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerTypeRepository extends JpaRepository<FlowerType, Long> {
}