
ALTER TABLE users
ADD COLUMN IF NOT EXISTS primary_role VARCHAR(50);

UPDATE users
SET primary_role = 'ROLE_USER'
WHERE primary_role IS NULL;


