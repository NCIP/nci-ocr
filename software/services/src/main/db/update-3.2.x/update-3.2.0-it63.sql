alter table credential ADD column nih_oer_issued bool default false;
update credential set nih_oer_issued = true where nihOerIssued = true;
alter table credential DROP column nihOerIssued;

alter table registration ADD column coordinator_comments text;
alter table registration RENAME COLUMN comments to sponsor_comments;

create index person_ctep_id_index on person (ctep_id);
create index person_nes_id_index on person (nes_id);

alter table registration ADD column approval_acknowledged_by_investigator bool default false;
alter table registration ADD column approval_acknowledged_by_coordinator bool default false;

update firebird_file set length=30613 where file_name='FIREBIRD_CV_sample.pdf';