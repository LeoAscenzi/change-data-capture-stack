package com.lascenzi;

import com.lascenzi.DebeziumDeserializer;
import com.lascenzi.functions.FastTransactionFunction;
import com.lascenzi.model.Transaction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.common.serialization.SimpleStringSchema;



public class TransactionPatternJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");

        KafkaSource<Transaction> source = KafkaSource.<Transaction>builder()
            .setBootstrapServers(bootstrapServers)
            .setTopics("cdc.public.transactions")
            .setGroupId("flink-cdc-group")
            // .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new DebeziumDeserializer())
            .setStartingOffsets(OffsetsInitializer.latest()) 
            .setProperty("topic.metadata.refresh.interval.ms", "10000") 
            .build();

        DataStream<String> suspiciousUserIds = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka CDC Source")
            .filter(tx -> tx != null)
            .keyBy(tx -> tx.userId)
            .process(new FastTransactionFunction());

        KafkaSink<String> bannedSink = KafkaSink.<String>builder()
            .setBootstrapServers("kafka:9092")
            .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic("banned-users")
                .setValueSerializationSchema(new SimpleStringSchema())
                .build())
            .build();

        suspiciousUserIds.print();
        suspiciousUserIds.sinkTo(bannedSink);


        env.setBufferTimeout(0); 
        env.execute("Transaction Fraud Detector");
    }
}