package com.journi.challenge.repositories;

import com.journi.challenge.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByTimestampGreaterThanOrderByTimestampDesc(Timestamp timestamp);

    @Query("SELECT MIN(totalValue) as minAmount, MAX(totalValue) as maxAmount, MIN(p.timestamp) as minTime, MAX(p.timestamp)" +
            " as maxTime, COUNT(*) as numberPurchases, SUM(totalValue) as totalAmount, AVG(totalValue) as avgPurchase" +
            " FROM purchase p WHERE timestamp > ?1")
    TimestampQuery findByTimestampGreaterThan(String timestamp);

    class TimestampQuery {
        private Double minAmount;
        private Double maxAmount;
        private Timestamp minTime;
        private Timestamp maxTime;
        private Long numberPurchases;
        private Double totalAmount;
        private Double avgPurchase;

        public TimestampQuery() {
        }

        public Double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(Double minAmount) {
            this.minAmount = minAmount;
        }

        public Double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(Double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public Timestamp getMinTime() {
            return minTime;
        }

        public void setMinTime(Timestamp minTime) {
            this.minTime = minTime;
        }

        public Timestamp getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(Timestamp maxTime) {
            this.maxTime = maxTime;
        }

        public Long getNumberPurchases() {
            return numberPurchases;
        }

        public void setNumberPurchases(Long numberPurchases) {
            this.numberPurchases = numberPurchases;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Double getAvgPurchase() {
            return avgPurchase;
        }

        public void setAvgPurchase(Double avgPurchase) {
            this.avgPurchase = avgPurchase;
        }
    }
}
