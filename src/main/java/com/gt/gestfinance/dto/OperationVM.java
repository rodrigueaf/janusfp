package com.gt.gestfinance.dto;

import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationDetail;

import java.util.List;

/**
 * Operation View model
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public class OperationVM {

    public Operation operation;
    public List<OperationDetail> operationDetails;
}
