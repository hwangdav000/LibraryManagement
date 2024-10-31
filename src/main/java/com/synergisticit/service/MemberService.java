package com.synergisticit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergisticit.domain.Member;
import com.synergisticit.domain.User;

public interface MemberService {
	Member saveMember(Member a);
	Member findMemberById(Long aId);
	Member findMemberByUser(User user);
	List<Member> findAllMembers();
	Page<Member> findAllMembers(Pageable pageable);
	Member updateMember(Member a);
	void deleteMember(Long aId);
	void updateBalance(Long amount, Long userId);
	Long getMaxId();
}
