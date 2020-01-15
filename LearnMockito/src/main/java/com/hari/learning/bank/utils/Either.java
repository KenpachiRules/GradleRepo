package com.hari.learning.bank.utils;

/**
 * Type for making partial function to Total function.
 * Contains three sub-types.
 * 1) Left - represents the success value.
 * 2) Right - represents the failure
 * (exception). 3) Empty - represents an empty value.
 * 
 * @author harim
 *
 */

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Either<T> {

	abstract public <U> Either<U> map(Function<T, U> func);

	abstract public <U> Either<U> flatMap(Function<T, Either<U>> func);

	public abstract Either<T> filter(Predicate<T> pred);

	public abstract T get();

	public abstract T getOrElse(T value);

	public static <T> Either<T> of(T value) {
		try {
			if (value != null)
				return left(value);
			else
				return right(new Throwable(" Passed a null value "));
		} catch (Throwable t) {
			return right(new Throwable(t));
		}
	}

	private static <T> Left<T> left(T value) {
		return new Left<T>(value);
	}

	private static <T> Right<T> right(Throwable t) {
		return new Right<T>(t);
	}

	private static class Left<T> extends Either<T> {

		private final T value;

		private Left(T value) {
			this.value = value;
		}

		@Override
		public <U> Either<U> map(Function<T, U> func) {
			try {
				return left(func.apply(value));
			} catch (Throwable t) {
				return right(t);
			}
		}

		@Override
		public <U> Either<U> flatMap(Function<T, Either<U>> func) {
			try {
				return func.apply(value);
			} catch (Throwable t) {
				return right(t);
			}
		}

		@Override
		public Either<T> filter(Predicate<T> pred) {
			try {
				if (pred.test(value)) {
					return this;
				} else {
					return right(new Throwable(String.format("Predicate failed")));
				}
			} catch (Throwable t) {
				return right(t);
			}
		}

		@Override
		public T get() {
			return value;
		}

		@Override
		public T getOrElse(T value) {
			return value;
		}

	}

	private static class Right<T> extends Either<T> {

		private final Throwable t;

		private Right(Throwable t) {
			this.t = t;
		}

		@Override
		public Either<T> filter(Predicate<T> pred) {
			return this;
		}

		@Override
		public T get() {
			throw new RuntimeException(t);
		}

		@Override
		public T getOrElse(T defValue) {
			return defValue;
		}

		@Override
		public <U> Either<U> map(Function<T, U> func) {
			return right(t);
		}

		@Override
		public <U> Either<U> flatMap(Function<T, Either<U>> func) {
			return right(t);
		}

	}

}
