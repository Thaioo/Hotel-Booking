CREATE DATABASE IF NOT EXISTS luxurioushotel;
SET TIME_ZONE="+0:00";
SHOW VARIABLES LIKE '%time_zone%';

USE luxurioushotel;

CREATE TABLE amenity(
	id int auto_increment,
	description varchar(255),
	
	primary key(id)
);

CREATE TABLE food_menu(
	id_menu int,
	id_food int,
	
	primary key(id_menu, id_food)
);

CREATE TABLE food(
	id int auto_increment,
	name varchar(255),
	description text,
	price decimal,
	image text,
	
	primary key(id)
);

CREATE TABLE room_amenity(
	id_room_type int,
	id_amenity int,
	
	primary key(id_room_type, id_amenity)
);

CREATE TABLE bed_type(
	id int auto_increment,
	name varchar(255),
	
	primary key(id)
);

CREATE TABLE menu(
	id int auto_increment,
	name varchar(255),
	
	primary key(id)
);

CREATE TABLE orders(
	id int auto_increment,
	id_food int,
	id_reservation int,
	quantity int,
	order_time timestamp default now(),
	id_status int,
	primary key(id)
);

CREATE TABLE order_status(
	id int auto_increment,
	name varchar(100),
	primary key(id)
);


CREATE TABLE room(
	id int auto_increment,
	name varchar(255) unique,
	id_room_type int,
	
	primary key(id)
);

CREATE TABLE room_type(
	id int auto_increment,
	name varchar(255) unique,
	overview varchar(255),
	price decimal,
	area float,
	capacity int,
	id_bed_type int,
	image text,
	
	primary key(id)
);

CREATE TABLE tables(
	id int auto_increment,
	name varchar(255),
	
	primary key(id)
);

CREATE TABLE room_booking(
	id_room int ,
	id_booking int,
	
	primary key(id_room, id_booking)
);

CREATE TABLE users(
	id int auto_increment,
	username varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci UNIQUE,
	password varchar(100),
	first_name varchar(100),
	last_name varchar(100),
	dob date,
	phone varchar(20) unique not null,
	email varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci UNIQUE,
	address text,
	summary text,
	image text,
	id_role int DEFAULT 1, /*mặc định là ROLE_GUEST*/
	deleted boolean DEFAULT FALSE,
	enabled boolean,
	
	primary key(id)
);

CREATE TABLE reservation(
	id int auto_increment,
	id_guest int ,
	id_table int,
	guest_number int,
	reservation_time timestamp,
	create_date timestamp default now(),
	
	primary key(id)
);


CREATE TABLE booking(
	id int auto_increment,
	check_in timestamp,
	check_out timestamp,
	room_number int,
	id_guest int ,
	adult_number int,
	children_number int,
	id_payment_status int,
	id_payment int,
	id_status int,
	paid_amount double,
	total decimal,
	create_date timestamp default now(),
	
	primary key(id)
);

CREATE TABLE roles(
	id int auto_increment,
	name varchar(50) unique,
	description varchar(100),
	primary key(id)
);

CREATE TABLE payment_status(
	id int auto_increment,
	name varchar(50),
	primary key(id)
	
);

CREATE TABLE payment_method(
	id int auto_increment,
	name varchar(50),
	primary key(id)
);

CREATE TABLE booking_status(
	id int auto_increment,
	name varchar(50),
	primary key(id)
);

CREATE TABLE invalid_token(
	id int auto_increment,
	token text,
	exp_time timestamp,
	primary key(id)
);

CREATE TABLE notification(
	id int auto_increment,
	title varchar(20),
	message varchar(100),
	noti_type varchar(100),
	create_time timestamp,
	unread boolean,
	primary key(id)
);

