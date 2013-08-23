package gov.nih.nci.firebird.service.signing;

/**
 * Certificate Generation Attributes.
 */
public class CertificateGenerationAttributes {

    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String organizationUnit;
    private String organization;
    private String city;
    private String stateProvince;
    private String countryCode;
    private String emailAddress;
    private long serialNumber;
    private int validDays;
    private String alias;
    private String keystorePassword;

    /**
     * @return isValidate
     */
    public boolean isValid() {

        return !(this.getFirstName() == null
                || this.getFirstName().trim().length() == 0
                || this.getLastName() == null || this.getLastName().trim().length() == 0
                || this.getOrganization() == null || this.getOrganization().trim().length() == 0
                || this.getOrganizationUnit() == null || this.getOrganizationUnit().trim().length() == 0
                || this.getEmailAddress() == null || this.getEmailAddress().trim().length() == 0
                || this.getSerialNumber() < 0
                || this.getValidDays() < 0
                || this.getAlias() == null || this.getAlias().trim().length() == 0
                || this.getKeystorePassword() == null || this.getKeystorePassword().trim().length() == 0
         );
    }


    /**
     * @return EmailAddress
    */
   public String getEmailAddress() {
       return emailAddress;
   }
   /**
     * @param emailAddress EmailAddress
    */
   public void setEmailAddress(String emailAddress) {
       this.emailAddress = emailAddress;
   }
   /**
    * @return title
   */
   public String getTitle() {
       return title;
   }
   /**
    * @param title Title
   */
   public void setTitle(String title) {
       this.title = title;
   }
   /**
    * @return firstName
   */
   public String getFirstName() {
       return firstName;
   }
   /**
    * @param firstName First Name
   */
   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }
   /**
    * @return middleName
   */
   public String getMiddleName() {
       return middleName;
   }
   /**
    * @param middleName  Middle Name
   */
   public void setMiddleName(String middleName) {
       this.middleName = middleName;
   }
   /**
    * @return lastName
   */
   public String getLastName() {
       return lastName;
   }
   /**
    * @param lastName Last Name
   */
   public void setLastName(String lastName) {
       this.lastName = lastName;
   }
   /**
    * @return city.
   */
   public String getCity() {
       return city;
   }
   /**
    * @param city city
   */
   public void setCity(String city) {
       this.city = city;
   }
   /**
    * @return stateProvince
   */
   public String getStateProvince() {
       return stateProvince;
   }
   /**
    * @param stateProvince stateProvince
   */
   public void setStateProvince(String stateProvince) {
       this.stateProvince = stateProvince;
   }
   /**
    * @return countryCode
   */
   public String getCountryCode() {
       return countryCode;
   }
   /**
    * @param countryCode countryCode
   */
   public void setCountryCode(String countryCode) {
       this.countryCode = countryCode;
   }
    /**
     * @return organizationUnit
     */
    public String getOrganizationUnit() {
        return organizationUnit;
    }
    /**
     * @param organizationUnit organizationUnit
     */
    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
    /**
     * @return organization
     */
    public String getOrganization() {
        return organization;
    }
    /**
     * @param organization organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return the keystorePassword
     */
    public String getKeystorePassword() {
        return keystorePassword;
    }
    /**
     * @param keystorePassword the keystorePassword to set
     */
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    /**
     * @return common name
     */
    public String constructCommonName() {

        StringBuffer commonName = new StringBuffer();

        if (this.getTitle() != null) {
            commonName.append(this.getTitle()).append(' ');
        }

        commonName.append(this.getFirstName());

        if (this.getMiddleName() != null) {
            commonName.append(' ').append(this.getMiddleName());
        }

        if (this.getLastName() != null) {
            commonName.append(' ').append(this.getLastName());
        }

        return commonName.toString();
    }

    /**
     * generate user's DistinguishedName object.
     *
     * @return DigitalSigningDistinguishedName DigitalSigningDistinguishedName
     */
    DigitalSigningDistinguishedName generateUserDistinguishedName() {
        DigitalSigningDistinguishedName userDn = new DigitalSigningDistinguishedName();

        userDn.setCommonName(this.constructCommonName());
        userDn.setCountryName(this.getCountryCode());
        userDn.setEmailAddress(this.getEmailAddress());
        userDn.setLocalityName(this.getCity());
        userDn.setOrganizationalUnitName(this.getOrganizationUnit());
        userDn.setOrganizationName(this.getOrganization());
        userDn.setStateOrProvinceName(this.getStateProvince());

        return userDn;
    }
    /**
     * @return the serialNumber
     */
    public long getSerialNumber() {
        return serialNumber;
    }
    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }
    /**
     * @return the validDays
     */
    public int getValidDays() {
        return validDays;
    }
    /**
     * @param validDays the validDays to set
     */
    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }
    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }
    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
