package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericService;

import javax.ejb.Local;

/**
 * Protocol Validation Service.
 */
@Local
public interface ProtocolValidationService extends GenericService<Protocol> {

    /**
     * Validates changes to a protocol.
     *
     * @param revised Revised Protocol
     * @param comment Protocol change comment
     * @throws ValidationException Exception
     */
    void validateChanges(Protocol revised, String comment) throws ValidationException;


    /**
     * Validates a Protocol.
     *
     * @param protocol Protocol
     * @throws ValidationException Exception
     */
    void validate(Protocol protocol) throws ValidationException;

}
