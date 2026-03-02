package com.example.guardpay.global.config;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속하는 엔티티에 아래 필드들을 컬럼으로 추가합니다.
@EntityListeners(AuditingEntityListener.class) // JPA에게 Auditing 기능을 사용한다고 알립니다.
public abstract class BaseEntity {

    @CreatedDate // 엔티티가 생성될 때의 시간을 자동으로 저장합니다.
    @Column(name = "created_at", updatable = false) // 생성 시간은 수정되면 안 되므로 updatable=false
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때의 시간을 자동으로 저장합니다.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}