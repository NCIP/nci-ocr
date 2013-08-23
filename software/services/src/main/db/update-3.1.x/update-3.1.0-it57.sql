alter table protocol_registration add column curriculum_vitae_id int8;
alter table protocol_registration add column financial_disclosure_id int8;
alter table protocol_registration add column form_1572_id int8;
alter table protocol_registration add column human_research_certificate_form_id int8;
alter table protocol_registration add column additional_attachments_id int8;

alter table protocol_registration add constraint registration_curriculum_vitae_fkey foreign key (curriculum_vitae_id) references curriculum_vitae;
alter table protocol_registration add constraint registration_financial_disclosure_fkey foreign key (financial_disclosure_id) references financial_disclosure;
alter table protocol_registration add constraint registration_form_1572_fkey foreign key (form_1572_id) references form_1572;
alter table protocol_registration add constraint registration_human_research_certificate_form_fkey foreign key (human_research_certificate_form_id) references human_research_certificate_form;
alter table protocol_registration add constraint registration_additional_attachments_fkey foreign key (additional_attachments_id) references additional_attachments;

update protocol_registration set curriculum_vitae_id = curriculum_vitae.id from curriculum_vitae where protocol_registration.id = curriculum_vitae.registration_id;
update protocol_registration set financial_disclosure_id = financial_disclosure.id from financial_disclosure where protocol_registration.id = financial_disclosure.registration_id;
update protocol_registration set form_1572_id = form_1572.id from form_1572 where protocol_registration.id = form_1572.registration_id;
update protocol_registration set human_research_certificate_form_id = human_research_certificate_form.id from human_research_certificate_form where protocol_registration.id = human_research_certificate_form.registration_id;
update protocol_registration set additional_attachments_id = additional_attachments.id from additional_attachments where protocol_registration.id = additional_attachments.registration_id;

alter table curriculum_vitae drop column registration_id;
alter table financial_disclosure drop column registration_id;
alter table form_1572 drop column registration_id;
alter table human_research_certificate_form drop column registration_id;
alter table additional_attachments drop column registration_id;
