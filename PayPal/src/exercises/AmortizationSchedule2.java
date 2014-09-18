//
// Exercise Details:
// Build an amortization schedule program using Java. 
// 
// The program should prompt the user for
//		the amount he or she is borrowing,
//		the annual percentage rate used to repay the loan,
//		the term, in years, over which the loan is repaid.  
// 
// The output should include:
//		The first column identifies the payment number.
//		The second column contains the amount of the payment.
//		The third column shows the amount paid to interest.
//		The fourth column has the current balance.  The total payment amount and the interest paid fields.
// 
// Use appropriate variable names and comments.  You choose how to display the output (i.e. Web, console).  
// Amortization Formula
// This will get you your monthly payment.  Will need to update to Java.
// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
// 
// Where:
// P = Principal
// I = Interest
// J = Monthly Interest in decimal form:  I / (12 * 100)
// N = Number of months of loan
// M = Monthly Payment Amount
// 
// To create the amortization table, create a loop in your program and follow these steps:
// 1.      Calculate H = P x J, this is your current monthly interest
// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
// 

package exercises;

import java.lang.Math;
import java.lang.IllegalArgumentException;

public class AmortizationSchedule2 {


	private long amountBorrowed = 0;
	private double apr = 0d;
	private int initialTermMonths = 0;
	
	private final double monthlyInterestDivisor = 12d * 100d;
	private double monthlyInterest = 0d;
	private double monthlyPaymentAmount = 0d;

	private static final Range<Double> borrowAmountRange = new Range<Double>( 0.01d, 1000000000000d );
	private static final Range<Double> aprRange = new Range<Double>( 0.000001d, 100d );
	private static final Range<Integer> termRange = new Range<Integer>( 1, 1000000 );
	
	private double calculateMonthlyPayment() {
		// Instead of: M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		// Use this formula: M = P * J * (1 + J)^N / ((1 + J)^N - 1)
		//
		// Where:
		// P = Principal
		// I = Interest
		// J = Monthly Interest in decimal form:  I / (12 * 100)
		// N = Number of months of loan
		// M = Monthly Payment Amount
		// 
		
		// calculate J
		monthlyInterest = apr / monthlyInterestDivisor;
		
		// this is (1 + J)^N
		double tmp = Math.pow(1d + monthlyInterest, initialTermMonths);
		
		// M = P * J * (1 + J)^N / ((1 + J)^N - 1);
		double rc = amountBorrowed * monthlyInterest * tmp / (tmp - 1d);
		
		monthlyPaymentAmount = rc;
		
		return rc;
	}
	
