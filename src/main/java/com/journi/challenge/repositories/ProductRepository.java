package com.journi.challenge.repositories;

import com.journi.challenge.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;

@Entity
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
