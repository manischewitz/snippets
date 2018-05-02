package org.javautests.project.user;

public class UserServiceImpl implements UserService {
	
	 private UserDAO userDAO;
	 private SecurityService securityService;
	 
	 @Override
	 public void assignPassword(User user) throws Exception {
		 if (user == null) {
			 throw new IllegalArgumentException();
		 }
		 char[] passwordMd5 = securityService.md5(user.getPassword());
		 user.setPassword(passwordMd5);
		 userDAO.updateUser(user);
	 }
	 
	 public UserServiceImpl(UserDAO dao, SecurityService security) {
		 this.userDAO = dao;
		 this.securityService = security;
	 }
	 
	}