ALTER TABLE room_amenity ADD CONSTRAINT fk_id_room_type_room_amenity FOREIGN KEY(id_room_type) REFERENCES room_type(id);
ALTER TABLE room_amenity ADD CONSTRAINT fk_id_amenity_room_amenity FOREIGN KEY(id_amenity) REFERENCES amenity(id);
ALTER TABLE food_menu ADD CONSTRAINT fk_id_menu_food_menu FOREIGN KEY(id_menu) REFERENCES menu(id);
ALTER TABLE food_menu ADD CONSTRAINT fk_id_food_food_menu FOREIGN KEY(id_food) REFERENCES food(id);
ALTER TABLE orders ADD CONSTRAINT fk_id_food_orders FOREIGN KEY(id_food) REFERENCES food(id);
ALTER TABLE orders ADD CONSTRAINT fk_id_reservation_orders FOREIGN KEY(id_reservation) REFERENCES reservation(id);
ALTER TABLE orders ADD CONSTRAINT fk_id_status_orders FOREIGN KEY(id_status) REFERENCES order_status(id);
ALTER TABLE room_type ADD CONSTRAINT fk_id_bed_type_room_type FOREIGN KEY(id_bed_type) REFERENCES bed_type(id);
ALTER TABLE room ADD CONSTRAINT fk_id_room_type_room FOREIGN KEY(id_room_type) REFERENCES room_type(id);
ALTER TABLE reservation ADD CONSTRAINT fk_id_guest_reservation FOREIGN KEY(id_guest) REFERENCES users(id);
ALTER TABLE reservation ADD CONSTRAINT fk_id_table_reservation FOREIGN KEY(id_table) REFERENCES tables(id);
ALTER TABLE room_booking ADD CONSTRAINT fk_id_room_room_booking FOREIGN KEY(id_room) REFERENCES room(id);
ALTER TABLE room_booking ADD CONSTRAINT fk_id_booking_room_booking FOREIGN KEY(id_booking) REFERENCES booking(id);
ALTER TABLE users ADD CONSTRAINT fk_id_role_users FOREIGN KEY(id_role) REFERENCES roles(id);
ALTER TABLE booking ADD CONSTRAINT fk_id_guest_booking FOREIGN KEY(id_guest) REFERENCES users(id);
ALTER TABLE booking ADD CONSTRAINT fk_id_payment_status_booking FOREIGN KEY(id_payment_status) REFERENCES payment_status(id);
ALTER TABLE booking ADD CONSTRAINT fk_id_payment_booking FOREIGN KEY(id_payment) REFERENCES payment_method(id);
ALTER TABLE booking ADD CONSTRAINT fk_id_status_booking FOREIGN KEY(id_status) REFERENCES booking_status(id);

INSERT INTO `roles` (name) VALUES
('ROLE_GUEST'),
('ROLE_ADMIN'),
('ROLE_HOTEL_MANAGER'),
('ROLE_RES_MANAGER');


INSERT INTO bed_type (name)
VALUES ('Single Bed'),
('Double Bed'),
('Queen Bed'),
('King Bed'),
('Super King Bed'),
('Extra  Bed'),
('2 King Bed'),
('3 King Bed')
;

INSERT INTO room_type (name, overview, price, area, capacity, id_bed_type, image)
VALUES ('Standard Room', '', 200, 40, 2, 3,''),
('Superior Room', '', 300, 50, 2, 4,''),
('Deluxe Room', '', 500, 60, 2, 5,''),
('Suite Room', '', 1000, 300, 4, 5,''),
('Family Room', '', 900, 200, 6, 8,'')
;

-- Standard Room (id_room_type = 1)
INSERT INTO room (name, id_room_type) VALUES
('101', 1),
('102', 1),
('103', 1),
('104', 1);

-- Superior Room (id_room_type = 2)
INSERT INTO room (name, id_room_type) VALUES
('201', 2),
('202', 2),
('203', 2),
('204', 2);

-- Deluxe Room (id_room_type = 3)
INSERT INTO room (name, id_room_type) VALUES
('301', 3),
('302', 3),
('303', 3),
('304', 3);

