package com.at.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class GitHubErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.status() >= 400 && response.status() <= 499) {
			return new GitHubClientException(response.status(), response.reason());
		}
		if (response.status() >= 500 && response.status() <= 599) {
			return new GitHubServerException(response.status(), response.reason());
		}
		return FeignException.errorStatus(methodKey, response);
	}

}
