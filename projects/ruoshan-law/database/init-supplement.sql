-- 补充数据库初始化脚本
-- 添加更多测试数据

USE ruoshan_law;

-- 添加更多律师数据
INSERT INTO lawyers (name, title, introduction, specialty, email, phone) VALUES
('孙律师', '合伙人', '14年执业经验，专注房地产和建设工程领域', '房地产,建设工程', 'sun@ruoshan.com', '13800138007'),
('周律师', '资深律师', '9年执业经验，专注劳动人事和婚姻家庭', '劳动人事,婚姻家庭', 'zhou@ruoshan.com', '13800138008');

-- 添加更多案例数据
INSERT INTO cases (title, type, summary, result, lawyer, created_at) VALUES
('某劳动争议案', '民商事诉讼', '代理员工处理劳动争议，成功获得经济补偿', '胜诉，获赔15万元', '陈律师', '2024-01-10'),
('某离婚财产分割案', '民商事诉讼', '代理离婚案件，成功维护当事人财产权益', '调解成功，财产合理分割', '周律师', '2024-02-15'),
('某建筑工程合同纠纷案', '民商事诉讼', '代理建筑公司处理合同纠纷，涉案金额800万元', '和解成功，收回工程款600万元', '孙律师', '2024-01-20');

-- 添加更多新闻数据
INSERT INTO news (title, category, summary, created_at) VALUES
('若山律师事务所成立15周年庆典圆满举行', '律所动态', '庆祝若山律师事务所成立15周年，回顾发展历程，展望美好未来', '2024-01-08'),
('如何应对劳动争议？律师为您支招', '法律解读', '详细解读劳动争议的常见类型和应对策略，帮助劳动者维护合法权益', '2024-01-25'),
('若山律师事务所与某大型企业签订常年法律顾问合同', '律所动态', '本所成功与某大型企业签订常年法律顾问合同，将为其提供全方位法律服务', '2024-02-05');

-- 创建索引优化查询
CREATE INDEX idx_lawyers_specialty ON lawyers(specialty);
CREATE INDEX idx_cases_type ON cases(type);
CREATE INDEX idx_cases_created_at ON cases(created_at);
CREATE INDEX idx_news_category ON news(category);
CREATE INDEX idx_news_created_at ON news(created_at);
CREATE INDEX idx_careers_active ON careers(active);
