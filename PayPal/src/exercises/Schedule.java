package exercises;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private List<MonthlySchedule> months;
	
	public Schedule() {
		this.months = new ArrayList<MonthlySchedule>();
	}
	
	public void addMonth(MonthlySchedule ms) {
		this.months.add(ms);
	}
	
	private int size() {
		return this.months.size();
	}
	
	public MonthlySchedule getMonth(int month) {
		if (month > size()) {
			return null;
		} else {
			return this.months.get(month);
		}
	}

	public void outputSchedule() {
		MonthlySchedule.outputHeader();
		for (MonthlySchedule ms: this.months) {
			ms.outputMonth();
		}
	}
	
}
