package org.javautests.project.race;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class RaceResultsServiceImpl implements RaceResultsService {

	private Map<RaceType, Set<Client>> groups;
	private Logging log;
	
	public RaceResultsServiceImpl () {
		this(new Logging() {
			@Override
			public void log(Object message) {
				System.out.println(message.toString());
			}
		});
	}
	
	public RaceResultsServiceImpl(Logging logger) {
		groups = new HashMap<>();
		for(RaceType type : RaceType.values()) {
			groups.put(type, new HashSet<Client>());
		}
		log = logger;
	}

	@Override
	public boolean addSubscriber(Client client) {
		if (client == null) {
			return false;
		}
		groups.forEach((k, v) -> v.add(client));
		return true;

	}

	@Override
	public boolean send(Message message) {
		if (message == null) {
			return false;
		}
		log.log(message);
		groups.forEach((k, v) ->  v.forEach(c -> c.receive(message)));
		return true;
	}

	@Override
	public boolean removeSubscriber(Client client) {
		if (client == null) {
			return false;
		}
		groups.forEach((k, v) -> v.remove(client));
		return true;
	}

	@Override
	public boolean addSubscriber(Client client, RaceType type) {
		if (client == null || type == null) {
			return false;
		}
		groups.get(type).add(client);
		return true;
	}

	@Override
	public boolean send(Message message, RaceType rt) {
		if (message == null || rt == null) {
			return false;
		}
		log.log(rt);
		log.log(message);
		groups.get(rt).forEach(c -> c.receive(message));
		return true;
	}

}
