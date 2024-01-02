package study.datajpa.entitiy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) //entity 로그 찍을 때
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //1 대 N관계 매핑, 연관관계는 기본적으로 다 지연 로딩으로 설정
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){ //팀이 변경이 될 시
        this.team = team;
        team.getMembers().add(this);
    }
}
