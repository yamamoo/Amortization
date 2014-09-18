package exercises;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Range<T extends Comparable<T>> {
	private Object[] range;
	
	public Range(T minVal, T maxVal) {
		this.range = new Object[2];
		this.range[0] = minVal;
		this.range[1] = maxVal;
	}
	
	public void setRange(T minVal, T maxVal) {
		this.range[0] = minVal;
		this.range[1] = maxVal;
	}
	
	@SuppressWarnings("unchecked")
	public T[] getRange() {
		return (T[]) this.range;
	}
	
	@SuppressWarnings("unchecked")
	public T getMininum() {
		return (T) this.range[0];
	}
	
	@SuppressWarnings("unchecked")
	public T getMaximum() {
		return (T) this.range[1];
	}
	
	public boolean contains(T value) {
		return (this.getMininum().compareTo(value) <= 0)
			&& (this.getMaximum().compareTo(value) >= 0);
	}
	
	public static void main(String[] args) {
		Range<Double> borrowAmountRange = new Range<Double>(0.01d, 1000000000000d);
		Range<Double> aprRange = new Range<Double>(0.000001d, 100d);
		Range<Integer> termRange = new Range<Integer>(1, 1000000);
		
		assert(borrowAmountRange.contains(1.0d) == true);				// lower boundary
		assert(borrowAmountRange.contains(1000000000000d) == true);		// upper boundary
		assert(borrowAmountRange.contains(1.0d) == true);				// in range
		assert(borrowAmountRange.contains(-1.0d) == false); 			// less than minimum
		assert(borrowAmountRange.contains(1000000000001d) == false);	// more than maximum
		
		assert(aprRange.contains(0.000001d) == true);					// lower boundary
		assert(aprRange.contains(100d) == true);						// upper boundary
		assert(aprRange.contains(2.99d) == true);						// in range
		assert(aprRange.contains(0.0000001d) == false);					// less than minimum
		assert(aprRange.contains(101d) == false);						// more than maximum
		
		assert(termRange.contains(1) == true);							// lower boundary
		assert(termRange.contains(1000000) == true);					// upper boundary
		assert(termRange.contains(30) == true);							// in range
		assert(termRange.contains(0) == false);							// minimum - 1
		assert(termRange.contains(-1) == false);						// minimum - 2
		assert(termRange.contains(1000001) == false);					// maximum + 1
		
		SimpleDateFormat df = new SimpleDateFormat("mm-dd-yyyy");
		try {
			Range<Date> dateRange = new Range<Date>(df.parse("01-01-2013"), df.parse("12-31-2013"));
			assert(dateRange.contains(df.parse("01-01-2013")) == true);	// lower boundary
			assert(dateRange.contains(df.parse("12-31-2013")) == true);	// upper boundary
			assert(dateRange.contains(df.parse("06-06-2013")) == true);	// in range
			assert(dateRange.contains(df.parse("12-31-2012")) == false);// a day before start date
			assert(dateRange.contains(df.parse("01-01-2014")) == false);// a day after end date			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Passed");

	}
}
