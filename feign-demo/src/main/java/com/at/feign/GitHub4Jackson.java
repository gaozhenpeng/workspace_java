package com.at.feign;

import java.util.List;

import feign.Param;
import feign.RequestLine;

interface GitHub4Jackson {
	@RequestLine("GET /repos/{owner}/{repo}/contributors")
	List<Contributor4Jackson> contributors(@Param("owner") String owner, @Param("repo") String repo);
}
