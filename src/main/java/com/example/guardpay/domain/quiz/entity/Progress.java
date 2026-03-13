package com.example.guardpay.domain.quiz.entity;

import com.example.guardpay.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // FK

    private Integer progress;

    private LocalDateTime updateAt;

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void updateProgress(int increase) {
        this.progress += increase;

        if (this.progress > 100) {
            this.progress = 100;
        }
        this.updateAt = LocalDateTime.now();
    }
}
