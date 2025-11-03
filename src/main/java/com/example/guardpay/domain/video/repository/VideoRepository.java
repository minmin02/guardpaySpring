package com.example.guardpay.domain.video.repository;

import com.example.guardpay.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
