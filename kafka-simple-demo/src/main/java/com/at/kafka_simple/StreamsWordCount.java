package com.at.kafka_simple;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamsWordCount {
    /**
     * Howto:
     * <ul>
     * <li>Create topics
     *      <ul>
     *      <li>kafka-topics --zookeeper localhost:2181/kafka --create --topic wordcount-input --replication-factor 1 --partitions 3
     *      </li>
     *      
     *      <li>kafka-topics --zookeeper localhost:2181/kafka --create --topic wordcount-output --replication-factor 1 --partitions 3
     *      </li>
     *      </ul>
     * </li>
     * <li>Run This Program
     * </li>
     * <li>Run Producer/Consumer
     *      <ul>
     *      <li>kafka-console-consumer --bootstrap-server localhost:9092 --topic wordcount-output --value-deserializer org.apache.kafka.common.serialization.LongDeserializer --property print.key=true --property key.separator=" : "
     *      </li>
     *      <li>kafka-console-producer --broker-list localhost:9092 --topic wordcount-input
     *      <pre><code>>a b c d e f g h i j k l m n
>who are you indeed ?
>a b c d e f g
>x y z
>who are you !
>i don't know
>
</code></pre>
     *      </li>
     *      
     *      </ul>
     * </li>
     * </ul>
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "application-id-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("wordcount-input");
        
        final Pattern pattern = Pattern.compile("\\W+");
        
        KStream<String, Long> counts = 
                        source
                            .flatMapValues(value -> Arrays.asList( pattern.split(value.toLowerCase()) ))
                            .map((key, value) -> new KeyValue<String, String>(value, value))
                            .filter((key, value) -> (!key.equals("the")))
                            .groupByKey()
                            .count(Materialized.as("CountStore"))
//                            .mapValues(c -> Long.toString(c))
                            .toStream()
                            ;
        counts.to("wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        log.info("streams starting...");
        streams.start();
        log.info("started");
        
        
//        Thread.sleep(60000L);
//        
//        
//        streams.close(10, TimeUnit.SECONDS);
    }
}
