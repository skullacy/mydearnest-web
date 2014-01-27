package com.osquare.mydearnest.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;

public class DetailModifyStatus {
	private static HashMap<String, String> currentModify;
	// 키: post.getId(), 밸류: account.getName()
	private static HashMap<String, String> currentModifyName;
	
	//현재 Account가 수정하고 있는 Post 갱신
	public static Boolean updateModifyStatus(Account account, Post post) {
		if(checkModifyStatus(account, post)) 
			return setCurrentModify(account, post);
		
		return false;
	}
	
	public static Boolean releaseModifyStatus(Account account) {
		Iterator<Map.Entry<String, String>> iterator = DetailModifyStatus.currentModify.entrySet().iterator();
		Iterator<Map.Entry<String, String>> nameIterator = DetailModifyStatus.currentModifyName.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			if(entry.getKey().equals(String.valueOf(account.getId()))) {
				iterator.remove();
			}
		}
		
		while(nameIterator.hasNext()) {
			Map.Entry<String, String> entry = nameIterator.next();
			if(entry.getValue().equals(String.valueOf(account.getName()))) {
				nameIterator.remove();
			}
		}
		
		return false;
	}
	
	public static Boolean checkModifyStatus(Account account, Post post) {
		return checkModifyStatus(account.getId(), post.getId());
	}
	
	//해당 포스트를 수정하고 있는 계정이 있는지 체크
	public static Boolean checkModifyStatus(long accountId, long postId) {
		HashMap<String, String> currentModify = getCurrentModify();
		if(currentModify != null) {
			for(String currentAccountId : currentModify.keySet()) {
				if (String.valueOf(postId).equals(currentModify.get(String.valueOf(accountId)))) return true;
				else if(currentModify.get(currentAccountId).equals(String.valueOf(postId))) return false;
			}
		}
		return true;
	}
	
	
	//해당 Account에 Post를 수정중으로 등록
	public static Boolean setCurrentModify(Account account, Post post) {
		releaseModifyStatus(account);
		DetailModifyStatus.currentModify.put(String.valueOf(account.getId()), String.valueOf(post.getId()));
		DetailModifyStatus.currentModifyName.put(String.valueOf(post.getId()), account.getName());
		
		return true;
	}
	
	//현재 Account별 수정중인 Post id 리턴
	public static HashMap<String, String> getCurrentModify() {
		if(DetailModifyStatus.currentModify == null) DetailModifyStatus.currentModify = new HashMap<String, String>();
		return DetailModifyStatus.currentModify;
	}
	
	// Account name 리턴
	public static HashMap<String, String> getCurrentModifyName() {
		if(DetailModifyStatus.currentModifyName == null) DetailModifyStatus.currentModifyName = new HashMap<String, String>();
		return DetailModifyStatus.currentModifyName;
	}
	
	
	public static void viewCurrentStatus() {
		System.out.println("=========== VIEW CURRENT STATUS ===========");
		if(currentModify != null) {
			for(String currentAccount: currentModify.keySet()) {
				System.out.println("Account: " + currentAccount + " Post: " + currentModify.get(currentAccount));
			}
		}
		System.out.println("=================== END ===================");
	}
	
	public static void cleanCurrentModify() {
		DetailModifyStatus.currentModify = null;
		DetailModifyStatus.currentModifyName = null;
	}
	
}
