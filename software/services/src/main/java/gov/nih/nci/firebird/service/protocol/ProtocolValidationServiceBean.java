package gov.nih.nci.firebird.service.protocol;

import com.google.common.collect.Sets;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.exception.ValidationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Collection;
import java.util.Set;

/**
 * Service for Protocol Validation.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProtocolValidationServiceBean extends AbstractProtocolServiceBean<Protocol>
        implements ProtocolValidationService {

    private static final Set<FormOptionality> SUBINVESTIGATOR_REQUIRED_OPTIONALITIES = Sets.newHashSet(
            FormOptionality.REQUIRED, FormOptionality.OPTIONAL);

    @Override
    public void validate(Protocol protocol) throws ValidationException {
        if (hasDuplicateProtocolNumber(protocol)) {
            String message = getResources().getString("sponsor.protocol.duplicate.number.error");
            ValidationResult result = new ValidationResult(new ValidationFailure("protocol.protocolNumber", message));
            throw new ValidationException(result);
        }
    }

    @Override
    public void validateChanges(Protocol revised, String comment) throws ValidationException {
        ValidationResult validationResults = new ValidationResult();
        checkComments(comment, validationResults);
        checkInvestigatorOptionalities(revised, validationResults);
        checkSubInvestigatorOptionalities(revised, validationResults);
        if (!validationResults.isValid()) {
            getSession().evict(revised);
            throw new ValidationException(validationResults);
        }
    }

    private void checkComments(String comment, ValidationResult validationResults) {
        if (StringUtils.isBlank(comment)) {
            validationResults.addFailure(new ValidationFailure("comment", getResources()
                    .getString("protocol.change.comment.required")));
        }
    }

    private void checkInvestigatorOptionalities(Protocol revised, ValidationResult validationResults) {
        Collection<FormOptionality> optionalities = revised.getRegistrationConfiguration()
                .getInvestigatorConfiguration().getFormOptionalities().values();
        if (!optionalities.contains(FormOptionality.REQUIRED)) {
            validationResults.addFailure(new ValidationFailure(getResources()
                    .getString("protocol.investigator.optionalities.require.one.required.form")));
        }
    }

    private void checkSubInvestigatorOptionalities(Protocol revised, ValidationResult validationResults) {
        Collection<FormOptionality> optionalities = revised.getRegistrationConfiguration()
                .getSubinvestigatorConfiguration().getFormOptionalities().values();
        if (!CollectionUtils.containsAny(optionalities, SUBINVESTIGATOR_REQUIRED_OPTIONALITIES)) {
            String message = getResources()
                    .getString("protocol.subinvestigator.optionalities.require.one.required.or.optional.form");
            validationResults.addFailure(new ValidationFailure(message));
        }
    }
}
