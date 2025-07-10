package dev.dornol.codebox.jpatypedid.entity;

import dev.dornol.codebox.jpatypedid.converter.MemberIdJavaType;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JavaType;
import org.hibernate.type.YesNoConverter;

@Entity
public class Member extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("id..")
    @Id
    @Column(nullable = false, updatable = false)
    @JavaType(MemberIdJavaType.class)
    @Convert(disableConversion = true)
    private MemberId id;

    private String name;

    private String tel;

    @Convert(converter = YesNoConverter.class)
    private boolean use;

    protected Member() {
        /* empty */
    }

    public Member(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public MemberId getId() {
        return id;
    }

    public boolean isUse() {
        return use;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", use='" + use + '\'' +
                '}';
    }
}