-- Suite Room (id_room_type = 4)
INSERT INTO room (name, id_room_type) VALUES
('401', 4),
('402', 4),
('403', 4),
('404', 4);

-- Family Room (id_room_type = 5)
INSERT INTO room (name, id_room_type) VALUES
('501', 5),
('502', 5),
('503', 5),
('504', 5);


INSERT INTO amenity (description) VALUES
('Free Wi-Fi'),
('Swimming Pool'),
('Fitness Center'),
('24-Hour Reception'),
('Room Service'),
('Laundry Service'),
('Parking'),
('Air Conditioning'),
('Breakfast Included'),
('Airport Shuttle'),
('Spa and Wellness Center'),
('Pet-Friendly'),
('Non-Smoking Rooms'),
('Conference Facilities'),
('Restaurant'),
('Bar/Lounge'),
('Business Center'),
('Cable/Satellite TV'),
('Mini-Bar'),
('Balcony/Terrace')
;

-- Tiện ích cho Standard Room
INSERT INTO room_amenity (id_room_type, id_amenity) VALUES
(1, 1), -- Free Wi-Fi
(1, 4), -- 24-Hour Reception
(1, 7), -- Parking
(1, 8), -- Air Conditioning
(1, 17), -- Cable/Satellite TV
(1, 14), -- Conference Facilities
(1, 13); -- Non-Smoking Rooms

-- Tiện ích cho Superior Room
INSERT INTO room_amenity (id_room_type, id_amenity) VALUES
(2, 1), -- Free Wi-Fi
(2, 4), -- 24-Hour Reception
(2, 6), -- Laundry Service
(2, 8), -- Air Conditioning
(2, 13), -- Non-Smoking Rooms
(2, 14), -- Conference Facilities
(2, 17); -- Cable/Satellite TV

-- Tiện ích cho Deluxe Room
INSERT INTO room_amenity (id_room_type, id_amenity) VALUES
(3, 1), -- Free Wi-Fi
(3, 2), -- Swimming Pool
(3, 3), -- Fitness Center
(3, 4), -- 24-Hour Reception
(3, 5), -- Room Service
(3, 8), -- Air Conditioning
(3, 10), -- Airport Shuttle
(3, 13), -- Non-Smoking Rooms
(3, 17), -- Cable/Satellite TV
(3, 18), -- Mini-Bar
(3, 19); -- Balcony/Terrace

-- Tiện ích cho Suite Room
INSERT INTO room_amenity (id_room_type, id_amenity) VALUES
(4, 1), -- Free Wi-Fi
(4, 2), -- Swimming Pool
(4, 3), -- Fitness Center
(4, 4), -- 24-Hour Reception
(4, 5), -- Room Service
(4, 8), -- Air Conditioning
(4, 9), -- Breakfast Included
(4, 10), -- Airport Shuttle
(4, 12), -- Pet-Friendly
(4, 13), -- Non-Smoking Rooms
(4, 16), -- Bar/Lounge
(4, 17), -- Cable/Satellite TV
(4, 18), -- Mini-Bar
(4, 19); -- Balcony/Terrace

-- Tiện ích cho Family Room
INSERT INTO room_amenity (id_room_type, id_amenity) VALUES
(5, 1), -- Free Wi-Fi
(5, 2), -- Swimming Pool
(5, 3), -- Fitness Center
(5, 4), -- 24-Hour Reception
(5, 5), -- Room Service
(5, 7), -- Parking
(5, 8), -- Air Conditioning
(5, 9), -- Breakfast Included
(5, 11), -- Spa and Wellness Center
(5, 13), -- Non-Smoking Rooms
(5, 15), -- Restaurant
(5, 17), -- Cable/Satellite TV
(5, 18), -- Mini-Bar
(5, 19); -- Balcony/Terrace

