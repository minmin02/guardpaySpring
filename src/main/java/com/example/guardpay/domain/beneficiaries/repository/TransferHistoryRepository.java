package com.example.guardpay.domain.beneficiaries.repository;

import com.example.guardpay.domain.beneficiaries.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
}
