package com.sdm.facebook.model.attachment;

import org.json.JSONObject;

public class LocationAttachment extends GeneralAttachment {
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
	public JSONObject serialize() {
		JSONObject attachment = super.serialize();
		attachment.put("type", "location");
		JSONObject cords = new JSONObject();
		cords.put("lat", this.latitude);
		cords.put("long", this.longtitude);
		attachment.put("payload", new JSONObject().put("coordinates", cords));
		return attachment;
	}

	@Override
	public void deserialize(JSONObject value) {
		if (value.has("payload") && value.getJSONObject("payload").has("coordinates")) {
			JSONObject cords = value.getJSONObject("payload").getJSONObject("coordinates");
			if (cords.has("lat")) {
				this.latitude = cords.getDouble("lat");
			}

			if (cords.has("long")) {
				this.latitude = cords.getDouble("long");
			}
		}

		super.deserialize(value);
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
