-- 若山律师事务所官网数据库初始化脚本
-- 数据库: ruoshan_law
-- 创建日期: 2026-03-12

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ruoshan_law 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE ruoshan_law;

-- 律师表
CREATE TABLE IF NOT EXISTS lawyers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '律师姓名',
    title VARCHAR(100) NOT NULL COMMENT '职位',
    introduction TEXT COMMENT '简介',
    photo_url VARCHAR(500) COMMENT '照片URL',
    specialty VARCHAR(200) COMMENT '专业领域',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='律师信息表';

-- 案例表
CREATE TABLE IF NOT EXISTS cases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '案例标题',
    type VARCHAR(50) NOT NULL COMMENT '案例类型',
    summary TEXT COMMENT '案例摘要',
    content TEXT COMMENT '案例详情',
    result VARCHAR(200) COMMENT '案件结果',
    lawyer VARCHAR(100) COMMENT '承办律师',
    image_url VARCHAR(500) COMMENT '案例图片',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='案例表';

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '新闻标题',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    summary TEXT COMMENT '摘要',
    content TEXT COMMENT '内容',
    image_url VARCHAR(500) COMMENT '图片URL',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- 招聘表
CREATE TABLE IF NOT EXISTS careers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL COMMENT '职位名称',
    location VARCHAR(100) NOT NULL COMMENT '工作地点',
    experience VARCHAR(50) NOT NULL COMMENT '经验要求',
    salary VARCHAR(100) NOT NULL COMMENT '薪资范围',
    requirements TEXT COMMENT '职位要求',
    active BOOLEAN DEFAULT TRUE COMMENT '是否有效',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘表';

-- 联系记录表
CREATE TABLE IF NOT EXISTS contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) NOT NULL COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    type VARCHAR(50) NOT NULL COMMENT '咨询类型',
    content TEXT NOT NULL COMMENT '咨询内容',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/PROCESSING/COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系记录表';

-- 插入测试数据 - 律师
INSERT INTO lawyers (name, title, introduction, specialty, email, phone) VALUES
('张律师', '高级合伙人', '20年执业经验，专注民商事诉讼领域，曾代理多起重大疑难案件', '民商事诉讼,合同纠纷', 'zhang@ruoshan.com', '13800138001'),
('李律师', '合伙人', '15年执业经验，专注刑事辩护，具有丰富的刑事案件办理经验', '刑事辩护,经济犯罪', 'li@ruoshan.com', '13800138002'),
('王律师', '资深律师', '10年执业经验，专注知识产权领域，服务众多知名企业', '知识产权,商标专利', 'wang@ruoshan.com', '13800138003'),
('赵律师', '资深律师', '12年执业经验，专注公司法务，为多家上市公司提供法律顾问服务', '公司法务,股权设计', 'zhao@ruoshan.com', '13800138004'),
('陈律师', '律师', '8年执业经验，专注劳动人事和婚姻家庭领域', '劳动人事,婚姻家庭', 'chen@ruoshan.com', '13800138005'),
('刘律师', '律师', '6年执业经验，专注房地产和建设工程领域', '房地产,建设工程', 'liu@ruoshan.com', '13800138006');

-- 插入测试数据 - 案例
INSERT INTO cases (title, type, summary, result, lawyer, created_at) VALUES
('某上市公司股权纠纷案', '公司法务', '成功帮助客户解决股权纠纷，维护股东合法权益，涉案金额5000万元', '和解成功，为客户挽回损失3000万元', '张律师', '2024-01-15'),
('某知名企业商标维权案', '知识产权', '代理知名企业进行商标维权，成功制止侵权行为并获得赔偿', '胜诉，获赔500万元', '王律师', '2024-02-10'),
('某合同纠纷案', '民商事诉讼', '代理客户处理复杂合同纠纷，通过调解方式快速解决争议', '调解成功，合同继续履行', '李律师', '2024-03-05'),
('某经济犯罪辩护案', '刑事辩护', '为涉嫌经济犯罪的被告人提供辩护，成功为当事人争取从轻处罚', '从轻处罚，缓刑执行', '李律师', '2024-02-20'),
('某专利侵权案', '知识产权', '代理科技公司进行专利维权，成功制止竞争对手的侵权行为', '胜诉，停止侵权并赔偿', '王律师', '2024-01-25'),
('某企业并购重组案', '公司法务', '为某大型企业的并购重组提供全程法律服务', '交易顺利完成，金额2亿元', '赵律师', '2023-12-20');

-- 插入测试数据 - 新闻
INSERT INTO news (title, category, summary, created_at) VALUES
('若山律师事务所荣获2024年度优秀律师事务所称号', '律所动态', '凭借专业的法律服务和良好的客户口碑，若山律师事务所荣获年度优秀律师事务所称号', '2024-03-15'),
('新《公司法》解读：企业需要注意的十大变化', '法律解读', '新修订的《公司法》即将实施，本文为您解读企业需要重点关注的十大变化', '2024-03-10'),
('张律师受邀参加国际法律论坛并发表演讲', '律所动态', '本所高级合伙人张律师受邀参加国际法律论坛，就跨境投资法律风险发表主题演讲', '2024-03-05'),
('成功代理某上市公司股权纠纷案', '成功案例', '本所成功代理某上市公司股权纠纷案，为客户挽回经济损失3000万元', '2024-02-28'),
('劳动合同解除的法律风险与防范', '法律解读', '本文详细分析劳动合同解除过程中的法律风险，并提供相应的防范建议', '2024-02-20'),
('若山律师事务所开展公益法律咨询活动', '律所动态', '为回馈社会，本所组织开展公益法律咨询活动，为社区居民提供免费法律咨询服务', '2024-02-15');

-- 插入测试数据 - 招聘
INSERT INTO careers (title, location, experience, salary, requirements, active) VALUES
('执业律师', '北京市', '1-3年', '15K-25K', '法学本科及以上学历，持有律师执业证;1-3年执业经验，有民商事诉讼经验优先;良好的沟通能力和团队协作精神;责任心强，工作认真负责', TRUE),
('资深律师', '北京市', '3-5年', '25K-40K', '法学本科及以上学历，持有律师执业证;3-5年执业经验，能独立承办案件;有专业领域特长，如知识产权、公司法务等;具备团队管理和业务拓展能力', TRUE),
('律师助理', '北京市', '应届生', '8K-12K', '法学本科及以上学历，通过司法考试优先;良好的法律功底和文字表达能力;学习能力强，工作积极主动;有志于长期从事律师职业', TRUE);
