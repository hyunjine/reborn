-- =============================================
-- Reborn Store ERD - SQL Schema (MySQL)
-- =============================================

-- 업체 기본 정보 테이블
CREATE TABLE stores (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL COMMENT '업체명',
    address    VARCHAR(255) NOT NULL COMMENT '업체 주소',
    description TEXT        NOT NULL COMMENT '업체 소개 텍스트',
    phone_number VARCHAR(20) NOT NULL COMMENT '업체 전화번호',
    last_updated DATETIME   NOT NULL COMMENT '시세 최종 업데이트 시각',
    PRIMARY KEY (id)
) COMMENT = '업체 기본 정보 테이블';

-- 업체 이미지 테이블 (stores 1:N)
CREATE TABLE store_images (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    store_id  BIGINT       NOT NULL COMMENT '소속 업체 외래키',
    image_url VARCHAR(500) NOT NULL COMMENT '이미지 URL',
    PRIMARY KEY (id),
    CONSTRAINT fk_store_images_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
) COMMENT = '업체 이미지 테이블';

-- 업체 영업시간 테이블 (stores 1:N)
CREATE TABLE store_business_hours (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    store_id    BIGINT      NOT NULL COMMENT '소속 업체 외래키',
    day_of_week VARCHAR(10) NOT NULL COMMENT '요일 (e.g. MONDAY)',
    is_open     BOOLEAN     NOT NULL COMMENT '해당 요일 영업 여부',
    open_time   TIME        NULL     COMMENT '영업 시작 시각 (휴무일이면 NULL)',
    close_time  TIME        NULL     COMMENT '영업 종료 시각 (휴무일이면 NULL)',
    PRIMARY KEY (id),
    CONSTRAINT fk_store_business_hours_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
) COMMENT = '업체 영업시간 테이블';

-- 업체 매입 시세 테이블 (stores 1:N)
CREATE TABLE store_prices (
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    store_id BIGINT      NOT NULL COMMENT '소속 업체 외래키',
    name     VARCHAR(50) NOT NULL COMMENT '품목명 (e.g. 고철, 알루미늄)',
    price    VARCHAR(50) NOT NULL COMMENT '단가 텍스트 (e.g. 450원/kg)',
    PRIMARY KEY (id),
    CONSTRAINT fk_store_prices_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
) COMMENT = '업체 매입 시세 테이블';
