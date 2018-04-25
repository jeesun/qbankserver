-- 二维码
CREATE TABLE qr_code (
  id bigserial not NULL PRIMARY KEY,
  username VARCHAR(256) NOT NULL UNIQUE,
  token VARCHAR(256) NOT NULL,
  sid VARCHAR(256) NOT NULL,
  is_ok bool DEFAULT FALSE
);

-- 用户信息
CREATE TABLE user_info (
  id bigserial not NULL PRIMARY KEY,
  address VARCHAR(256),
  age int4,
  birth VARCHAR(256),
  email VARCHAR(256),
  head_photo VARCHAR(256),
  person_brief VARCHAR(256),
  phone VARCHAR(256),
  sex bool DEFAULT FALSE,
  user_id int8,
  username VARCHAR(256),
  visit_card VARCHAR(256)
);

-- 验证码
CREATE TABLE veri_code (
  id bigserial not NULL PRIMARY KEY,
  code int4,
  create_time int8,
  expires int4,
  phone VARCHAR(256) UNIQUE
);