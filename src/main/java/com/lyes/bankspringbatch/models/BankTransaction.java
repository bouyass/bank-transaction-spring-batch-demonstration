package com.lyes.bankspringbatch.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class BankTransaction {

    @Id
    private Long transactionId;
    private Long transactionAccountId;
    private Date transactionDate;
    @Transient
    private String strTransactionDate;
    private String transactionType;
    private double transactionAmount;
}
