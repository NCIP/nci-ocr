ALTER TABLE annual_registration_configuration ALTER sub_investigator_text TYPE varchar(500);
ALTER TABLE annual_registration_configuration ALTER protocol_text TYPE varchar(450);

update firebird_file set length=306894 where file_name='ctep_form_fda_1572.pdf';

