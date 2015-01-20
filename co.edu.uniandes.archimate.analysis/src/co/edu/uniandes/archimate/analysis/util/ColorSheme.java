package co.edu.uniandes.archimate.analysis.util;

import co.edu.uniandes.archimate.analysis.util.ColorUtil.ColorType;

public class ColorSheme {
		private ColorType type; 
		private String color;

		protected ColorType getType() {
			return type;
		}

		protected void setType(ColorType type) {
			this.type = type;
		}

		protected String getColor() {
			return color;
		}

		protected void setColor(String color) {
			this.color = color;
		}

		public ColorSheme(ColorType type, String color) {
			this.type = type;
			this.color = color;
		} 
}
