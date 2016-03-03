package edu.uclm.esi.common.jsonMessages;

import org.json.JSONException;
import org.json.JSONObject;

public class GetMessagesMessage extends JSONMessage {
	@JSONable
	private String email;

	public GetMessagesMessage(String email) {
		super(true);
		this.email=email;
	}

	public GetMessagesMessage(JSONObject jso) throws JSONException {
		this(jso.get("email").toString());
	}
	
	public String getEmail() {
		return email;
	}
}
