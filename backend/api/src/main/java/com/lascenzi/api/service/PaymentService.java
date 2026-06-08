package com.lascenzi.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lascenzi.api.repository.TransactionRepository;
import com.lascenzi.api.entity.Transaction;

@Service
public class PaymentService {
    
    private TransactionRepository transactionRepository;

    public PaymentService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll(); 
    }

    public Transaction createPayment(Transaction transaction)
    {
        return transactionRepository.save(transaction);
    }
}
