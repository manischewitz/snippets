package org.javautests.project.race;

public interface RaceResultsService {
	
	public static enum RaceType {
		HORSE,
		F1,
		BOAT
	}

	/**
	 * Returned true not guarantees successful addition to set, but that operation was 
	 * at least performed. False means operation was not performed e. g. due illegal arguments passed.
	 * */
	public boolean addSubscriber(Client client);
	
	public boolean addSubscriber(Client client, RaceType type);
	
	public boolean send(Message message);

	public boolean removeSubscriber(Client clientA);

	public boolean send(Message message, RaceType rt);
	
}
