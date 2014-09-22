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

import java.io.IOException;
import java.lang.Math;
import java.lang.IllegalArgumentException;

public class AmortizationSchedule {

	// private fields
	private long amountBorrowed = 0;
	private double apr = 0d;
	private int termMonths = 0;
	private long monthlyPaymentAmount = 0;
	
	// constants
	private final double MONTHLY_INTEREST_DIVISOR = 12d * 100d;
	private static final Range<Double> BORROW_AMOUNT_RANGE = new Range<Double>( 0.01d, 1000000000000d );
	private static final Range<Double> APR_RANGE = new Range<Double>( 0.000001d, 100d );
	private static final Range<Integer> TERM_RANGE = new Range<Integer>( 1, 1000000 );

	// 
	// getters/setters of private fields
	//
	public long getAmountBorrowed() {
		return amountBorrowed;
	}
	
	public void setAmountBorrowed(double amount) throws IllegalArgumentException {
		if (isValidBorrowAmount(amount) == false) {
			throw new IllegalArgumentException();
		}
		amountBorrowed = Math.round(amount * 100);
	}
	
	public double getAPR() {
		return apr;
	}
	
	public void setAPR(double interestRate) throws IllegalArgumentException {
		if (isValidAPRValue(interestRate) == false) {
			throw new IllegalArgumentException();
		}
		apr = interestRate;
	}
	
	public int getTermMonths() {
		return termMonths;
	}
	
	public void setTermMonths(int years) throws IllegalArgumentException {
		if (isValidTerm(years) == false) {
			throw new IllegalArgumentException();
		}
		termMonths = years * 12;
	}
	
	public long getMonthlyPaymentAmount() {
		return monthlyPaymentAmount == 0 ? calculateMonthlyPayment() : monthlyPaymentAmount;
	}
	
	public void setMonthlyPaymentAmount(long amount) throws IllegalArgumentException {
		// the following shouldn't happen with the available valid ranges
		// for borrow amount, apr, and term; however, without range validation,
		// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
		// may yield incorrect values with extreme input values
		if (amount > getAmountBorrowed()) {
			throw new IllegalArgumentException();
		}
		monthlyPaymentAmount = amount;
	}

	//
	// Constructor
	//
	public AmortizationSchedule(double amount, double interestRate, int years) throws IllegalArgumentException {

		setAmountBorrowed(amount);
		setAPR(interestRate);
		setTermMonths(years);
		setMonthlyPaymentAmount(calculateMonthlyPayment());
		
	}

	//
	// Calculate monthly payment amount based on loan amount, APR and number of months
	//
	private long calculateMonthlyPayment() {
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		//
		// Where:
		// P = Principal
		// I = Interest
		// J = Monthly Interest in decimal form:  I / (12 * 100)
		// N = Number of months of loan
		// M = Monthly Payment Amount
		// 
		
		// calculate J
		double monthlyInterest = getAPR() / MONTHLY_INTEREST_DIVISOR;
		
		// this is Math.pow(1/(1 + J), N)
		double tmp = Math.pow(1d/(1d + monthlyInterest), getTermMonths());
		
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		double rc = getAmountBorrowed() * (monthlyInterest / (1d - tmp));
		
		return Math.round(rc);
	}

