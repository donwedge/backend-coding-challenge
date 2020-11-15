package com.journi.challenge.repositories;

import com.journi.challenge.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByTimestampGreaterThanOrderByTimestampDesc(LocalDateTime timestamp);
}