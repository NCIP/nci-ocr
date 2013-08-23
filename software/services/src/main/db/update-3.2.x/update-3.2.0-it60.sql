update firebird_file set length=293940 where file_name='ctep_form_fda_1572.pdf';

alter table annual_registration_configuration add column protocol_text varchar(240);
alter table annual_registration_configuration add column sub_investigator_text varchar(400);
