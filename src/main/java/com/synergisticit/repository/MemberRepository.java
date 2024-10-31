package com.synergisticit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Category;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.User;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	@Query("SELECT COALESCE(MAX(m.memberId), 0) FROM Member m")
	Long getMaxId();
	
	Member findByMemberId(Long mId);
	Member findMemberByUser(User user);
	
}
