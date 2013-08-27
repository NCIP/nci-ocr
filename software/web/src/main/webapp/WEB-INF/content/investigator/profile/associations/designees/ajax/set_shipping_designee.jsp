<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script type="text/javascript">
    function getSearchAgainFunction(sectionName) {
        return function() {
            getSectionFunction(sectionName, function(index) {
                if (index === 0) {
                    $(this).show();
                } else {
                    $(this).hide();
                    $("input:not('[name=\"profile.id\"]')", $(this)).val("");
                    $("form", $(this)).children().first().unwrap();
                }
                setFocusToDialog();
            });
        }
    }

    function getSectionFunction(sectionName, iterationFunc) {
        $("#" + sectionName + " > div").each(iterationFunc);
    }

    function getCreateNewFunction(sectionName) {
        return function() {
            getSectionFunction(sectionName, function(index) {
                $("input:not('[name=\"createNewPerson\"],[name=\"createNewOrganization\"],[name=\"profile.id\"]')", $(this)).val("");
                if (index === 2) {
                    $(this).wrapInner("<form/>").show();
                } else {
                    $(this).hide();
                    $("form", $(this)).children().first().unwrap();
                }
                setFocusToDialog();
            });
        }
    }

</script>
<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="person.association.set.shipping.designee.title" />:
    </firebird:dialogHeader>
    <firebird:messages />
    <div id="shippingDesigneeAccordion" style="max-height: 650px">
        <h3><a href="#"><fmt:message key="person.association.shipping.designee.person.section.header"/></a></h3>
        <div id="selectPerson">
            <div id="searchPerson" class="<s:property value='%{shippingDesignee.person != null ? \"hide\" : \"\"}'/>">
                <s:fielderror>
                    <s:param>personSearch</s:param>
                </s:fielderror>
                <firebird:personSearch labelKey="person.search.label" >
                  <s:a id="createNewPerson" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
                </firebird:personSearch>
            </div>
            <div id="selectedPerson" class="<s:property value='%{shippingDesignee.person == null || shippingDesignee.person.nesId == null ? \"hide\" : \"\"}'/>">
                <s:hidden id="selectedPersonKey" name="selectedPersonKey" />
                <firebird:searchAgainLink/>
                <firebird:viewPerson person="${shippingDesignee.person}" tagVariableName="designeePerson"/>
            </div>
            <div id="newPerson" class="<s:property value='%{shippingDesignee.person == null || shippingDesignee.person.nesId != null ? \"hide\" : \"\"}'/>">
                <firebird:searchAgainLink/>
                <firebird:managePersonFormFields beanPrefix="shippingDesignee.person"/>
                <s:hidden name="createNewPerson" value="true"/>
            </div>
            <script>
            $(document).ready(function() {
                personSearch.clickSelectButton = function(personId, rowData) {
                $('#selectedPersonKey').val(personId);
                $("#selectPerson > div").each(function(index) {
                    if (index === 1) {
                      $(this).wrapInner("<form/>").show();
                      $(this).data("selection", rowData.person);
                      designeePerson.setPerson(rowData.person);
                    } else {
                      $(this).hide();
                    }
                    });
                    setFocusToDialog();
                }
                $("#selectPerson .searchAgainLink").click(getSearchAgainFunction("selectPerson"));
                $("#searchPerson #createNewPerson").click(getCreateNewFunction("selectPerson"));
            });
            </script>
            </div>
        <h3><a href="#"><fmt:message key="person.association.shipping.designee.organization.section.header"/></a></h3>
        <div id="selectOrganization" >
            <div id="searchOrganization" class="<s:property value='%{shippingDesignee.organization != null ? \"hide\" : \"\"}'/>">
                <s:fielderror>
                    <s:param>organizationSearch</s:param>
                </s:fielderror>
                <firebird:organizationSearch labelKey="organization.search.label">
                    <s:a id="createNewOrganization" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
                </firebird:organizationSearch>
            </div>
            <div id="selectedOrganization" class="<s:property value='%{shippingDesignee.organization == null || shippingDesignee.organization.nesId == null ? \"hide\" : \"\"}'/>">
                <s:hidden id="selectedOrganizationKey" name="selectedOrganizationKey" />
                <firebird:searchAgainLink/>
                <firebird:organizationDisplay organization="${shippingDesignee.organization}" tagVariableName="organizationDisplay"/>
            </div>
            <div id="newOrganization" class="<s:property value='%{shippingDesignee.organization == null || shippingDesignee.organization.nesId != null ? \"hide\" : \"\"}'/>">
                <firebird:searchAgainLink/>
                <div class="formcol">
                   <s:textfield id="shippingDesignee.organization.name" name="shippingDesignee.organization.name" maxlength="160" size="30" requiredLabel="true"
                       cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}" />
                </div>
                <div class="formcol clear">
                   <s:textfield id="shippingDesignee.organization.email" name="shippingDesignee.organization.email" maxlength="50" size="30" cssStyle="width: 19em;"
                       label="%{getText('textfield.emailAddress')}" requiredLabel="true" />
                </div>

                <div class="formcol">
                    <firebird:phoneNumber beanPrefix="shippingDesignee.organization" varPrefix="shippingDesignee.organization"/>
                </div>
                <div class="dotted_line"><br></div>
                <firebird:addressFields beanPrefix="shippingDesignee.organization.postalAddress" varPrefix="shippingDesignee.organization"/>
                <s:hidden name="createNewOrganization" value="true"/>
            </div>
            <script>
            $(document).ready(function() {
                organizationSearch.clickSelectButton = (function() {
                    return function(organizationId, rowData) {
                        $('#selectedOrganizationKey').val(organizationId);
                        $("#selectOrganization > div").each(function(index) {
                            if (index === 1) {
                                $(this).wrapInner("<form/>").show();
                                $(this).data("selection", rowData.organization); //The selection
                                organizationDisplay.setOrganization(rowData.organization);
                            } else {
                                $(this).hide();
                            }
                            setFocusToDialog();
                        });
                    }
                })();
                $("#selectOrganization .searchAgainLink").click(getSearchAgainFunction("selectOrganization"));
                $("#searchOrganization #createNewOrganization").click(getCreateNewFunction("selectOrganization"));
            });
            </script>
        </div>
        <h3><a href="#"><fmt:message key="person.association.shipping.designee.shipping.address.section.header"/></a></h3>
        <div>
            <form>
                <firebird:addressFields beanPrefix="shippingDesignee.shippingAddress" varPrefix="shippingDesignee.shippingAddress"/>
            </form>
        </div>
    </div>
    <div class="btn_bar clear">
        <s:a id="saveButton" cssClass="button" href="#" onclick="saveShippingDesignee();"><fmt:message key="button.save"/></s:a>
        <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
    </div>

    <s:url var="setShippingDesigneeUrl" action="setShippingDesignee" />
    <script type="text/javascript">

    var errorsInSectionPrefixText = '<s:property value="%{getText('accordion.form.errors.in.section')}"/>';

    $(document).ready(function() {
      $("#shippingDesigneeAccordion").accordion({ heightStyle: "content" });
      wrapDataInFormsIfNecessary();
      addErrorMessagesIfNecessary();
      $("input").keydown(testForEnter);
    });

    function wrapDataInFormsIfNecessary() {
      $("#shippingDesigneeAccordion > div > div:not(.hide)").each(function() {
          $(this).wrapInner("<form/>");
      });
    }

    function addErrorMessagesIfNecessary() {
      var messages = [];
        $("#shippingDesigneeAccordion").children("h3,div").each((function() {
          var sectionHeader = "";
          return function(index) {
            if ($(this).is("h3")) {
              sectionHeader = $(this).text();
            } else {
              if ($(".wwerr", $(this)).length > 0) {
                messages.push(errorsInSectionPrefixText + " " + sectionHeader);
              }
            }
          };
        })());
        setPageErrorMessages(messages);
    }

    function saveShippingDesignee() {
        var url = '<s:property value="#setShippingDesigneeUrl"/>'
        var target = getCurrentDialog();

        var formData = "";
        $("#shippingDesigneeAccordion").children("div").each(function() {
          if (formData) {
            formData += "&"
          }
          formData += $("form", this).serialize();
        });
        _fbUtil.performAjaxPost(url, target, formData);
    }
    </script>
</firebird:dialogLayout>
