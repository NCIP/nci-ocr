UPDATE registration SET current_registration_id = current_registration_revision_id WHERE current_registration_id is null;