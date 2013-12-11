package com.osquare.mydearnest.admin.service;

import java.util.Collection;

import com.osquare.mydearnest.entity.Account;


public interface AdminAccountService {

	Long sizeOfAccount();
	Collection<Account> findAccount(int page);
	Account updateRole(String userId, String role);


}
