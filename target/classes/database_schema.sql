CREATE TABLE USERS (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(25) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL
);

CREATE TABLE GROUPS (
    group_id SERIAL PRIMARY KEY,
    reading_text_path VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    administrator_user_id INT NOT NULL,
    FOREIGN KEY (administrator_user_id) REFERENCES USERS(user_id)
);

CREATE TABLE CATEGORIES (
    category_id SERIAL PRIMARY KEY,
    category_label VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE COMMENTS (
    comment_id SERIAL PRIMARY KEY,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id),
    text_section VARCHAR(100) NOT NULL,
    comment_content VARCHAR(100) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    comment_page_number INT NOT NULL,
    comment_page_occurrence INT NOT NULL
);

CREATE TABLE USERS_GROUPS (
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (group_id) REFERENCES GROUPS(group_id)
);
