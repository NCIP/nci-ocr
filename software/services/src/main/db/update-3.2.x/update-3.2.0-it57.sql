create table ctepform1572_labs
(
ctepform1572_id bigint not null, lab_id bigint not null,
primary key (ctepform1572_id, lab_id), unique (ctepform1572_id, lab_id)
);


create table ctepform1572_practicesites
(
ctepform1572_id bigint not null, practice_site_id bigint not null, primary key (ctepform1572_id, practice_site_id),
unique (ctepform1572_id, practice_site_id)
);


create table ctepform1572_irbs
(
ctepform1572_id bigint not null, irb_id bigint not null, primary key (ctepform1572_id, irb_id),
unique (ctepform1572_id, irb_id)
);


alter table ctepform1572_labs add constraint ctepform1572_fkey foreign key (ctepform1572_id) references ctep_form_1572;
alter table ctepform1572_labs add constraint ctepform1572_lab_fkey foreign key (lab_id) references organization;

alter table ctepform1572_practicesites add constraint ctepform1572_fkey foreign key (ctepform1572_id) references ctep_form_1572;
alter table ctepform1572_practicesites add constraint ctepform1572_practicesite_fkey foreign key (practice_site_id) references organization;

alter table ctepform1572_irbs add constraint ctepform1572_fkey foreign key (ctepform1572_id) references ctep_form_1572;
alter table ctepform1572_irbs add constraint ctepform1572_irb_fkey foreign key (irb_id) references organization;


alter table registration add column ctep_financial_disclosure_id int8;
alter table registration add column annual_form_1572_id int8;
alter table registration add column supplemental_investigator_data_form_id int8;

alter table registration add constraint registration_ctep_financial_disclosure_fkey foreign key (ctep_financial_disclosure_id) references ctep_financial_disclosure;
alter table registration add constraint registration_annual_form_1572_fkey foreign key (annual_form_1572_id) references ctep_form_1572;
alter table registration add constraint registration_supplemental_investigator_data_form_fkey foreign key (supplemental_investigator_data_form_id) references supplemental_investigator_data_form;

update registration set ctep_financial_disclosure_id = ctep_financial_disclosure.id from ctep_financial_disclosure where registration.id = ctep_financial_disclosure.registration_id;
update registration set annual_form_1572_id = ctep_form_1572.id from ctep_form_1572 where registration.id = ctep_form_1572.registration_id;
update registration set supplemental_investigator_data_form_id = supplemental_investigator_data_form.id from supplemental_investigator_data_form where registration.id = supplemental_investigator_data_form.registration_id;

alter table ctep_financial_disclosure drop column registration_id;
alter table ctep_form_1572 drop column registration_id;
alter table supplemental_investigator_data_form drop column registration_id;

update firebird_file set length=273922 where file_name='ctep_form_fda_1572.pdf'
