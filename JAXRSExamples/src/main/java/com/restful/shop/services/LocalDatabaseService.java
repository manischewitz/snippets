package com.restful.shop.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/localDatabase")
public class LocalDatabaseService {

	public enum Databases {
		
		EUROPE,
		NORTH_AMERICA
	};
	
	private final EuropeDbService europeDbService = new EuropeDbService();
	private final NorthAmericaDbService northAmericaDbService = new NorthAmericaDbService();
	
	@Path("{database}-db")
	public Object getDatabase(@PathParam("database") LocalDatabaseService.Databases db) {
		System.out.println(db);
		switch (db) {
			case EUROPE: return europeDbService;
			case NORTH_AMERICA: return northAmericaDbService;
			default: return null;
		}
	}
	
}
