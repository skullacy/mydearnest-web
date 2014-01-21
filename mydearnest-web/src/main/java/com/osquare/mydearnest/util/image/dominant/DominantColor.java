package com.osquare.mydearnest.util.image.dominant;

public class DominantColor {
	
	private int color;
	private float percentage;
	
	public DominantColor(int color, float percentage) {
		this.color = color;
		this.percentage = percentage;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	
	public String getRGBHex() {
		String rgbHex = "#"+Integer.toHexString(this.color).substring(2);
		return rgbHex;
	}
	
	public int getPercent() {
		return (int)(percentage * 100);
	}
	
	@Override
	public String toString() {
		return "Color: " + getColor() + "   Percentage: " + getPercentage();
	}

}
