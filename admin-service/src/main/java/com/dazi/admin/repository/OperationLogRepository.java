package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.OperationLog;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends BaseMapper<OperationLog> {
}
