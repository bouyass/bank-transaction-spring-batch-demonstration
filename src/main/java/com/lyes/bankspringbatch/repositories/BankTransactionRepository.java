package com.lyes.bankspringbatch.repositories;

import com.lyes.bankspringbatch.models.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
}
