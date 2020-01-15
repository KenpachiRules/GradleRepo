package com.hari.learning.bank.personal.tests;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hari.learning.bank.personal.Address;
import com.hari.learning.bank.personal.Address.Builder;
import static com.hari.learning.bank.personal.Address.Utils.*;
import static org.testng.Assert.assertEquals;

public class TestAddressEntity {

	private Address correct;
	private Address incorrect;

	@BeforeTest
	public void initAddresses() {
		//
		Builder correctAddBuilder = new Builder();
		correctAddBuilder.city = "Erode";
		correctAddBuilder.country = "India";
		correctAddBuilder.district = "Erode";
		correctAddBuilder.doorNo = "47";
		correctAddBuilder.pincode = 638012L;
		correctAddBuilder.state = "Tamil Nadu";
		correct = correctAddBuilder.build();

		Builder incorrectAddBuilder = new Builder();
		incorrectAddBuilder.city = null;
		incorrectAddBuilder.country = null;
		incorrectAddBuilder.district = "";
		incorrectAddBuilder.doorNo = "";
		incorrectAddBuilder.pincode = 238012;
		incorrectAddBuilder.state = null;

		incorrect = incorrectAddBuilder.build();

	}

	/**
	 * Test both correct and incorrect Address's city.
	 */
	@Test
	public void testCity() {
		assertEquals(getCity(correct).get(), "Erode", "Wrong city");
		try {
			getCity(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(city) failed -> %s", t.getMessage()));
		}
		assertEquals(getCity(incorrect).getOrElse("Bangalore"), "Bangalore");
	}

	/**
	 * Test both correct and incorrect Address's pincode.
	 * 
	 */
	@Test
	public void testPinCode() {
		assertEquals((long) getPin(correct).get(), 638012L, "Incorrect pin");
		try {
			getPin(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(pin) failed -> %s", t.getMessage()));
		}
		assertEquals((long) getPin(incorrect).getOrElse(560093L), (long) 560093L);
	}

	/**
	 * Test both correct and incorrect Address's country.
	 * 
	 */

	@Test
	public void testCountry() {
		assertEquals(getCountry(correct).get(), "India", "Wrong Country");
		try {
			getCountry(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(Country) failed -> %s", t.getMessage()));
		}
		assertEquals(getCountry(incorrect).getOrElse("India"), "India");
	}

	/**
	 * Test both correct and incorrect Address's district.
	 * 
	 */

	@Test
	public void testDistrict() {
		assertEquals(getDistrict(correct).get(), "Erode", "Wrong District");
		try {
			getDistrict(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(District) failed -> %s", t.getMessage()));
		}
		assertEquals(getDistrict(incorrect).getOrElse("Erode"), "Erode");
	}

	/**
	 * Test both correct and incorrect Address's state.
	 * 
	 */

	@Test
	public void testState() {
		assertEquals(getState(correct).get(), "Tamil Nadu", "Wrong State");
		try {
			getState(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(State) failed -> %s", t.getMessage()));
		}
		assertEquals(getState(incorrect).getOrElse("Karnataka"), "Karnataka");
	}

	/**
	 * Test both correct and incorrect Address's door no.
	 * 
	 */

	@Test
	public void testDoorNo() {
		assertEquals(getDoorNo(correct).get(), "47", "Wrong Door No");
		try {
			getDoorNo(incorrect).get();
		} catch (Throwable t) {
			System.out.println(String.format("Incorrect Address(Door No) failed -> %s", t.getMessage()));
		}
		assertEquals(getDoorNo(incorrect).getOrElse("47"), "47");
	}

}
