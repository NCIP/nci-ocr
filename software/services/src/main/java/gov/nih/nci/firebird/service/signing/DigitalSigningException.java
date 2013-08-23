package gov.nih.nci.firebird.service.signing;

/**
 * Customized Exception for Digital Signing.
 */
public class DigitalSigningException extends Exception {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     *
     * @param inErrorMessage Error message customized.
     * @param e Exception
     */
    DigitalSigningException(String inErrorMessage, Exception e) {
        super(inErrorMessage, e);
    }

 }
