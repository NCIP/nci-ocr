<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.treeTable.js"/>"></script>
<link href="<c:url value='/styles/jquery.treeTable.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/styles/jquery.treetable.theme.default.css'/>" rel="stylesheet" type="text/css" />

<s:url var="registrationIconUrl" value='/images/ico_document.png' />
<s:url var="regUrl" action="enterRegistrations" namespace="/investigator/registration">
    <s:param name="profile.id" value="profile.id" />
</s:url>
<h1>
    <fmt:message key="registration.browse.title"/><fmt:message key="label.separator"/> <span class="gray">${profile.person.displayName}</span>
</h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">

            <div class="registrationsTableHeader">
                <div><fmt:message key="registration.browse.protocol.title.column.header" /></div>
                <div><fmt:message key="registration.browse.protocol.id.column.header" /></div>
                <div><fmt:message key="registration.browse.protocol.sponsor.column.header" /></div>
                <div><fmt:message key="label.type" /></div>
                <div><fmt:message key="registration.browse.protocol.status.column.header" /></div>
            </div>

            <div id="registrationsAccordionTable">
                <s:if test="registrationListings.empty">
                    <br>
                    <div class="centeredText"><fmt:message key="label.no.registrations"/></div>
                </s:if> <s:else>
                    <s:iterator value="registrationListings" var="registration">
                        <div class="accordion">
                            <div id="<s:property value='%{#registration.id}'/>" class="accordionHeader">
                                <s:if test="#registration.revisedRegistrations.empty">
                                    <span style="margin-right: 17px;">&nbsp</span>
                                </s:if><s:else>
                                    <span class="accordionToggle ui-icon ui-icon-triangle-1-e"></span>
                                </s:else>
                                <img alt="Registration" src="${registrationIconUrl}"/>
                                <div><s:property value='%{#registration.title}'/></div>
                                <div><s:property value='%{#registration.protocolNumber}'/></div>
                                <div><s:property value='%{#registration.sponsor}'/></div>
                                <div><s:property value='%{#registration.type}'/></div>
                                <div>
                                    <span class="statusBullet statusBullet_inv_<s:property value='%{#registration.status.name()}'/>">&bull;</span>
                                    <span id="registrationStatus"><s:property value='%{#registration.status.display}'/></span>
                                </div>
                                <s:if test="#registration.submittable">
                                    <div><s:a cssClass="button" href="#" onclick="viewRegistration(%{#registration.id})"><fmt:message key="button.edit"/></s:a></div>
                                </s:if><s:else>
                                    <div><s:a cssClass="button" href="#" onclick="viewRegistration(%{#registration.id})"><fmt:message key="button.view"/></s:a></div>
                                </s:else>
                            </div>

                            <!-- Collapsed Revised Registrations -->
                            <div id="revisedRegistrations" class="hide">
                               <s:iterator value="#registration.revisedRegistrations" var="revisedRegistration">
                                   <div id="<s:property value='%{#revisedRegistration.id}'/>" class="accordionHeader">
                                        <span style="margin-right: 17px;">&nbsp</span>
                                        <img alt="Registration" src="${registrationIconUrl}"/>
                                        <div><s:property value='%{#revisedRegistration.title}'/></div>
                                        <div><s:property value='%{#revisedRegistration.protocolNumber}'/></div>
                                        <div><s:property value='%{#revisedRegistration.sponsor}'/></div>
                                        <div><s:property value='%{#revisedRegistration.type}'/></div>
                                        <div>
                                            <span class="statusBullet statusBullet_inv_<s:property value='%{#revisedRegistration.status.name()}'/>">&bull;</span>
                                            <span id="registrationStatus"><s:property value='%{#revisedRegistration.status.display}'/></span>
                                        </div>
                                        <div><s:a cssClass="button" href="#" onclick="viewRegistration(%{#revisedRegistration.id})"><fmt:message key="button.view"/></s:a></div>
                                    </div>
                               </s:iterator>
                            </div>

                        </div>
                    </s:iterator>
                </s:else>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        setUpAccordion();
    });

    function setUpAccordion() {
        $('.accordionToggle').click(function () {
            toggleAccordionArrow(this);
            $(this).parent().next().slideToggle('fast');
        }).parent().next().hide();
    }

    function toggleAccordionArrow(arrow) {
        $(arrow).toggleClass("ui-icon-triangle-1-s");
        $(arrow).toggleClass("ui-icon-triangle-1-e");
    }

    function viewRegistration(id) {
        var url = "${regUrl}" + "&registration.id=" + id;
        window.location.href = url;
    }

</script>