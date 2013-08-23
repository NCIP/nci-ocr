-- Initial set of forms supported in FIREBIRD

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/form_1572.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'FDA Form 1572: Statement of Investigator',
    190796,
    'form_1572.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id, template_id, signing_field) values
(
    currval('hibernate_sequence'),
    '1572',
    'FDA Form 1572: Statement of Investigator',
    'FORM_1572',
    'REQUIRED',
    'NONE',
    currval('hibernate_sequence'),
    currval('hibernate_sequence'),
    'signature'
);

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/FIREBIRD_CV_sample.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'Curriculum Vitae',
    6226,
    'FIREBIRD_CV_sample.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id, signing_field) values
(
    currval('hibernate_sequence'),
    'CV',
    'Curriculum Vitae',
    'CV',
    'REQUIRED',
    'REQUIRED',
    currval('hibernate_sequence'),
    'signature'
);

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/dcp_financial_disclosure_form.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'Financial Disclosure Form',
    45723,
    'dcp_financial_disclosure_form.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, template_id, sample_id, signing_field) values
(
    currval('hibernate_sequence'),
    'Financial Disclosure',
    'Financial Disclosure Form',
    'FINANCIAL_DISCLOSURE_FORM',
    'REQUIRED',
    'REQUIRED',
    currval('hibernate_sequence'),
    currval('hibernate_sequence'),
    'signature'
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id) values
(
    nextval('hibernate_sequence'),
    'Human Research Certificate',
    'Protection of Human Research Subjects Training Certificate',
    'HUMAN_RESEARCH_CERTIFICATE',
    'REQUIRED',
    'REQUIRED',
    null
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id) values
(
    nextval('hibernate_sequence'),
    'Additional Attachments',
    'Additional Attachments',
    'ADDITIONAL_ATTACHMENTS',
    'SUPPLEMENTARY',
    'SUPPLEMENTARY',
    null
);

