JPA
- 개발자는 핵심 비즈니스 로직을 개발하는데 집중을 할 수가 있다.



- 스프링 MVC
- 스프링 ORM
- JPA, 하이버네이트
- 스프링 데이터 JPA
- H2 데이터베이스
- 커넥션 풀 : HikariCP
- 로깅 SLF4J & LogBack
- Junit5 테스트


-    properties:
      hibernate:
        format_sql: true
  -> jpa의 로그를 모두 콘솔창에 찍음


- @GeneratedValue
  -> jpa가 알아서 시퀀스나 id값을 찾아줌


-@PersistenceContext
  -> EntityManager이라는 엔티티 관리 클래스(영속성 컨텍스트)를 가져와 알아서 insert, select 해줌




- Query Method
  -> 도메인에 특화된 조회를 할 수 있게 하는 기능
  -> findByUsernameAndAgeGreaterThan -> username이 equal 조건으로 먼저 들어가고 age 컬럼이 greaterThan에 의해서 더 큰 경우의 조건


- JPA NamedQuery
  -> 쿼리에 메소드 이름을 붙여 호출하는 방식
  -> repository entity 타입.메소드명 형태로 되어있는 namedQuery 먼저 찾고 없으면 Query Method로 찾는 순서임 (namedQuery가 Query Method보다 우선순위)


- @Query, repository 메소드에 쿼리 정의
  -> repository 메소드 위에 Query 어노테이션을 이용하여 쿼리를 작성하고 실행하는 방식
  -> 복잡한 쿼리에 적합



- JPA 페이징
  -> repository에서 선언 시 Pageable 인자로 선언
  -> 소스 구현 시 PageRequest 선언 후 PageRequest.of안에 페이지번호, 갯수, 정렬(Sort.by(Sort.Direction)) 순서대로 구현
  -> 이후 page.getContent()로 페이징 처리 결과 확인
  -> page.getNumber()는 페이지 번호
  -> page.getTotalPages())는 총 페이지 갯수
  -> page.hasNext()는 다음 페이지가 존재하느냐


- Slice
  -> limit + 1 로 최대 갯수 + 1로 가져옴


- 리스트와 count 분리
@Query(value = "select m from Member m left join m.team",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable); //리스트 와 count 분리
  -> value 와 countQuery 사용


- page처리 결과를 API에서 응답할 때
  -> API에서 응답할 때 entity로 응답하면 절대 안되고 DTO로 변환해서 응답해줘야 하므로 map을 사용
  -> Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


- bulk 수정 쿼리
  -> 주의할 점 : bulk 수정 쿼리를 작성하게 되면 update 이후에 조회가 필요할 때 꼭 @Modifying(clearAutomatically = true) //excuteUpdate 역할, clearAutomatically 역할은 영속성 컨텍스트 안에 있는 데이터를 없앰
를 쓰자 !


- fetchjoin
  -> 실제 전체 데이터를 가져올 때 row가 10개면 10개 select쿼리를 날려야하기 때문에 성능에 문제가 발생
  -> @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin(); 로 작성
  
  -> @Query로 쿼리를 일일히 작성하지 않고 싶으면 
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
처럼 Override해서 사용


- @Transactional(readOnly=true) 가 있으면 jpa에서 flush를 미실행 변경감지 등을 실행 안함 -> 약간의 db 읽기 성능이 향상될 수 있음


- implements Persistable<String>
  -> isNew() 오버라이딩 선언하고 @createdDate를 선언한 뒤 return createdDate == null로 반환
  -> save() 구현체 중 isNew()가 createdDate를 null로 인식하여 persist() 실행
