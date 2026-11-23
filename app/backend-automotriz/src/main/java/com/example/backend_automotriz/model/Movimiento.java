package com.example.backend_automotriz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movements")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productCode;
    private String movementType;
    private int quantity;
    private long timestamp = System.currentTimeMillis();
    private String reason;
    private int previousStock;
    private int resultingStock;
    private String registeredByUserId;

    public Movimiento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getPreviousStock() { return previousStock; }
    public void setPreviousStock(int previousStock) { this.previousStock = previousStock; }
    public int getResultingStock() { return resultingStock; }
    public void setResultingStock(int resultingStock) { this.resultingStock = resultingStock; }
    public String getRegisteredByUserId() { return registeredByUserId; }
    public void setRegisteredByUserId(String registeredByUserId) { this.registeredByUserId = registeredByUserId; }
}