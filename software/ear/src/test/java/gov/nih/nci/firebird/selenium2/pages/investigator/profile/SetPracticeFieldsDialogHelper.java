package gov.nih.nci.firebird.selenium2.pages.investigator.profile;

import gov.nih.nci.firebird.data.PracticeSiteType;
import org.apache.commons.lang3.StringUtils;

public class SetPracticeFieldsDialogHelper {

    private SetPracticeSiteFieldsDialog dialog;

    public SetPracticeFieldsDialogHelper(SetPracticeSiteFieldsDialog dialog) {
        this.dialog = dialog;
    }

    public void selectPracticeSiteType(PracticeSiteType type) {
        dialog.selectPracticeSiteType(type == null ? "" : type.name());
    }

    public PracticeSiteType getSelectedPracticeSiteType() {
        String selectedValue = dialog.getSelectedPracticeSiteType();
        return StringUtils.isEmpty(selectedValue) ? null : PracticeSiteType.valueOf(selectedValue);
    }
}
