package co.edu.uniandes.archimate.analysis.quantitative.availability;

import com.archimatetool.model.impl.ArchimateElement;

public class WService {

	public final static String CAT_CONTINOUS = "CONTINUOUS AVAILABILITY";
	public final static String CAT_CRITICAL = "MISSION CRITICAL";
	public final static String CAT_IMPORTANT = "BUSINESS IMPORTANT";
	public final static String CAT_FOUNDATION = "BUSINESS FOUNDATION";
	public final static String CAT_EDGE = "BUSINESS EDGE";
	
	private ArchimateElement element;
	private String categorization;
	private String hoursXdays;
	private Double hours;
	private Double days;
	private Double maxRecTimeMins;

	public ArchimateElement getElement() {
		return element;
	}

	public String getCategorization() {
		return categorization;
	}
	public void setCategorization(String categorization) {
		this.categorization = categorization!=null? categorization.toUpperCase():categorization;
	}
	public boolean isValidCategorization()
	{
		return ((this.categorization!=null) && (this.categorization.equals(CAT_CONTINOUS) || this.categorization.equals(CAT_CRITICAL) || this.categorization.equals(CAT_IMPORTANT) || this.categorization.equals(CAT_FOUNDATION) || this.categorization.equals(CAT_EDGE)));
	}
	public String getCategorizationValidValues ()
	{
		return CAT_CONTINOUS+", "+ CAT_CRITICAL+", "+ CAT_IMPORTANT+", "+ CAT_FOUNDATION+", "+ CAT_EDGE;
	}

	public Double getMaxMinorOutagexCategorization()
	{
		if(this.categorization == CAT_CONTINOUS) return (1d/60d);
		if(this.categorization == CAT_CONTINOUS) return (10d/60d);
		if(this.categorization == CAT_CONTINOUS) return 60d;
		if(this.categorization == CAT_CONTINOUS) return hours;
		if(this.categorization == CAT_CONTINOUS) return hours * days;
		return 0d;
	}
	
	public void setHoursXdays(String hoursXdays)
	{
		this.hoursXdays = hoursXdays;
		this.hours = null;
		this.days = null;

		if(this.hoursXdays!=null)
		{
			this.hoursXdays = this.hoursXdays.toLowerCase();
			String[] splitValues = this.hoursXdays.split("x");
			if(splitValues.length!=2) {this.hoursXdays =null; return;}
			try {
				this.hours = Double.parseDouble(splitValues[0]);
				this.days = Double.parseDouble(splitValues[1]);
			} catch (Exception e) {
				this.hoursXdays =null;
				System.out.print(e);
				return;
			}
		}
	}
	
	public String getHoursXdays()
	{
		return hoursXdays;
	}
	
	public Double getHours() {
		return hours;
	}

	public Double getDays() {
		return days;
	}

	public Double getMaxRecTimeMins() {
		return maxRecTimeMins;
	}

	public void setMaxRecTimeMins(Double maxRecTimeMins) {
		this.maxRecTimeMins = maxRecTimeMins;
	}

	public WService(ArchimateElement element) {
		this.element = element;
	}

}
