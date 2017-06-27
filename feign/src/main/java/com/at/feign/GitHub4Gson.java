package com.at.feign;

import java.util.List;

import feign.Param;
import feign.RequestLine;

interface GitHub4Gson {
	@RequestLine("GET /repos/{owner}/{repo}/contributors")
	List<Contributor4Gson> contributors(@Param("owner") String owner, @Param("repo") String repo);
}
