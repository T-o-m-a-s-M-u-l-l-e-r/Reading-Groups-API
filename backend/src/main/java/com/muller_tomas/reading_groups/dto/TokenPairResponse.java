package com.muller_tomas.reading_groups.dto;

import java.util.Objects;

public class TokenPairResponse {
	private String accessToken;
	private String refreshToken;
	
	public TokenPairResponse() {
		super();
	}

	public TokenPairResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TokenPairResponse tokenPairObject = (TokenPairResponse) o;
		return Objects.equals(accessToken, tokenPairObject.accessToken)
				&& Objects.equals(refreshToken, tokenPairObject.refreshToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken, refreshToken);
	}

}
