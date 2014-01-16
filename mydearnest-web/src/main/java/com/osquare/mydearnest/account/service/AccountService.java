package com.osquare.mydearnest.account.service;

import com.osquare.mydearnest.account.vo.AccountDetails;
import com.osquare.mydearnest.account.vo.JoinDefault;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.profile.vo.AccountSummary;

public interface AccountService {

	Account findAccountById(long id);
	Account findAccountByUserName(String signname);
	Account findAccountBySocial(String type, String name);

	AccountSummary findAccountSummaryBySocial(String type, String name, long visitor_id);
	AccountSummary findAccountSummaryById(long id, long visitor_id);
    
	boolean isNotFoundUser(String username);
	Account createAccount(JoinDefault vo) throws Exception;
	Account enabledAccount(AccountDetails vo, ImageSource source);
	Account updateSignInfo(Account account, String remoteAddr);

	Account updateAccount(long account_id, AccountDetails vo, ImageSource imageSource);
	void updateAccountPassword	(long account_id, String password);
	
	void updateSocialTokens(long id, String string);
	
	Account refreshAccountPassword(String mailAddress);
	void updateAccount(Account me);
	
	long getTotalModifierCount();
	
}
