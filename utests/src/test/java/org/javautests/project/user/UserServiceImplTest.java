package org.javautests.project.user;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

	private UserService userService;
	private SecurityService securityService;
	private UserDAO userDAO;
	
	@Before
	public void setUp() {
		userDAO = mock(UserDAO.class);
		securityService = mock(SecurityService.class);
		userService = new UserServiceImpl(userDAO, securityService);
	}
	
	@Test
	public void shouldUpdatePassword() throws Exception {
		User dummy = new User();
		char[] password = new char[] {'q', 'w', 'e', 'r', 't', 'y'};
		char[] hashed = new char[] {'h', 'a', 's', 'h', 'e', 'd'};
		dummy.setName("John Doe");
		dummy.setPassword(password);
		
		when(securityService.md5(password)).thenReturn(hashed);
		userService.assignPassword(dummy);
		verify(securityService, times(1)).md5(password);
		verify(userDAO, times(1)).updateUser(dummy);	
		assertEquals(new String(hashed), new String(dummy.getPassword()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullUser() throws Exception {
		userService.assignPassword(null);
	}
	
	
	
}
