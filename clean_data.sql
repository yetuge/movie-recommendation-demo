-- 数据清洗脚本：去除用户表中字符串字段的首尾空格
-- 执行前请备份数据库！

USE movie_recommend_db;

-- 去除邮箱字段的首尾空格
UPDATE ordinary_user SET user_mailbox = TRIM(user_mailbox);

-- 去除手机号字段的首尾空格
UPDATE ordinary_user SET phone_number = TRIM(phone_number);

-- 去除密码字段的首尾空格
UPDATE ordinary_user SET user_password = TRIM(user_password);

-- 去除用户名字段的首尾空格
UPDATE ordinary_user SET user_name = TRIM(user_name);

-- 去除性别字段的首尾空格
UPDATE ordinary_user SET gender = TRIM(gender);

-- 查看清洗后的数据
SELECT user_id, user_name, user_mailbox, phone_number, user_password, gender
FROM ordinary_user;
