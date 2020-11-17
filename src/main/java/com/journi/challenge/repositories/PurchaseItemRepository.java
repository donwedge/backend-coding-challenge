package com.journi.challenge.repositories;

import com.journi.challenge.models.PurchaseItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItems, Integer> {
}
