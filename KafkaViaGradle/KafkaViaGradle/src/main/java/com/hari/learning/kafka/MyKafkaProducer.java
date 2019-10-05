package com.hari.learning.kafka;

import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MyKafkaProducer<V> {

	public BiFunction<String, V, Producer<V, V>> getProducer() {
		return (brkr, serDe) -> {
			Properties props = new Properties();
			props.put("bootstrap.servers", brkr);
			props.put("key.serializer", serDe);
			props.put("value.serializer", serDe);
			return new KafkaProducer<V, V>(props);
		};
	}

	public BiFunction<String, V, ProducerRecord<V, V>> getProducerRec() {
		return (topic, value) -> new ProducerRecord<V, V>(topic, value);
	}

	public static <V> Supplier<MyKafkaProducer<V>> newInstance() {
		return () -> new MyKafkaProducer<V>();
	}

}

interface TriFunction<K, U, V, W> {

	public W apply(K k, U u, V v);
}