package dev.dornol.codebox.jpatypedid.entity;

import dev.dornol.codebox.jpatypedid.converter.TeamIdJavaType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JavaType;

@Entity
public class Team extends BaseEntity {

    @Comment("id..")
    @Id
    @Column(nullable = false, updatable = false)
    @JavaType(TeamIdJavaType.class)
    @Convert(disableConversion = true)
    private TeamId id;

    private String name;

    public Team() {
        /* empty */
    }

    public Team(TeamId id, String name) {
        this.id = id;
        this.name = name;
    }

    public TeamId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
