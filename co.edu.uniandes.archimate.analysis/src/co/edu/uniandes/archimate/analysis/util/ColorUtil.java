package co.edu.uniandes.archimate.analysis.util;

public class ColorUtil {

	public enum ColorType{FontColor, LineColor, FillColor}

	public final static String White= "#FFFFFF";
	public final static String Red= "#FF0000";
	public final static String Green= "#00FF00";
	public final static String Blue= "#0000FF";
	public final static String Magenta= "#FF00FF";
	public final static String Cyan= "#00FFFF";
	public final static String Yellow= "#FFFF00";
	public final static String Black= "#000000";
	public final static String Aqua= "#70DB93";
	public final static String Bakers_Chocolate= "#5C3317";
	public final static String Blue_Violet= "#9F5F9F";
	public final static String Brass= "#B5A642";
	public final static String Bright_Gold= "#D9D919";
	public final static String Brown= "#A62A2A";
	public final static String Bronze= "#8C7853";
	public final static String Bronze_II= "#A67D3D";
	public final static String Cadet_Blue= "#5F9F9F";
	public final static String Cool_Copper= "#D98719";
	public final static String Copper= "#B87333";
	public final static String Coral= "#FF7F00";
	public final static String Corn_Flower_Blue= "#42426F";
	public final static String Dark_Brown= "#5C4033";
	public final static String Dark_Green= "#2F4F2F";
	public final static String Dark_Green_Copper= "#4A766E";
	public final static String Dark_Olive_Green= "#4F4F2F";
	public final static String Dark_Orchid= "#9932CD";
	public final static String Dark_Purple= "#871F78";
	public final static String Dark_Slate_Blue= "#6B238E";
	public final static String Dark_Slate_Grey= "#2F4F4F";
	public final static String Dark_Tan= "#97694F";
	public final static String Dark_Turquoise= "#7093DB";
	public final static String Dark_Wood= "#855E42";
	public final static String Dim_Grey= "#545454";
	public final static String Dusty_Rose= "#856363";
	public final static String Feldspar= "#D19275";
	public final static String Firebrick= "#8E2323";
	public final static String Forest_Green= "#238E23";
	public final static String Gold= "#CD7F32";
	public final static String Goldenrod= "#DBDB70";
	public final static String Gray= "#C0C0C0";
	public final static String Green_Copper= "#527F76";
	public final static String Green_Yellow= "#93DB70";
	public final static String Hunter_Green= "#215E21";
	public final static String Indian_Red= "#4E2F2F";
	public final static String Khaki= "#9F9F5F";
	public final static String Light_Blue= "#C0D9D9";
	public final static String Light_Grey= "#A8A8A8";
	public final static String Light_Steel_Blue= "#8F8FBD";
	public final static String Light_Wood= "#E9C2A6";
	public final static String Lime= "#32CD32";
	public final static String Mandarian_Orange= "#E47833";
	public final static String Maroon= "#8E236B";
	public final static String Medium_Aquamarine= "#32CD99";
	public final static String Medium_Blue= "#3232CD";
	public final static String Medium_Forest_Green= "#6B8E23";
	public final static String Medium_Goldenrod= "#EAEAAE";
	public final static String Medium_Orchid= "#9370DB";
	public final static String Medium_Sea_Green= "#426F42";
	public final static String Medium_Slate_Blue= "#7F00FF";
	public final static String Medium_Spring_Green= "#7FFF00";
	public final static String Medium_Turquoise= "#70DBDB";
	public final static String Medium_Violet_Red= "#DB7093";
	public final static String Medium_Wood= "#A68064";
	public final static String Midnight_Blue= "#2F2F4F";
	public final static String Navy= "#23238E";
	public final static String Neon_Blue= "#4D4DFF";
	public final static String Neon_Pink= "#FF6EC7";
	public final static String New_Midnight_Blue= "#00009C";
	public final static String New_Tan= "#EBC79E";
	public final static String Old_Gold= "#CFB53B";
	public final static String Orange= "#FF7F00";
	public final static String Orange_Red= "#FF2400";
	public final static String Orchid= "#DB70DB";
	public final static String Pale_Green= "#8FBC8F";
	public final static String Pink= "#BC8F8F";
	public final static String Plum= "#EAADEA";
	public final static String Quartz= "#D9D9F3";
	public final static String Rich_Blue= "#5959AB";
	public final static String Salmon= "#6F4242";
	public final static String Scarlet= "#8C1717";
	public final static String Sea_Green= "#238E68";
	public final static String SemiSweet_Chocolate= "#6B4226";
	public final static String Sienna= "#8E6B23";
	public final static String Silver= "#E6E8FA";
	public final static String Sky_Blue= "#3299CC";
	public final static String Slate_Blue= "#007FFF";
	public final static String Spicy_Pink= "#FF1CAE";
	public final static String Spring_Green= "#00FF7F";
	public final static String Steel_Blue= "#236B8E";
	public final static String Summer_Sky= "#38B0DE";
	public final static String Tan= "#DB9370";
	public final static String Thistle= "#D8BFD8";
	public final static String Turquoise= "#ADEAEA";
	public final static String Very_Dark_Brown= "#5C4033";
	public final static String Very_Light_Grey= "#CDCDCD";
	public final static String Violet= "#4F2F4F";
	public final static String Violet_Red= "#CC3299";
	public final static String Wheat= "#D8D8BF";
	public final static String Yellow_Green= "#99CC32";	

	public static String rgbToHexaString(int r, int g, int b){
		return String.format("#%02x%02x%02x", r, g, b);
	}


}
