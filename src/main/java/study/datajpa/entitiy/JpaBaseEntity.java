package study.datajpa.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속받은 클래스에게 속성을 사용할 수 있게끔 하는 어노테이션
public class JpaBaseEntity {
    @Column(updatable = false) //update 불가
    private LocalDateTime creadDate;
    private LocalDateTime updatedDate;

    @PrePersist //등록 전 이벤트 실행
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        creadDate = now;
        updatedDate = now;
    }

    @PreUpdate //수정 전 이벤트 실행
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
