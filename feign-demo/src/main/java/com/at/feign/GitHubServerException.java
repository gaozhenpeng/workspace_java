package com.at.feign;

public class GitHubServerException extends GitHubException {
	private static final long serialVersionUID = -3692993409405311554L;

	public GitHubServerException(int status, String reason) {
		super("server", status, reason);
	}
}
