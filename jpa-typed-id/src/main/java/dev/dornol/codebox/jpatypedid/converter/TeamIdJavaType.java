package dev.dornol.codebox.jpatypedid.converter;

import dev.dornol.codebox.jpatypedid.entity.TeamId;

public class TeamIdJavaType extends EntityIdJavaType<TeamId> {
    protected TeamIdJavaType() {
        super(TeamId.class, TeamId::of);
    }
}
