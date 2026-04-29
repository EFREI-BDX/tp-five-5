package com.group3.efreifive.recordmatch.service;

public enum DomainErrorCode {
	UNDEFINED("UNDEF", "Undefined error"),
	EVENT_NOT_FOUND("ENFND", "No event with this id exists"),
	MATCH_NOT_FOUND("MNFND", "No match with this id exists"),
	MATCH_EVENT_NOT_FOUND("MENFD", "No match event with this id exists"),
	PLAYER_NOT_FOUND("PNFND", "No player with this id exists");

	private final String code;
	private final String message;

	DomainErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static DomainErrorCode fromCode(String code) {
		for (DomainErrorCode value : DomainErrorCode.values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return null;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
