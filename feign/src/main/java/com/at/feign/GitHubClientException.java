package com.at.feign;

public class GitHubClientException extends GitHubException {
	private static final long serialVersionUID = 552407448516172377L;

	public GitHubClientException(int status, String reason) {
		super("client", status, reason);
	}
}
