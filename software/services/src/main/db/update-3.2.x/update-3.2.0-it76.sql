create index registration_status_index on registration (status);

update firebird_file set length = 349970 where id = (select template_id from form_type where form_type_enum = 'FORM_1572');
update firebird_file set length = 242175 where id = (select template_id from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM');
update firebird_file set length = 354202  where id = (select template_id from form_type where form_type_enum = 'CTEP_FORM_1572');
update firebird_file set length = 60554 where id = (select template_id from form_type where form_type_enum = 'CTEP_FINANCIAL_DISCLOSURE_FORM');
update firebird_file set length = 7206 where id = (select sample_id from form_type where form_type_enum = 'CV');

update form_type set sample_id = null, template_id = null where form_type_enum = 'SUPPLEMENTAL_INVESTIGATOR_DATA_FORM';

delete from firebird_file where file_name = 'ctep_supplemental_investigator_data_form.pdf';

delete from byte_data_source where resource_path = '/files/ctep_supplemental_investigator_data_form.pdf.gz';

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/ctep_sidf_sample.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'Supplemental Investigator Data Form',
    6876,
    'ctep_sidf_sample.pdf',
    'now',
    currval('hibernate_sequence')
);

update form_type set sample_id = currval('hibernate_sequence') where form_type_enum = 'SUPPLEMENTAL_INVESTIGATOR_DATA_FORM';
