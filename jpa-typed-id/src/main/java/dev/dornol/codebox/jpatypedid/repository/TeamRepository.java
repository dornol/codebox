package dev.dornol.codebox.jpatypedid.repository;

import dev.dornol.codebox.jpatypedid.entity.Team;
import dev.dornol.codebox.jpatypedid.entity.TeamId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, TeamId> {
}
