package com.sdm.facebook.model.attachment;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

public class LocationAttachment extends FacebookSerialize {
	/**
	 * 
	 */
	private static final long serialVersionUID = 884178724612147977L;
	/**
	 * cordinates.lat
	 */
	private double latitude;

	/**
	 * coordinates.long
	 */
	private double longtitude;

	@Override
	public void setJson(JSONObject value) {
		if (value.has("payload") && value.getJSONObject("payload").has("coordinates")) {
			JSONObject cords = value.getJSONObject("payload").getJSONObject("coordinates");
			if (cords.has("lat")) {
				this.latitude = cords.getDouble("lat");
			}

			if (cords.has("long")) {
				this.latitude = cords.getDouble("long");
			}
		}
		
		super.setJson(value);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

}
