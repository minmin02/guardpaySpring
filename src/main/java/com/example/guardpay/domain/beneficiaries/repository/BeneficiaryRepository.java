package com.example.guardpay.domain.beneficiaries.repository;

import com.example.guardpay.domain.beneficiaries.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByActiveTrue();
}
