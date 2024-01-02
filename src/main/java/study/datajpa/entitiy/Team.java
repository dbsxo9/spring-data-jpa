package study.datajpa.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // 1 대 N 관계 매핑 (Team은 여러 명의 회원을 보유할 수 있다. 주로 FK가 없는 ENTITY에 적는 것을 권장)
    private List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }
}
