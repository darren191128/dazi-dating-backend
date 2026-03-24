package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.FinanceDailyReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanceDailyReportRepository extends BaseMapper<FinanceDailyReport> {
    
    @Select("SELECT * FROM finance_daily_report WHERE report_date BETWEEN #{start} AND #{end} ORDER BY report_date DESC")
    List<FinanceDailyReport> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Select("SELECT * FROM finance_daily_report WHERE report_date = #{date}")
    FinanceDailyReport findByDate(@Param("date") LocalDate date);
}
