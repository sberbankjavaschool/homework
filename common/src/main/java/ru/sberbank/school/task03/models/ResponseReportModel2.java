package ru.sberbank.school.task03.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Модель данных для отчета
 *
 * @author Gregory Melnikov at 15.05.2019
 */

@Getter
@Setter
public class ResponseReportModel {
    int maxResponsesCount;
    String maxResponses;

    Map<String, ReportMiddleBlock> symbols = new HashMap<>();

    @Getter
    @Setter
    class ReportMiddleBlock {
        String symbol;
        BigDecimal maxAmount;
        BigDecimal allAmount = BigDecimal.ZERO;
        BigDecimal profitableAmount;
        BigDecimal unprofitableAmount;
        int symbolResponseCount;
        double minAmount = Double.POSITIVE_INFINITY;
        double profitablePrice;
        double unprofitablePrice = Double.POSITIVE_INFINITY;

        List<ReportResponse> reportResponses = new ArrayList<>();
    }

    @Getter
    @Setter
    class ReportResponse {
        String responseDate;
        String responseDirection;
        BigDecimal responseAmount;
        double responsePrice;
        boolean responseIsNotFound;
    }
}