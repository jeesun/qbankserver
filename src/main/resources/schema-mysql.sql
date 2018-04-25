-- 二维码
CREATE TABLE qr_code (
  id INT(8) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(256) NOT NULL UNIQUE,
  token VARCHAR(256) NOT NULL,
  sid VARCHAR(256) NOT NULL,
  is_ok tinyint(1) DEFAULT NULL
);

-- 用户信息
CREATE TABLE user_info (
  id INT(8) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  address VARCHAR(256),
  age INT(4),
  birth VARCHAR(256),
  email VARCHAR(256),
  head_photo VARCHAR(256),
  person_brief VARCHAR(256),
  phone VARCHAR(256),
  sex tinyint(1) DEFAULT NULL,
  user_id INT(8),
  username VARCHAR(256),
  visit_card VARCHAR(256)
);

-- 验证码
CREATE TABLE veri_code (
  id INT(8) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  code INT(4),
  create_time INT(8),
  expires INT(4),
  phone VARCHAR(256) UNIQUE
);