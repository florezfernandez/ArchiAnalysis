package co.edu.uniandes.archimate.analysis.quantitative.availability;


public class Utils {

	public final static double TIME_1HOUR_TO_MINS = 60;
	public final static double TIME_1DAY_TO_HOURS = 8;
	public final static double TIME_1WEEK_TO_DAYS = 5;
	public final static double TIME_1YEAR_TO_WEAKS = 52;
	public final static double TIME_1YEAR_TO_MONTHS = 12;
	public final static double TIME_1YEAR_TO_QUARTERS = 4;

	public final static String CAT_CONTINOUS = "CONTINUOUS AVAILABILITY";
	public final static String CAT_CRITICAL = "MISSION CRITICAL";
	public final static String CAT_IMPORTANT = "BUSINESS IMPORTANT";
	public final static String CAT_FOUNDATION = "BUSINESS FOUNDATION";
	public final static String CAT_EDGE = "BUSINESS EDGE";
	
	public static Double getMaxMinorOutagexCategorization(WService service)
	{
		if(service.getCategorization().equals(CAT_CONTINOUS)) return (1d/TIME_1HOUR_TO_MINS);
		if(service.getCategorization().equals(CAT_CRITICAL)) return (10d/TIME_1HOUR_TO_MINS);
		if(service.getCategorization().equals(CAT_IMPORTANT)) return 60d;
		if(service.getCategorization().equals(CAT_FOUNDATION)) return service.getHours();
		if(service.getCategorization().equals(CAT_EDGE)) return service.getHours() * service.getDays();
		return service.getHours() * service.getDays();
	}
	
	public static String getRTO(WService service)
	{
		if(service.getCategorization().equals(CAT_CONTINOUS)) return "2 Hours";
		if(service.getCategorization().equals(CAT_CRITICAL)) return "8 Hours";
		if(service.getCategorization().equals(CAT_IMPORTANT)) return "3 Days";
		if(service.getCategorization().equals(CAT_FOUNDATION)) return "1 Week";
		if(service.getCategorization().equals(CAT_EDGE)) return ">1 Month";
		return "Not Defined";
	}
	
	public static String getRPO(WService service)
	{
		if(service.getCategorization().equals(CAT_CONTINOUS)) return "10 Mins";
		if(service.getCategorization().equals(CAT_CRITICAL)) return "2 Hours";
		if(service.getCategorization().equals(CAT_IMPORTANT)) return "1 Day";
		if(service.getCategorization().equals(CAT_FOUNDATION)) return "1 Day";
		if(service.getCategorization().equals(CAT_EDGE)) return "1 Week";
		return "Not Defined";
	}

}
