set search_path to ccs;

INSERT INTO "user" (user_id, email, registration_date, status)
VALUES ('testUserId', 'testUserEmail@gmail.com', '2020-01-15', 'ACTIVE');
INSERT INTO vehicle (user_id, vehicle_id, vendor)
VALUES ((select id from "user" where "user".user_id = 'testUserId'), 'testVehicleId', 'BMW');