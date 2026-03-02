package com.example.guardpay.domain.diagnosis.repository;

import com.example.guardpay.domain.diagnosis.entity.DiagnosisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisHistoryRepository extends JpaRepository<DiagnosisHistory, Long> {
}