	//
	// Recalculate the monthly payment amount based on the new loan amount, APR or number of monthsg
	//
	public double recalculate() {
		long newMonthlyPaymentAmount = calculateMonthlyPayment();
		setMonthlyPaymentAmount(newMonthlyPaymentAmount);
		return newMonthlyPaymentAmount;
	}
	
	
	// The output should include:
	//	The first column identifies the payment number.
	//	The second column contains the amount of the payment.
	//	The third column shows the amount paid to interest.
	//	The fourth column has the current balance, the total payment amount and the interest paid fields.
	public Schedule buildAmortizationSchedule() {
		// 
		// To create the amortization table, create a loop in your program and follow these steps:
		// 1.      Calculate H = P x J, this is your current monthly interest
		// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
		// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
		// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
		// 
		
		long balance = getAmountBorrowed();
		int paymentNumber = 0;
		long totalPayments = 0;
		long totalInterestPaid = 0;
		
		Schedule schedule = new Schedule();
		MonthlySchedule ms = new MonthlySchedule(paymentNumber++, 0, 0, balance, 0, 0);
		schedule.addMonth(ms);
		
		final int maxPaymentNumber = getTermMonths();
		final long monthlyPaymentAmount = getMonthlyPaymentAmount();
		final double monthlyInterest = getAPR() / MONTHLY_INTEREST_DIVISOR;
		while (balance > 0) {
			// Calculate H = P x J, this is your current monthly interest
			//double curMonthlyInterest = Math.floor((double)balance * monthlyInterest);
			double curMonthlyInterest = Math.round((double)balance * monthlyInterest);
	
			// Determine current month's payment
			long curMonthlyPaymentAmount;
			if (paymentNumber != maxPaymentNumber) {
				// not the final month
				// the payment is equal to the calculated monthly payment amount (fixed)
				curMonthlyPaymentAmount = monthlyPaymentAmount;
			} else {
				// the final month
				// the amount required to payoff the loan is: [remaining balance] + [monthly interest]
				// this may be less than the calculated monthly payment amount
				curMonthlyPaymentAmount = balance + (long)curMonthlyInterest;
			}
						
			// Calculate C = M - H, this is your monthly payment minus your monthly interest,
			// so it is the amount of principal you pay for that month
			long curMonthlyPrincipalPaid = curMonthlyPaymentAmount - (long)curMonthlyInterest;
			
			// Calculate Q = P - C, this is the new balance of your principal of your loan.
			long curBalance = balance - curMonthlyPrincipalPaid;
			
			totalPayments += curMonthlyPaymentAmount;
			totalInterestPaid += curMonthlyInterest;
			
			// add this month's data to schedule
			ms = new MonthlySchedule(paymentNumber++, 
					curMonthlyPaymentAmount, 
					(long)curMonthlyInterest, 
					curBalance, 
					totalPayments, 
					totalInterestPaid);
			schedule.addMonth(ms);
						
			// Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
			balance = curBalance;
		}
		
		return schedule;
	}
	
	// Static methods used by main()
	
	public static boolean isValidBorrowAmount(double amount) {
		return BORROW_AMOUNT_RANGE.contains(amount);
	}
	
	public static boolean isValidAPRValue(double rate) {
		return APR_RANGE.contains(rate);
	}
	
	public static boolean isValidTerm(int years) {
		return TERM_RANGE.contains(years);
	}
	
	@SuppressWarnings("rawtypes")
	public static void printRangeError(Range range) {
		Util.print("Please enter a positive value between " + range.getMininum() + " and " + range.getMaximum() + ". ");
	}
	
	// 
	public static void main(String [] args) {
		
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
						printRangeError(BORROW_AMOUNT_RANGE);
					}
					break;
				case 1:
					apr = Double.parseDouble(line);
					if (isValidAPRValue(apr) == false) {
						isValidValue = false;
						printRangeError(APR_RANGE);
					}
					break;
				case 2:
					years = Integer.parseInt(line);
					if (isValidTerm(years) == false) {
						isValidValue = false;
						printRangeError(TERM_RANGE);
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
		
		try {
			AmortizationSchedule3 as = new AmortizationSchedule3(amount, apr, years);
			Schedule sched = as.buildAmortizationSchedule();
			sched.outputSchedule();
			// you can update amount, APR or years and recalculate
			//as.setAmountBorrowed(200000);
			//as.setAPR(3.49);
			//as.setTerm
			//as.recalculate();
			//as.outputAmortizationSchedule();
		} catch (IllegalArgumentException e) {
			Util.print("Unable to process the values entered. Terminating program.\n");
		}
	}
}