INSERT INTO order_status (name) VALUES
('New Order'),
('Order Confirmed'),
('In the Kitchen'),
('Ready to Serve'),
('Served'),
('Completed'),
('Cancelled'),
('Pending Payment'),
('Paid'),
('Refunded');

INSERT INTO booking_status (name) VALUES
('Pending Confirmation'),
('Confirmed'),
('Checked In'),
('Checked Out'),
('Cancelled');

INSERT INTO payment_status (name) VALUES
('Pending'),
('Completed'),
('Failed'),
('Refunded'),
('Partially Paid'),
('Cancelled'),
('Awaiting Confirmation');

INSERT INTO payment_method (name) VALUES
('Credit Card'),
('Debit Card'),
('PayPal'),
('Bank Transfer'),
('Cash'),
('Gift Card');

-- Thêm dự liệu mẫu cho nhà hàng ----------------------------------------------------------------------------------------------------------------------
INSERT INTO tables (name) VALUES
('Table 1'),
('Table 2'),
('Table 3'),
('Table 4'),
('Table 5'),
('Table 6'),
('Table 7'),
('Table 8'),
('Table 9'),
('Table 10'),
('Table 11'),
('Table 12'),
('Table 13'),
('Table 14'),
('Table 15'),
('Table 16'),
('Table 17'),
('Table 18'),
('Table 19'),
('Table 20'); 

INSERT INTO menu (name) VALUES
('Breakfast'),
('Lunch'),
('Dinner'),
('Starters'),
('Beverages');

INSERT INTO food (name, description, image, price) VALUES
-- Breakfast items
('Pancakes', 'Fluffy pancakes served with maple syrup and butter.', 'pancakes.jpg', 8.99),
('Omelette', 'Three-egg omelette with cheese, mushrooms, and peppers.', 'omelette.jpg', 9.49),
('French Toast', 'Golden French toast served with syrup and fresh berries.', 'french_toast.jpg', 9.99),
('Breakfast Burrito', 'Tortilla filled with scrambled eggs, sausage, and cheese.', 'breakfast_burrito.jpg', 10.49),
('Bagel with Cream Cheese', 'Toasted bagel with a generous spread of cream cheese.', 'bagel_cream_cheese.jpg', 4.99),
('Avocado Toast', 'Toasted bread topped with mashed avocado and a sprinkle of salt.', 'avocado_toast.jpg', 6.49),
('Yogurt Parfait', 'Layers of yogurt, granola, and fresh fruit.', 'yogurt_parfait.jpg', 7.99),
('Smoothie Bowl', 'Blended smoothie topped with granola and fresh fruit.', 'smoothie_bowl.jpg', 8.49),
('Granola', 'Crunchy granola served with milk or yogurt.', 'granola.jpg', 5.99),
('Fruit Salad', 'A mix of fresh seasonal fruits.', 'fruit_salad.jpg', 6.99),

-- Lunch items
('Caesar Salad', 'Crisp romaine lettuce with Caesar dressing, croutons, and parmesan.', 'caesar_salad.jpg', 7.99),
('Grilled Chicken Sandwich', 'Grilled chicken breast with lettuce, tomato, and mayo on a toasted bun.', 'grilled_chicken_sandwich.jpg', 11.49),
('Turkey Club', 'Turkey, bacon, lettuce, and tomato on toasted bread.', 'turkey_club.jpg', 10.99),
('Veggie Wrap', 'Tortilla filled with mixed vegetables and hummus.', 'veggie_wrap.jpg', 9.49),
('BLT Sandwich', 'Bacon, lettuce, and tomato on toasted bread.', 'blt_sandwich.jpg', 10.49),
('Chicken Quesadilla', 'Tortilla filled with chicken and cheese, served with salsa.', 'chicken_quesadilla.jpg', 12.49),
('Tomato Soup', 'Hearty tomato soup served with a slice of bread.', 'tomato_soup.jpg', 8.99),
('Pasta Salad', 'Cold pasta salad with vegetables and Italian dressing.', 'pasta_salad.jpg', 9.99),
('Club Sandwich', 'Triple-decker sandwich with turkey, ham, bacon, lettuce, and tomato.', 'club_sandwich.jpg', 11.99),
('Fish Tacos', 'Soft tortillas filled with grilled fish, cabbage slaw, and sauce.', 'fish_tacos.jpg', 13.49),

