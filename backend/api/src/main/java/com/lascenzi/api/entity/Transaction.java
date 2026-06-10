package com.lascenzi.api.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Matches Postgres SERIAL/BIGSERIAL
    private Long id;

    @Column(name="user_id", nullable = false)
    private String user_id;

    @Column(name="transaction_id")
    private UUID transaction_id;

    @Column(nullable = false, precision = 12, scale = 2) 
    private BigDecimal amount;

    @Column(name="status", nullable = false)
    private String status = "pending";

    @Column(name = "created_at", insertable = false, updatable = false) 
    private OffsetDateTime createdAt;

    protected Transaction() {}

    public Transaction(String user_id, UUID transaction_id, BigDecimal amount, String status) 
    {
        this.user_id = user_id;
        this.transaction_id = transaction_id;
        this.amount = amount;
        this.status = status;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public String getUser_id() 
    {
        return user_id;
    }

    public void setUser_id(String user_id) 
    {
        this.user_id = user_id;
    }

    public UUID getTransaction_id() 
    {
        return transaction_id;
    }

    public void setTransaction_id(UUID transaction_id) 
    {
        this.transaction_id = transaction_id;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() 
    {
        return createdAt;
    }

}
