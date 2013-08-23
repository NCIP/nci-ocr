ALTER TABLE organization RENAME COLUMN nes_id TO external_id;
ALTER TABLE organization_snapshot RENAME COLUMN nes_id TO external_id;
ALTER TABLE person RENAME COLUMN nes_id TO external_id;
ALTER TABLE person_snapshot RENAME COLUMN nes_id TO external_id;
ALTER INDEX person_nes_id_index RENAME TO person_external_id_index