-- Dinner items
('Spaghetti Carbonara', 'Classic Italian pasta with creamy egg sauce, pancetta, and parmesan.', 'spaghetti_carbonara.jpg', 13.99),
('Cheeseburger', 'Juicy beef burger with cheddar cheese, lettuce, tomato, and pickles.', 'cheeseburger.jpg', 12.99),
('Grilled Salmon', 'Salmon fillet grilled to perfection, served with vegetables.', 'grilled_salmon.jpg', 18.99),
('Roast Chicken', 'Roasted chicken with herbs and spices, served with mashed potatoes.', 'roast_chicken.jpg', 15.49),
('Beef Stroganoff', 'Tender beef in a creamy mushroom sauce, served over noodles.', 'beef_stroganoff.jpg', 16.99),
('Vegetarian Lasagna', 'Layers of pasta, cheese, and vegetables in a rich tomato sauce.', 'vegetarian_lasagna.jpg', 14.49),
('Pork Chops', 'Grilled pork chops with apple sauce, served with rice.', 'pork_chops.jpg', 17.99),
('Seafood Paella', 'Spanish rice dish with a mix of seafood and vegetables.', 'seafood_paella.jpg', 19.49),
('Ribeye Steak', 'Juicy ribeye steak cooked to your preference, served with fries.', 'ribeye_steak.jpg', 22.99),
('Chicken Alfredo', 'Pasta in a creamy Alfredo sauce with grilled chicken.', 'chicken_alfredo.jpg', 15.99),

-- Starters items
('Bruschetta', 'Toasted bread topped with tomato, basil, and garlic.', 'bruschetta.jpg', 6.99),
('Stuffed Mushrooms', 'Mushrooms filled with cheese and herbs, then baked.', 'stuffed_mushrooms.jpg', 7.49),
('Spring Rolls', 'Crispy rolls filled with vegetables and served with dipping sauce.', 'spring_rolls.jpg', 8.49),
('Chicken Wings', 'Spicy chicken wings served with celery and blue cheese dressing.', 'chicken_wings.jpg', 9.99),
('Nachos', 'Tortilla chips topped with cheese, jalapenos, and served with salsa.', 'nachos.jpg', 7.99),

-- Beverages items
('Coffee', 'Freshly brewed coffee.', 'coffee.jpg', 2.99),
('Tea', 'A variety of hot and iced teas.', 'tea.jpg', 2.49),
('Orange Juice', 'Freshly squeezed orange juice.', 'orange_juice.jpg', 3.49),
('Lemonade', 'Refreshing lemonade made with fresh lemons.', 'lemonade.jpg', 3.99),
('Soft Drink', 'Choice of cola, diet cola, or lemon-lime soda.', 'soft_drink.jpg', 2.49),
('Iced Coffee', 'Chilled coffee served over ice with milk.', 'iced_coffee.jpg', 3.99),
('Hot Chocolate', 'Rich hot chocolate topped with whipped cream.', 'hot_chocolate.jpg', 3.49),
('Milkshake', 'Creamy milkshake in a variety of flavors.', 'milkshake.jpg', 4.99),
('Sparkling Water', 'Chilled sparkling water with a hint of citrus.', 'sparkling_water.jpg', 3.49),
('Cocktail', 'Choice of popular cocktails like Mojito, Margarita, and Martini.', 'cocktail.jpg', 7.99);