	// The output should include:
	//	The first column identifies the payment number.
	//	The second column contains the amount of the payment.
	//	The third column shows the amount paid to interest.
	//	The fourth column has the current balance.  The total payment amount and the interest paid fields.
	public void outputAmortizationSchedule() {
		// 
		// To create the amortization table, create a loop in your program and follow these steps:
		// 1.      Calculate H = P x J, this is your current monthly interest
		// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
		// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
		// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
		// 

		String formatString = "%1$-20s%2$-20s%3$-20s%4$s,%5$s,%6$s\n";
		Util.printf(formatString,
				"PaymentNumber",
				"PaymentAmount",
				"PaymentInterest",
				"CurrentBalance",
				"TotalPayments",
				"TotalInterestPaid");
		
		double balance = amountBorrowed;
		int paymentNumber = 0;
		double totalPayments = 0;
		double totalInterestPaid = 0;
		
		// output is in dollars
		formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f,%5$.2f,%6$.2f\n";
		Util.printf(formatString,
				paymentNumber++,
				0d,
				0d,
				((double) amountBorrowed) / 100d,
				((double) totalPayments) / 100d,
				((double) totalInterestPaid) / 100d);
		
		final int maxNumberOfPayments = initialTermMonths;
		while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
			// Calculate H = P x J, this is your current monthly interest
			double curMonthlyInterest = balance * monthlyInterest;

			// the amount required to payoff the loan
			double curPayoffAmount = balance + curMonthlyInterest;
			
			// the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
			double curMonthlyPaymentAmount = Math.min(monthlyPaymentAmount, curPayoffAmount);
			
			// it's possible that the calculated monthlyPaymentAmount is 0,
			// or the monthly payment only covers the interest payment - i.e. no principal
			// so the last payment needs to payoff the loan
			if ((paymentNumber == maxNumberOfPayments) &&
					((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
				curMonthlyPaymentAmount = curPayoffAmount;
			}
			
			// Calculate C = M - H, this is your monthly payment minus your monthly interest,
			// so it is the amount of principal you pay for that month
			double curMonthlyPrincipalPaid = curMonthlyPaymentAmount - curMonthlyInterest;
			
			// Calculate Q = P - C, this is the new balance of your principal of your loan.
			double curBalance = balance - curMonthlyPrincipalPaid;
			
			totalPayments += curMonthlyPaymentAmount;
			totalInterestPaid += curMonthlyInterest;
			
			// output is in dollars
			Util.printf(formatString,
					paymentNumber++,
					((double) curMonthlyPaymentAmount) / 100d,
					((double) curMonthlyInterest) / 100d,
					((double) curBalance) / 100d,
					((double) totalPayments) / 100d,
					((double) totalInterestPaid) / 100d);
						
			// Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
			balance = curBalance;
		}
	}
	
	public void setAmount(double amount) throws IllegalArgumentException {
		if (isValidBorrowAmount(amount) == false) {
			throw new IllegalArgumentException();
		}
		amountBorrowed = Math.round(amount * 100);
	}
	
	public void setAPR(double interestRate) throws IllegalArgumentException {
		if (isValidAPRValue(interestRate) == false) {
			throw new IllegalArgumentException();
		}
		apr = interestRate;
	}
	
	public void setTerm(int years) throws IllegalArgumentException {
		if (isValidTerm(years) == false) {
			throw new IllegalArgumentException();
		}
		initialTermMonths = years * 12;
	}
	
	public double calculate() {
		return calculateMonthlyPayment();
	}
	
	public double getMonthlyPaymentAmount() {
		return monthlyPaymentAmount;
	}
	
	public AmortizationSchedule2(double amount, double interestRate, int years) throws IllegalArgumentException {

		/*
		if ((isValidBorrowAmount(amount) == false) ||
				(isValidAPRValue(interestRate) == false) ||
				(isValidTerm(years) == false)) {
			throw new IllegalArgumentException();
		}

		amountBorrowed = Math.round(amount * 100);
		apr = interestRate;
		initialTermMonths = years * 12;
		*/
		setAmount(amount);
		setAPR(interestRate);
		setTerm(years);
		calculateMonthlyPayment();
		
		// the following shouldn't happen with the available valid ranges
		// for borrow amount, apr, and term; however, without range validation,
		// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
		// may yield incorrect values with extreme input values
		if (getMonthlyPaymentAmount() > amountBorrowed) {
			throw new IllegalArgumentException();
		}
	}
	
	public static boolean isValidBorrowAmount(double amount) {
		return borrowAmountRange.contains(amount);
	}
	
	public static boolean isValidAPRValue(double rate) {
		return aprRange.contains(rate);
	}
	
	public static boolean isValidTerm(int years) {
		return termRange.contains(years);
	}
	
	@SuppressWarnings("rawtypes")
	public static void printRangeError(Range range) {
		Util.print("Please enter a positive value between " + range.getMininum() + " and " + range.getMaximum() + ". ");
	}
	
	// 
	public static void main(String [] args) {
		/*
		String[] userPrompts = {
				"Please enter the amount you would like to borrow: ",
				"Please enter the annual percentage rate used to repay the loan: ",
				"Please enter the term, in years, over which the loan is repaid: "
		};
		
		String line = "";
		double amount = 0;
		double apr = 0;
		int years = 0;
		
		for (int i = 0; i< userPrompts.length; ) {
			String userPrompt = userPrompts[i];
			try {
				line = Util.readLine(userPrompt);
			} catch (IOException e) {
				Util.print("An IOException was encountered. Terminating program.\n");
				return;
			}
			
			boolean isValidValue = true;
			try {
				switch (i) {
				case 0:
					amount = Double.parseDouble(line);
					if (isValidBorrowAmount(amount) == false) {
						isValidValue = false;
						printRangeError(borrowAmountRange);
					}
					break;
				case 1:
					apr = Double.parseDouble(line);
					if (isValidAPRValue(apr) == false) {
						isValidValue = false;
						printRangeError(aprRange);
					}
					break;
				case 2:
					years = Integer.parseInt(line);
					if (isValidTerm(years) == false) {
						isValidValue = false;
						printRangeError(termRange);
					}
					break;
				}
			} catch (NumberFormatException e) {
				isValidValue = false;
			}
			if (isValidValue) {
				i++;
			} else {
				Util.print("An invalid value was entered.\n");
			}
		}
		*/
		try {
			AmortizationSchedule2 as = new AmortizationSchedule2(10000, 2.99, 5);
			as.outputAmortizationSchedule();
			as.setAPR(3.49);
			as.calculate();
			as.outputAmortizationSchedule();
		} catch (IllegalArgumentException e) {
			Util.print("Unable to process the values entered. Terminating program.\n");
		}
	}
}