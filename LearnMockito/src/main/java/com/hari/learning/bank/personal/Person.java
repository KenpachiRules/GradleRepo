package com.hari.learning.bank.personal;

/***
 * Represents the account holder information.
 * 
 * @author harim
 *
 */

public class Person {

	private final String name;

	private final int age;

	private final Address address;

	private Person(String name, int age, Address address) {
		this.name = name;
		this.age = age;
		this.address = address;
	}

}