-- Breakfast menu (id_menu = 1)
INSERT INTO food_menu (id_menu, id_food) VALUES
(1, 1), -- Pancakes
(1, 2), -- Omelette
(1, 3), -- French Toast
(1, 4), -- Breakfast Burrito
(1, 5), -- Bagel with Cream Cheese
(1, 6), -- Avocado Toast
(1, 7), -- Yogurt Parfait
(1, 8), -- Smoothie Bowl
(1, 9), -- Granola
(1, 10); -- Fruit Salad

-- Lunch menu (id_menu = 2)
INSERT INTO food_menu (id_menu, id_food) VALUES
(2, 11), -- Caesar Salad
(2, 12), -- Grilled Chicken Sandwich
(2, 13), -- Turkey Club
(2, 14), -- Veggie Wrap
(2, 15), -- BLT Sandwich
(2, 16), -- Chicken Quesadilla
(2, 17), -- Tomato Soup
(2, 18), -- Pasta Salad
(2, 19), -- Club Sandwich
(2, 20); -- Fish Tacos

-- Dinner menu (id_menu = 3)
INSERT INTO food_menu (id_menu, id_food) VALUES
(3, 21), -- Spaghetti Carbonara
(3, 22), -- Cheeseburger
(3, 23), -- Grilled Salmon
(3, 24), -- Roast Chicken
(3, 25), -- Beef Stroganoff
(3, 26), -- Vegetarian Lasagna
(3, 27), -- Pork Chops
(3, 28), -- Seafood Paella
(3, 29), -- Ribeye Steak
(3, 30); -- Chicken Alfredo

-- Starters menu (id_menu = 4)
INSERT INTO food_menu (id_menu, id_food) VALUES
(4, 31), -- Bruschetta
(4, 32), -- Stuffed Mushrooms
(4, 33), -- Spring Rolls
(4, 34), -- Chicken Wings
(4, 35); -- Nachos

-- Beverages menu (id_menu = 5)
INSERT INTO food_menu (id_menu, id_food) VALUES
(5, 36), -- Coffee
(5, 37), -- Tea
(5, 38), -- Orange Juice
(5, 39), -- Lemonade
(5, 40), -- Soft Drink
(5, 41), -- Iced Coffee
(5, 42), -- Hot Chocolate
(5, 43), -- Milkshake
(5, 44), -- Sparkling Water
(5, 45); -- Cocktail


-- username: admin | password: admin
INSERT INTO users (username , password, first_name, last_name, dob, phone, email, address, summary, id_role) VALUES 
('admin', '$2a$12$bsO6yLnL4.7nsn9DOvLqqeY6oolhWIrpFEwKHzvGnywyT/rZWNJim', 'admin', 'luxurious', '1990-01-01', '1234567890', 'admin@gmail.com', 'admin', '', 2);

-- username: user | password: user
INSERT INTO users (username , password, first_name, last_name, dob, phone, email, address, summary, id_role) VALUES 
('user', '$2a$12$kXpBtvYgGvRtAkvXvgv3fugdX1oxyxXY7EfI4LveoSlqVPT5xUpWq', 'user', 'luxurious', '1990-01-01', '1234567891', 'user@gmail.com', 'user', '', 1);

-- Thêm hình ảnh cho room_type
UPDATE room_type
SET image = CASE id
    WHEN 1 THEN 'istockphoto-954121470-612x612.jpg,
istockphoto-1066999762-612x612.jpg'
    WHEN 2 THEN 'istockphoto-1066999762-612x612.jpg,
istockphoto-1266155634-612x612.jpg'
    WHEN 3 THEN 'istockphoto-1266155634-612x612.jpg,
istockphoto-1318363878-612x612.jpg'
    WHEN 4 THEN 'istockphoto-1318363878-612x612.jpg,
istockphoto-1334117334-612x612.jpg'
    WHEN 5 THEN 'istockphoto-1334117334-612x612.jpg,
istockphoto-1370825295-612x612.jpg'
END
WHERE id IN (1, 2, 3, 4, 5);