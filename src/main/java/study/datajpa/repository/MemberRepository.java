package study.datajpa.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entitiy.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") //named Query
    List<Member> findByUsername(@Param("username") String username); //Param은 Member Entity namedQuery에 있는 파라미터에 쓰임

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto(); //dto로 조회할 때는 new operation을 꼭 써야함

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names); //IN 조건으로 조회를 할 시

    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberListByUsername(String username); //단건

    Optional<Member> findOptionalListByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable); //리스트 와 count 분리

//    Slice<Member> findByAge(int age, Pageable pageable);
    
    @Modifying(clearAutomatically = true) //excuteUpdate 역할, clearAutomatically 역할은 영속성 컨텍스트 안에 있는 데이터를 없앰
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team") //n + 1 쿼리 실행 문제 해결 (쿼리가 여러번 select 될 때)
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) //JPQL로 일일히 작성해야하는 것을 간단 해결
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);
}
