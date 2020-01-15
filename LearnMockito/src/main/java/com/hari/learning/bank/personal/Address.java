package com.hari.learning.bank.personal;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.hari.learning.bank.utils.Either;

/**
 * Address of a location. Contains Door No, Street Name, City , District , State
 * , Pincode and Country information.
 * 
 * @author harim
 *
 */

public class Address {

	private final String doorNo; // to allow alphanumericals as well.

	private final String street;

	private final String city;

	private final String district;

	private final String state;

	private final long pincode;

	private final String country;

	private Address(String doorNo, String street, String city, String district, String state, long pincode,
			String country) {
		this.doorNo = doorNo;
		this.street = street;
		this.city = city;
		this.district = district;
		this.state = state;
		this.pincode = pincode;
		this.country = country;
	}
	
	@Override
	public String toString() {
		return city;
	}

	public static final class Builder {
		public String doorNo;
		public String street;
		public String city;
		public String district;
		public String state;
		public long pincode;
		public String country;

		public Builder with(Consumer<Address.Builder> cons) {
			cons.accept(this);
			return this;
		}

		public Address build() {
			return new Address(doorNo, street, city, district, state, pincode, country);
		}
	}

	public static final class Utils {
		private final static Predicate<String> IS_NOT_NULL = (input) -> input != null;
		private final static Predicate<String> IS_EMPTY = (input) -> !input.isEmpty();

		private final static Predicate<Long> IS_VALID_PIN_CODE = (pin) -> pin != 0 && (pin > 500000 && pin < 1000000);

		private final static Function<Address, String> GET_STREET = address -> address.street;
		private final static Function<Address, String> GET_DOOR_NO = address -> address.doorNo;
		private final static Function<Address, String> GET_CITY = address -> address.city;
		private final static Function<Address, String> GET_DISTRICT = address -> address.district;
		private final static Function<Address, String> GET_STATE = address -> address.state;
		private final static Function<Address, Long> GET_PIN = address -> address.pincode;
		private final static Function<Address, String> GET_COUNTRY = address -> address.country;

		public static Either<String> getStreetName(final Address address) {
			return Either.of(address).map(GET_STREET).filter(IS_NOT_NULL.and(IS_EMPTY));
		}

		public static Either<String> getCity(final Address address) {
			return Either.of(address).map(GET_CITY).filter(IS_NOT_NULL.and(IS_EMPTY));
		}

		public static Either<String> getDoorNo(final Address address) {
			return Either.of(address).map(GET_DOOR_NO).filter(IS_NOT_NULL.and(IS_EMPTY));
		}

		public static Either<String> getDistrict(final Address address) {
			return Either.of(address).map(GET_DISTRICT).filter(IS_NOT_NULL.and(IS_EMPTY));
		}

		public static Either<String> getState(final Address address) {
			return Either.of(address).map(GET_STATE).filter(IS_NOT_NULL.and(IS_EMPTY));
		}

		public static Either<Long> getPin(final Address address) {
			return Either.of(address).map(GET_PIN).filter(IS_VALID_PIN_CODE);
		}

		public static Either<String> getCountry(final Address address) {
			return Either.of(address).map(GET_COUNTRY).filter(IS_NOT_NULL.and(IS_EMPTY));
		}
	}

}
