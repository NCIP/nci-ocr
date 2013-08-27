<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
<head>
    <title><fmt:message key="request.account.title" /></title>
</head>
<body onload="setFocusToFirstControl();">
    <s:set var="sponsorRole" value="@gov.nih.nci.firebird.data.user.UserRoleType@SPONSOR.display"/>
    <s:set var="sponsorDelegateRole" value="@gov.nih.nci.firebird.data.user.UserRoleType@SPONSOR_DELEGATE.display"/>


    <h2><fmt:message key="request.account.title"/></h2>
    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <firebird:messages/>
                <firebird:instructionBubble messageKey="request.account.instructions" />

                <script>
                    var _requestAccountPage = (function(){
                        _page = {};

                        var loginInputDiv;

                        var sponsorRole = '<s:property value="#sponsorRole"/>';
                        var sponsorDelegateRole = '<s:property value="#sponsorDelegateRole"/>';


                        _page.createRadioClickEvent = function(id, currentValue) {
                            var func = getRadioClickEvent(id);

                            if (currentValue !== undefined) {
                                func.selectedValue = currentValue;
                            }

                            return func;
                        };

                        var getRadioClickEvent = _.memoize(function(id) {
                            var func = function(event) {
                                if ($(event.target).val() === func.selectedValue) {
                                    $(event.target).prop("checked", false);
                                    func.selectedValue = undefined;
                                } else {
                                    func.selectedValue = $(event.target).val();
                                }
                            };
                            return func;
                        });

                        /*
                            Options:
                                idPrefix - String in differentiating radio button elements.
                                id - the identifying piece of data to use.
                                checked - whether the button should default to checked.
                                value - the value of the radio button.
                        */
                        _page.createDeSelectableRadioButton = function(options) {
                            with(options) {
                                var selectedValue = checked ? value : undefined;
                                var onClick = _page.createRadioClickEvent(id, selectedValue);
                                var itemId = idPrefix + id;
                                createNamedElement("input type='radio'", id).attr({
                                    checked : checked,
                                    value : value,
                                    id : itemId
                                }).click(onClick).insertAfter("label[for='"+ itemId +"']");
                            }
                        };

                        _page.toggleLoginInput = function(showLoginInput) {
                            if (showLoginInput && loginInputDiv != null) {
                                loginInputDiv.appendTo("#loginInfoDiv");
                                loginInputDiv = null;
                            } else if (loginInputDiv == null) {
                                loginInputDiv = $(".ldapLoginInput").detach();
                            }
                        }

                        _page.requestAccount = function() {
                            addSelectedSponsorIdsToForm(sponsorRole);
                            addSelectedSponsorIdsToForm(sponsorDelegateRole);
                            $("#requestAccountForm").submit();
                        }

                        function addSelectedSponsorIdsToForm(role) {
                            if (role === sponsorDelegateRole) {
                                var name = "selectedSponsorDelegateIds";
                            } else {
                                var name = "selectedSponsorIds";
                            }
                            $(".sponsorRoleSelect input:checked").each(function() {
                                if (this.value === role) {
                                    createNamedElement("input type='hidden'", name).val(this.name).appendTo("#requestAccountForm");
                                }
                            });
                        }

                        return _page;
                    })();

                    $(document).ready(function () {
                        _requestAccountPage.toggleLoginInput();
                        if (${newAccount.existingLdapAccount}) {
                            $("#existingLdapAccountSelect_yes").prop("checked", true);
                            _requestAccountPage.toggleLoginInput(true);
                        }
                    });

                </script>

                <s:form namespace="/user" action="requestAccount" id="requestAccountForm">

                    <h2><fmt:message key="label.login.information"/></h2>
                    <div id="loginInfoDiv">
                        <div id="yesNoRadioButtons" class="yesNoRadioButtons">
                            <span class="float"><firebird:label messageKey="login.ldap.login.question" showSeparator="false" /></span>
                            <span class="noButton float_right">
                                <input type="radio" id="existingLdapAccountSelect_no" name="newAccount.existingLdapAccount" onclick="_requestAccountPage.toggleLoginInput(false);" checked="checked"  value="false" />
                                <firebird:label forId="existingLdapAccountSelect_no" messageKey="label.no" showSeparator="false" />
                            </span>
                            <br>
                            <span class="yesButton float_right">
                                <input type="radio" id="existingLdapAccountSelect_yes" name="newAccount.existingLdapAccount" onclick="_requestAccountPage.toggleLoginInput(true);" value="true" />
                                <firebird:label forId="existingLdapAccountSelect_yes" messageKey="label.yes" showSeparator="false" />
                            </span>
                        </div>
                        <%-- <div id="yesNoRadioButtons" class="yesNoRadioButtons">
                            <span><firebird:label forId="yesNoRadioButtonsTest" messageKey="login.ldap.login.question" showSeparator="false" /></span>
                            <span class="noButton float_right">
                                <input type="radio" id="existingLdapAccountSelect_no" name="newAccount.existingLdapAccount" onclick="_requestAccountPage.toggleLoginInput(false);" checked="checked"  value="false" />
                                <firebird:label forId="existingLdapAccountSelect_no" messageKey="label.no" showSeparator="false" />
                            </span>
                            <br>
                            <span class="yesButton float_right">
                                <input type="radio" id="existingLdapAccountSelect_yes" name="newAccount.existingLdapAccount" onclick="_requestAccountPage.toggleLoginInput(true);" value="true" />
                                <firebird:label forId="existingLdapAccountSelect_yes" messageKey="label.yes" showSeparator="false" />
                            </span>
                        </div> --%>
                        <div class="clear"></div>

                        <div class="ldapLoginInput clear">
                            <br>
                            <s:textfield id="username" name="newAccount.username" requiredLabel="true" label="%{getText('textfield.username')}" labelposition="left" />
                            <s:password id="password" name="newAccount.password" requiredLabel="true" label="%{getText('textfield.password')}" labelposition="left" />
                            <s:select label="%{getText('login.idp')}" labelposition="left" id="identityProvider" name="newAccount.identityProviderUrl" list="%{identityProviders}" listValue="displayName" listKey="authenticationServiceURL" />
                        </div>
                    </div>
                    <br/>
                    <h2><fmt:message key="label.role.selection"/></h2>
                    <div class="roleSelection">
                        <firebird:instructionBubble messageKey="request.account.role.instructions" />
                        <s:checkboxlist id="selectedRoles" name="newAccount.roles" list="availableNonSponsorRoles" listValue="display" template="checkboxlistVertical"/>

                        <s:iterator value="sponsorOrganizations" var="sponsor">
                            <div class="boldHeader"><s:property value="name"/></div>
                            <div class="sponsorRoleSelect">

                                <firebird:label forId="sponsor_${nesId}" messageKey="sponsor.representative.role.text"/>
                                <firebird:label forId="sponsorDelegate_${nesId}" messageKey="sponsor.delegate.role.text"/>

                                <script>
                                _requestAccountPage.createDeSelectableRadioButton({
                                    idPrefix : 'sponsor_',
                                    id : "<s:property value='nesId'/>",
                                    checked : <s:property value="%{#sponsor.nesId in selectedSponsorIds}"/>,
                                    value : '<s:property value="sponsorRole"/>'
                                });
                                _requestAccountPage.createDeSelectableRadioButton({
                                    idPrefix : 'sponsorDelegate_',
                                    id : "<s:property value='nesId'/>",
                                    checked : <s:property value="%{#sponsor.nesId in selectedSponsorDelegateIds}"/>,
                                    value : '<s:property value="sponsorDelegateRole"/>'
                                });
                                </script>
                            </div>
                            <br/>
                        </s:iterator>
                    </div>

                    <h2><fmt:message key="label.account.details"/></h2>

                    <div class="formcol">
                        <s:textfield id="newAccount.personFirstName" name="newAccount.person.firstName" maxlength="50" size="30"
                        cssStyle="width: 19em;" label="%{getText('account.firstName')}" requiredLabel="true"/>
                    </div>

                    <div class="formcol">
                        <s:textfield id="newAccount.personLastName" name="newAccount.person.lastName"
                            maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('account.lastName')}" requiredLabel="true"/>
                    </div>

                    <div class="dotted_line"><br></div>

                    <div class="formcol">
                        <s:textfield id="newAccount.personEmail" name="newAccount.person.email"
                            maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('account.emailAddress')}" requiredLabel="true"/>
                    </div>

                    <div class="formcol">
                        <firebird:phoneNumber beanPrefix="newAccount.person" required="true"/>
                    </div>

                    <div class="dotted_line"><br></div>

                    <div class="addressInput">
                        <s:textfield id="newAccount.primaryOrganization.organization.name"
                            name="newAccount.primaryOrganization.organization.name"
                            maxlength="50" size="50" cssStyle="width: 19em;"
                            label="%{getText('account.organization')}" labelposition="left"
                            requiredLabel="true"/>
                    </div>
                    <br>

                    <firebird:addressFields beanPrefix="newAccount.person.postalAddress"/>

                    <div class="btn_bar">
                        <a href="#" class="button" onclick="indicateLoading(); _requestAccountPage.requestAccount();"
                            id="requestAccountButton"><fmt:message key="button.request.account"/></a>
                    </div>
                </s:form>
            </div>
        </div>
    </div>
</body>
</html>