INSERT INTO dbo."user" (user_id, user_name, user_email, user_password) VALUES (0,'John Doe', 'john@example.com', '1234');
INSERT INTO dbo."user" (user_id, user_name, user_email, user_password) VALUES (0,'Jane Smith', 'jane@example.com', '1234');

INSERT INTO category(category_id,category_name) VALUES (0,'FOOD');
INSERT INTO category(category_id,category_name) VALUES (0,'RENT');
INSERT INTO category(category_id,category_name) VALUES (0,'SUPPLIES');
INSERT INTO category(category_id,category_name) VALUES (0,'EDUCATION');
INSERT INTO category(category_id,category_name) VALUES (0,'HEALTH');
INSERT INTO category(category_id,category_name) VALUES (0,'GAS');

INSERT INTO expense (expense_id, expense_name, expense_value, expense_date, user_id,category_id,created_at, updated_at ) VALUES (0,'', 235, '2025-03-02', 1, 1,'2025-03-02','2025-03-02');
