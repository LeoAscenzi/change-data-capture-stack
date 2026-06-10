package com.lascenzi;

import com.lascenzi.model.Transaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DebeziumDeserializer implements DeserializationSchema<Transaction> {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Transaction deserialize(byte[] message) throws IOException {
        if (message == null || message.length == 0) {
            return null;
        }

        try {
            JsonNode root = mapper.readTree(message);
            
            JsonNode payload = root.has("payload") ? root.get("payload") : root;
            if (payload == null || payload.isNull()) {
                System.out.println("Skipping record: Top-level payload missing.");
                return null;
            }

            JsonNode opNode = payload.get("op");
            if (opNode == null || opNode.isNull()) {
                System.out.println("Skipping record: Operation 'op' field missing.");
                return null;
            }
            
            String op = opNode.asText();
            if (!"c".equals(op) && !"r".equals(op)) {
                System.out.println("Skipping record: Operation is not insert/snapshot (" + op + ")");
                return null;
            }

            JsonNode after = payload.get("after");
            if (after == null || after.isNull()) {
                System.out.println("Skipping record: 'after' state data block is empty.");
                return null;
            }

            Transaction tx = new Transaction();
            
            tx.id = after.has("id") ? after.get("id").asLong() : null;
            tx.userId = after.has("user_id") ? after.get("user_id").asText() : null;
            tx.transactionId = after.has("transaction_id") && !after.get("transaction_id").isNull() ? after.get("transaction_id").asText() : null;
            tx.amount = after.has("amount") ? after.get("amount").asDouble() : 0.0;
            tx.status = after.has("status") && !after.get("status").isNull() ? after.get("status").asText() : "pending";
            tx.createdAt = after.has("created_at") && !after.get("created_at").isNull() ? after.get("created_at").asText() : null;

            System.out.println("Transaction processed! " + tx);
            return tx;

        } catch (Exception e) {
            System.err.println("Failed to parse Kafka record. Error: " + e.getMessage());
            return null; 
        }
    }

    @Override
    public boolean isEndOfStream(Transaction nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Transaction> getProducedType() {
        return TypeInformation.of(Transaction.class);
    }
}
