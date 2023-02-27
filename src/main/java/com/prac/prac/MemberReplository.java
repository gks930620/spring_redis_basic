package com.prac.prac;

import com.prac.prac.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReplository   extends JpaRepository<Member,String>{

}
