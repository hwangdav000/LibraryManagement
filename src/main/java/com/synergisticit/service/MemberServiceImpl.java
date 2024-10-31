package com.synergisticit.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Member;
import com.synergisticit.domain.User;
import com.synergisticit.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired MemberRepository mRepo;
	@Autowired UserService uService;
	
	@Override
	public Member saveMember(Member m) {
		// Save membership date as new user
	    m.setMembershipDate(new Date());
	    
		return mRepo.save(m);
	}

	@Override
	public Member findMemberById(Long mId) {
		return mRepo.findByMemberId(mId);
	}

	@Override
	public List<Member> findAllMembers() {
		return mRepo.findAll();
	}

	@Override
	public Member updateMember(Member m) {
		Optional<Member> member= mRepo.findById(m.getMemberId());
		if (member.isEmpty()) {
			// member does not exist
			return null;
		}
		
		return mRepo.save(m);
	}

	@Override
	public void deleteMember(Long mId) {
		mRepo.deleteById(mId);
	}

	@Override
	public void updateBalance(Long amount, Long userId) {
		User u = uService.getUserById(userId);
		if (u == null) return;
		Member m = u.getMember();
		
		Double balance = m.getFineBalance();
		balance = amount-balance;
		if (balance < 0) balance = 0.00;
		m.setFineBalance(balance);
		mRepo.save(m);
	}

	@Override
	public Member findMemberByUser(User user) {
		
		return mRepo.findMemberByUser(user);
	}

	@Override
	public Long getMaxId() {
		
		return mRepo.getMaxId();
	}

	@Override
	public Page<Member> findAllMembers(Pageable pageable) {
		
		return mRepo.findAll(pageable);
	}


}
