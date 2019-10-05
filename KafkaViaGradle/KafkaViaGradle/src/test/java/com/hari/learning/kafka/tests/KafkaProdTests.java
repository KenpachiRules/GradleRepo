package com.hari.learning.kafka.tests;

import org.apache.kafka.clients.producer.Producer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

import com.hari.learning.kafka.MyKafkaProducer;

public class KafkaProdTests {
	private MyKafkaProducer<String> myKfkProd;
	private Producer<String, String> prod;

	@BeforeTest
	public void init() {
		myKfkProd = MyKafkaProducer.<String>newInstance().get();
		prod = myKfkProd.getProducer().apply("10.65.137.104:9098",
				"org.apache.kafka.common.serialization.StringSerializer");
	}

	@Test
	public void sendHelloWorld() {
		System.out.println(" Test sending Helloworld to kafka ");
		for (int i = 0; i < 10; i++)
			prod.send(myKfkProd.getProducerRec().apply("Tgt", "Hello World"));
	}

	@AfterTest
	public void deinit() {
		prod.close(10, TimeUnit.SECONDS);
	}

}
