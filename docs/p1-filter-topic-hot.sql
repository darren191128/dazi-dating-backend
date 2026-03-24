-- P1功能数据库脚本：高级筛选 + 每日精选 + 话题系统 + 热门推荐

-- =============================================
-- 1. 用户资料扩展字段（用于高级筛选）
-- =============================================
ALTER TABLE user_profile
ADD COLUMN height INT COMMENT '身高（cm）',
ADD COLUMN education VARCHAR(32) COMMENT '学历：高中/大专/本科/硕士/博士',
ADD COLUMN occupation VARCHAR(64) COMMENT '职业',
ADD COLUMN income INT COMMENT '月收入',
ADD COLUMN has_house TINYINT DEFAULT 0 COMMENT '是否有房：0-未填 1-有 2-无',
ADD COLUMN has_car TINYINT DEFAULT 0 COMMENT '是否有车：0-未填 1-有 2-无',
ADD COLUMN marital_status VARCHAR(32) COMMENT '婚姻状况：未婚/离异/丧偶';

-- =============================================
-- 2. 话题系统表
-- =============================================

-- 话题表
CREATE TABLE IF NOT EXISTS topic (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL COMMENT '话题名称',
    description VARCHAR(256) COMMENT '话题描述',
    icon VARCHAR(256) COMMENT '话题图标',
    cover VARCHAR(256) COMMENT '话题封面',
    post_count INT DEFAULT 0 COMMENT '帖子数',
    follow_count INT DEFAULT 0 COMMENT '关注数',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sort_order (sort_order),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题表';

-- 动态话题关联表
CREATE TABLE IF NOT EXISTS moment_topic (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    moment_id BIGINT NOT NULL COMMENT '动态ID',
    topic_id BIGINT NOT NULL COMMENT '话题ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_moment_topic (moment_id, topic_id),
    INDEX idx_moment_id (moment_id),
    INDEX idx_topic_id (topic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态话题关联表';

-- 用户话题关注表
CREATE TABLE IF NOT EXISTS user_topic_follow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    topic_id BIGINT NOT NULL COMMENT '话题ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_topic (user_id, topic_id),
    INDEX idx_user_id (user_id),
    INDEX idx_topic_id (topic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户话题关注表';

-- =============================================
-- 3. 初始化话题数据
-- =============================================
INSERT INTO topic (name, description, icon, sort_order, status) VALUES
('今日穿搭', '分享你的每日穿搭，展示个人风格', '/static/topics/fashion.png', 1, 1),
('美食分享', '分享美食探店、自制美食', '/static/topics/food.png', 2, 1),
('旅行日记', '记录旅途中的美好时光', '/static/topics/travel.png', 3, 1),
('运动健身', '运动打卡，健康生活', '/static/topics/sports.png', 4, 1),
('音乐推荐', '分享你喜欢的音乐', '/static/topics/music.png', 5, 1),
('电影讨论', '电影推荐与影评交流', '/static/topics/movie.png', 6, 1),
('宠物日常', '萌宠日常分享', '/static/topics/pet.png', 7, 1),
('职场生活', '职场经验、工作感悟', '/static/topics/work.png', 8, 1),
('相亲交友', '真诚交友，寻找另一半', '/static/topics/dating.png', 9, 1),
('周末计划', '周末去哪儿玩', '/static/topics/weekend.png', 10, 1);

-- =============================================
-- 4. 动态表扩展字段（用于热门算法）
-- =============================================
ALTER TABLE moment
ADD COLUMN hot_score DOUBLE DEFAULT 0 COMMENT '热度分数',
ADD INDEX idx_hot_score (hot_score);

-- =============================================
-- 5. 用户资料完整度相关字段
-- =============================================
ALTER TABLE user_profile
ADD COLUMN profile_completeness INT DEFAULT 0 COMMENT '资料完整度百分比';

-- =============================================
-- 6. 用户照片数量统计（用于每日精选）
-- =============================================
CREATE TABLE IF NOT EXISTS user_photo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    photo_url VARCHAR(512) NOT NULL COMMENT '照片URL',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户照片表';
