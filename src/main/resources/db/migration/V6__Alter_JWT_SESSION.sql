ALTER TABLE jwt_session
ALTER COLUMN access_token TYPE varchar(2048);

ALTER TABLE jwt_session
ALTER COLUMN refresh_token TYPE varchar(2048);