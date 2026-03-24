CREATE TABLE IF NOT EXISTS call_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    caller_id BIGINT NOT NULL COMMENT '主叫用户ID',
    callee_id BIGINT NOT NULL COMMENT '被叫用户ID',
    call_type TINYINT NOT NULL COMMENT '通话类型：1语音 2视频',
    status TINYINT NOT NULL COMMENT '状态：0待接听 1通话中 2已结束 3已拒绝 4未接听',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    duration INT COMMENT '通话时长（秒）',
    room_id VARCHAR(64) COMMENT '房间ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_caller_id (caller_id),
    INDEX idx_callee_id (callee_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通话记录表';
