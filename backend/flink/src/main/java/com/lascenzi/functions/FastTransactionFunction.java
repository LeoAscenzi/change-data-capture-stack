package com.lascenzi.functions;

import com.lascenzi.model.Transaction;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction.Context;
import org.apache.flink.util.Collector;

public class FastTransactionFunction extends KeyedProcessFunction<String, Transaction, String> 
{
    private transient ValueState<Long> lastTransactionTimestamp;

    @Override
    public void open(OpenContext openContext) 
    {
        ValueStateDescriptor<Long> descriptor = new ValueStateDescriptor<>("lastTxTime", Long.class);
        lastTransactionTimestamp = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void processElement(Transaction currentTx, Context ctx, Collector<String> out) throws Exception 
    {
        Long currentTxTime = parseCreatedAt(currentTx.createdAt);
        Long lastTxTime = lastTransactionTimestamp.value();

        if (lastTxTime != null && currentTxTime != null) {
            long timeDifferenceSeconds = (currentTxTime - lastTxTime) / 1000;

            // Rule condition
            if (timeDifferenceSeconds < 10) { 
                out.collect(currentTx.userId);
            }
        }

        if (currentTxTime != null) {
            lastTransactionTimestamp.update(currentTxTime);
        }
    }

    private long parseCreatedAt(String createdAtStr) 
    {
        if (createdAtStr == null) {
            return 0L;
        }
        return java.time.Instant.parse(createdAtStr).toEpochMilli();
    }
}
