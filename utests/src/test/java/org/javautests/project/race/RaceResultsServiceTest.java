package org.javautests.project.race;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.javautests.project.race.RaceResultsService.RaceType;

public class RaceResultsServiceTest {
	 
	private RaceResultsService raceResults;
	private Client clientA = mock(Client.class, "clientA");
	private Client clientB = mock(Client.class, "clientB");
	private Message message;
	private Logging logger;
	
	@Before
	public void setUp() {
		logger = mock(Logging.class);
		 raceResults = new RaceResultsServiceImpl(logger);
		 clientA = mock(Client.class, "clientA");
		 clientB = mock(Client.class, "clientB");
		 message = mock(Message.class);
		 
	}
	
	@Test
	 public void subscribedClientShouldReceiveMessage() {
		 raceResults.addSubscriber(clientA);
		 raceResults.send(message);
		 verify(clientA, times(RaceType.values().length)).receive(message);
	 }
	
	 @Test
	 public void messageShouldBeSentToAllSubscribedClients() {
		 raceResults.addSubscriber(clientA);
		 raceResults.addSubscriber(clientB);
		 raceResults.send(message);
		 verify(clientA, times(RaceType.values().length)).receive(message);
		 verify(clientB, times(RaceType.values().length)).receive(message);
	 }
	 
	 @Test
	 public void notSubscribedClientShouldNotReceiveMessage() {
		 raceResults.send(message);
		 verify(clientA, never()).receive(message);
		 verify(clientB, never()).receive(message);
		}
	 
	 @Test
	 public void shouldSendOnlyOneMessageToMultiSubscriber() {
	  raceResults.addSubscriber(clientA);
	  raceResults.addSubscriber(clientA);
	  raceResults.send(message);
	  verify(clientA, times(RaceType.values().length)).receive(message);
	 }
	 
	 @Test
	 public void unsubscribedClientShouldNotReceiveMessages() {
	  raceResults.addSubscriber(clientA);
	  raceResults.removeSubscriber(clientA);
	  raceResults.send(message);
	  verify(clientA, never()).receive(message);
	 }
	 
	 @Test
	 public void shouldReturnFalseOnNull() {
		 assertFalse(raceResults.removeSubscriber(null));
		 assertFalse(raceResults.addSubscriber(null));
		 assertFalse(raceResults.send(null)); 
		 assertFalse(raceResults.addSubscriber(null, null));
	}
	 
	@Test
	 public void subscribedClientsOnParticularRaceTypeShoulReceiveMessages() {
		raceResults.addSubscriber(clientA, RaceType.HORSE);
		 raceResults.send(message, RaceType.HORSE);
		 verify(clientA).receive(message);
	 }
	
	@Test
	public void shouldLogOnMessageWithRaceTypeSent() {
		raceResults.addSubscriber(clientA, RaceType.HORSE);
		when(message.toString()).thenReturn("hello world!");
		raceResults.send(message, RaceType.HORSE);
		verify(logger, times(1)).log(message);
	}
	
	@Test
	public void shouldLogOnMessageWithoutRaceTypeSent() {
		raceResults.addSubscriber(clientA);
		when(message.toString()).thenReturn("hello world!");
		raceResults.send(message);
		verify(logger, times(1)).log(message);
	}
	
	@Test
	public void shouldReceiveAnyNumberOfMessages() {
		int expectedNumber = (int) Math.random() * 100;
		raceResults.addSubscriber(clientA, RaceType.F1);
		for (int i = 0; i < expectedNumber; i++) {
			raceResults.send(mock(Message.class), RaceType.F1);
		}
		verify(clientA, times(expectedNumber)).receive(any(Message.class));
	}
	
	@Test
	public void shouldDoNothingWhenTryingUnsubscribeWhenNotSubscribed() {
		assertTrue(raceResults.removeSubscriber(clientA));
	}
	 
}
