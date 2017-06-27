package com.at.spring.annotation.property_source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueInComponent {

	@Value("#{'${openstack.compute.service.targeturl}'.trim()}")
	public String compute_service_targeturl = null;

}
