package com.lascenzi.api.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lascenzi.api.entity.Transaction;
import com.lascenzi.api.service.PaymentService;

@RestController
@RequestMapping("/payments/v1")
public class PaymentController {
    
    private final PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService)
    {
        this.paymentService = paymentService;
    }

    @GetMapping("/get-all-payments")
    public String getAllPayments()
    {
        StringBuilder sb = new StringBuilder();
        List<Transaction> transactions = paymentService.getAllTransactions();

    String format = "| %-36s | %-15s | %-15s | %-27s |%n";         
    String line = "----------------------------------------------------------------------------------------------------------" + System.lineSeparator();           


        sb.append("<pre style='font-family: monospace;'>");
        sb.append(line);
        sb.append(String.format(format, "Transaction ID", "Amount", "Status", "Created At"));
        sb.append(line);

        // 3. Print Rows
        for(Transaction tr : transactions)
        {
            sb.append(String.format(format,tr.getTransaction_id(), tr.getAmount(), tr.getStatus(), tr.getCreatedAt()));
        }
        
        sb.append("</pre>");

        return sb.toString();
    }

    @PostMapping("/make-payment")
    public String createPayment(@RequestBody Transaction transaction)
    {
        UUID uuid = UUID.randomUUID();
        transaction.setTransaction_id(uuid);
        Transaction saved = paymentService.createPayment(transaction);
        if (saved != null)
        {
            return "Saved: " + saved.getTransaction_id();
        }
        return "Error";
    }
}
