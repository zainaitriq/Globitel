package com.example.calculator;

import org.apache.log4j.Logger;

public class Calculator {
	static Logger loggerOBJ = Logger.getLogger(Calculator.class);

	public static void main(String[] args) {
		loggerOBJ.debug("logger debug");
		System.out.println("hello");

		if (args.length != 3) {
			System.out.println("invalid input");
			System.exit(1);
		}

		String operation = args[0];
		double firstNumber = Double.parseDouble(args[1]);
		double secondNumber = Double.parseDouble(args[2]);
		double result;

		if (operation.equals("sum")) {
			result = firstNumber + secondNumber;
		} else if (operation.equals("sub")) {
			result = firstNumber - secondNumber;
		} else {
			System.out.println("Invalid operation: " + operation);
			System.exit(1);
			return;
		}

		System.out.println(result);
	}
}
