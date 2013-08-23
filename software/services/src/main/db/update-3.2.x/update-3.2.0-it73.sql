update byte_data_source
set resource_path = '/files/OCR_CV_sample.pdf.gz'
where resource_path = '/files/FIREBIRD_CV_sample.pdf.gz';

update firebird_file
set file_name = 'OCR_CV_sample.pdf', length = 6888
where file_name = 'FIREBIRD_CV_sample.pdf';

alter table registration add column approval_date timestamp;
alter table registration add column current_registration_revision_id int8;
alter table registration add constraint current_registration_revision_fkey foreign key (current_registration_revision_id) references registration;

update registration set current_registration_revision_id = current_registration_id, current_registration_id = null;