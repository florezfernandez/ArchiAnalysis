package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning;

public class Utils {

	public final static double TIME_1DAY_TO_HOURS = 8;
	public final static double TIME_1WEEK_TO_DAYS = 5;
	public final static double TIME_1YEAR_TO_WEAKS = 52;
	public final static double TIME_1YEAR_TO_MONTHS = 12;
	public final static double TIME_1YEAR_TO_QUARTERS = 4;

	public final static double NETWORK_1KBPs_TO_bps = 1000;
	public final static double STORAGE_1KB_TO_BITS = 8192;
	public final static double STORAGE_1KB_TO_Bytes = 1024;

	public final static String FREQ_DAILY = "DAILY";
	public final static String FREQ_WEEKLY = "WEEKLY";
	public final static String FREQ_MONTHLY = "MONTHLY";
	public final static String FREQ_QUARTERLY = "QUARTERLY";

	public static Double toYears(Double valueFrom, String frequency)
	{
		Double result = 0d;
		if(frequency.equals(FREQ_DAILY))
			result = valueFrom*TIME_1WEEK_TO_DAYS*TIME_1YEAR_TO_WEAKS;
		if(frequency.equals(FREQ_WEEKLY))
			result = valueFrom*TIME_1YEAR_TO_WEAKS;
		if(frequency.equals(FREQ_MONTHLY))
			result = valueFrom*TIME_1YEAR_TO_MONTHS;
		if(frequency.equals(FREQ_QUARTERLY))
			result = valueFrom*TIME_1YEAR_TO_QUARTERS;
		return result;
	}

}
