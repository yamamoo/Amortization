package exercises;

public class MonthlySchedule {
	private long paymentNumber;
	private long paymentAmount;
	private long paymentInterest;
	private long currentBalance;
	private long totalPayments;
	private long totalInterestsPaid;
	
	public MonthlySchedule(long num, long amount, long interest, long balance, long totalPayments, long totalInterest) {
		this.paymentNumber = num;
		this.paymentAmount = amount;
		this.paymentInterest = interest;
		this.currentBalance = balance;
		this.totalPayments = totalPayments;
		this.totalInterestsPaid = totalInterest;
	}
	
	public long getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(long paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public long getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public long getPaymentInterest() {
		return paymentInterest;
	}
	public void setPaymentInterest(long paymentInterest) {
		this.paymentInterest = paymentInterest;
	}
	public long getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(long currentBalance) {
		this.currentBalance = currentBalance;
	}
	public long getTotalPayments() {
		return totalPayments;
	}
	public void setTotalPayments(long totalPayments) {
		this.totalPayments = totalPayments;
	}
	public long getTotalInterestsPaid() {
		return totalInterestsPaid;
	}
	public void setTotalInterestsPaid(long totalInterestsPaid) {
		this.totalInterestsPaid = totalInterestsPaid;
	}

	public static void outputHeader() {
		// Print table header
		String formatString = "%1$-20s%2$-20s%3$-20s%4$s,%5$s,%6$s\n";
		Util.printf(formatString,
				"#PaymentNumber",
				"#PaymentAmount",
				"#PaymentInterest",
				"#CurrentBalance",
				"#TotalPayments",
				"#TotalInterestPaid");
	}
	
	public void outputMonth() {
		// output is in dollars
		String formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f,%5$.2f,%6$.2f\n";
		Util.printf(formatString,
				getPaymentNumber(),
				((double)getPaymentAmount()) / 100d,
				((double)getPaymentInterest()) / 100d,
				((double)getCurrentBalance()) / 100d,
				((double)getTotalPayments()) / 100d,
				((double)getTotalInterestsPaid()) / 100d);
	}
	

}
