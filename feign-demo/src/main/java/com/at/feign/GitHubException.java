package com.at.feign;

public class GitHubException extends RuntimeException {
	private static final long serialVersionUID = -2253183367390112402L;

	public GitHubException(String side, int status, String reason) {
		super("side: '" + side + "'; status: '" + status + "'; reason: '" + reason + "'");
	}
}
