package com.restful.shop.features;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.restful.shop.annotations.MaxAge;
import com.restful.shop.filters.CacheControlFilter;

@Provider
public class MaxAgeFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		MaxAge maxAge = resourceInfo.getResourceMethod().getAnnotation(MaxAge.class);
		if (maxAge != null) {
			CacheControlFilter filter = new CacheControlFilter(maxAge.value());
			context.register(filter);
		}
		
	}

	
	
}
