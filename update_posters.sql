-- =====================================================
-- 电影海报图片更新脚本
-- 用于将 movie_information 表的 image_url 从默认值替换为真实海报
-- =====================================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- 添加 image_url 列（如果不存在）
-- =====================================================
ALTER TABLE movie_information ADD COLUMN IF NOT EXISTS image_url VARCHAR(255) DEFAULT 'default.jpg';

-- =====================================================
-- 方案一：按电影 ID 批量更新（推荐）
--
-- 适用场景：如果图片文件命名为 1.jpg, 2.jpg, 3.jpg... 对应 movie_id
--
-- 执行此方案前，请确保 images/ 目录下有 1.jpg 到 10.jpg 文件
-- =====================================================

-- UPDATE movie_information SET image_url = CONCAT(movie_id, '.jpg');


-- =====================================================
-- 方案二：按电影名称精准更新
--
-- 适用场景：如果图片文件名为中文名称（如 星际穿越.jpg, 盗梦空间.jpg...）
--
-- 执行此方案前，请确保 images/ 目录下有以下文件：
--   - 盗梦空间.jpg
--   - 星际穿越.jpg
--   - 黑客帝国.jpg
--   - 肖申克的救赎.jpg
--   - 教父.jpg
--   - 阿甘正传.jpg
--   - 泰坦尼克号.jpg
--   - 罗马假日.png
--   - 乱世佳人.jpg
--   - 千与千寻.jpg
-- =====================================================

UPDATE movie_information SET image_url = '盗梦空间.jpg' WHERE movie_name = '盗梦空间';
UPDATE movie_information SET image_url = '星际穿越.jpg' WHERE movie_name = '星际穿越';
UPDATE movie_information SET image_url = '黑客帝国.jpg' WHERE movie_name = '黑客帝国';
UPDATE movie_information SET image_url = '肖申克的救赎.jpg' WHERE movie_name = '肖申克的救赎';
UPDATE movie_information SET image_url = '教父.jpg' WHERE movie_name = '教父';
UPDATE movie_information SET image_url = '阿甘正传.jpg' WHERE movie_name = '阿甘正传';
UPDATE movie_information SET image_url = '泰坦尼克号.jpg' WHERE movie_name = '泰坦尼克号';
UPDATE movie_information SET image_url = '罗马假日.jpg' WHERE movie_name = '罗马假日';
UPDATE movie_information SET image_url = '乱世佳人.jpg' WHERE movie_name = '乱世佳人';
UPDATE movie_information SET image_url = '千与千寻.jpg' WHERE movie_name = '千与千寻';


-- =====================================================
-- 执行完成后，可以使用以下查询验证结果
-- =====================================================

-- SELECT movie_id, movie_name, image_url FROM movie_information ORDER BY movie_id;
