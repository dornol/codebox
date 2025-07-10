package dev.dornol.codebox.jpatypedid.repository;

import dev.dornol.codebox.jpatypedid.entity.Member;
import dev.dornol.codebox.jpatypedid.entity.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, MemberId> {
}
