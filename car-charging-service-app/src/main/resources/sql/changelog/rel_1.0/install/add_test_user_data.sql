set search_path to ccs;

UPDATE "user" SET provider_user_id = 'testProviderUserId' WHERE user_id = 'testUserId';
UPDATE "user" SET user_access_key = 'testUserAccessKey' WHERE user_id = 'testUserId';