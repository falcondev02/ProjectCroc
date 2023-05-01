
-- Таблица с информацией о пользователях
CREATE TABLE IF NOT EXISTS user_data (
    id 		   	  INTEGER 	   AUTO_INCREMENT PRIMARY KEY,
    user_login    VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_role     VARCHAR(5)   NOT NULL DEFAULT 'USER',
    	CONSTRAINT uk_user_data_user_login UNIQUE (user_login)
);

-- Таблица с тестами
CREATE TABLE IF NOT EXISTS test (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    english_sentence VARCHAR(255) NOT NULL,
    russian_sentence VARCHAR(255) NOT NULL,
    words_list VARCHAR(255) ARRAY
 
);

-- Таблица с результатами тестов
CREATE TABLE IF NOT EXISTS test_result (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    rating INTEGER NOT NULL,
    test_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    user_answer VARCHAR(255) NOT NULL,
    CONSTRAINT fk_test_result_test FOREIGN KEY (test_id) REFERENCES test(id),
    CONSTRAINT fk_test_result_user FOREIGN KEY (user_id) REFERENCES user_data(id)
);

-- Таблица связи пользователей и тестов
CREATE TABLE IF NOT EXISTS user_data_test (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_data_id INTEGER NOT NULL,
    test_id INTEGER NOT NULL,
    CONSTRAINT fk_user_data_test_user_data FOREIGN KEY (user_data_id) REFERENCES user_data(id),
    CONSTRAINT fk_user_data_test_test FOREIGN KEY (test_id) REFERENCES test(id)
);

