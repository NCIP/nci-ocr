ALTER TABLE credential DROP COLUMN nih_oer_issued;
ALTER TABLE submitted_certificate ADD COLUMN issuer_id int8;