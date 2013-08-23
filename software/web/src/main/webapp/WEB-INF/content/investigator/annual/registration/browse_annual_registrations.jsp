<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="viewRegistrationUrl" action="enterViewRegistration" namespace="/investigator/annual/registration">
    <s:param name="profile.id" value="profile.id" />
</s:url>
<s:url namespace="/investigator/annual/registration/ajax/reactivate" action="reactivateInvestigator" var="reactivateUrl"/>
<s:url var="registrationIconUrl" value='/images/ico_document.png' />
<h1>
    <fmt:message key="annual.registration.browse.title"/><fmt:message key="label.separator"/> <span class="gray">${profile.person.displayName}</span>
</h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:instructionBubble messageKey="annual.registration.browse.instructions" />

            <div class="annualRegistrationsTableHeader">
                <div><fmt:message key="label.type" /></div>
                <div><fmt:message key="label.status" /></div>
                <div><fmt:message key="label.submission.date" /></div>
                <div><fmt:message key="label.due.date" /></div>
            </div>

            <div id="annualRegistrationsAccordionTable">
                <s:if test="annualRegistrationListings.empty">
                    <br>
                    <div class="centeredText"><fmt:message key="label.no.registrations"/></div>
                </s:if> <s:else>
                    <s:iterator value="annualRegistrationListings" var="registration">
                        <div class="accordion">
                            <div id="<s:property value='%{#registration.id}'/>" class="accordionHeader">
                                <span class="accordionToggle ui-icon ui-icon-triangle-1-e"></span>
                                <img alt="Registration" src="${registrationIconUrl}"/>
                                <div><s:property value='%{#registration.type}'/></div>
                                <div>
                                    <span class="statusBullet statusBullet_inv_<s:property value='%{#registration.status.name()}'/>">&bull;</span>
                                    <span id="registrationStatus"><s:property value='%{#registration.status.display}'/></span>
                                </div>
                                <div class="date"><s:date name="submissionDate" format="MM/dd/yyyy" /></div>
                                <div class="date"><s:date name="dueDate" format="MM/dd/yyyy" /></div>
                                <s:if test="%{!readOnly && #registration.submittable}">
                                    <div><s:a cssClass="button" href="#" onclick="viewRegistration(%{#registration.id})"><fmt:message key="button.edit"/></s:a></div>
                                </s:if><s:else>
                                    <div><s:a cssClass="button" href="#" onclick="viewRegistration(%{#registration.id})"><fmt:message key="button.view"/></s:a></div>
                                </s:else>
                            </div>
                            <table id="annualRegistrationsTable_<s:property value='%{#registration.id}'/>"
                                   class="investigatorAnnualRegistrationsTable ui-jqgrid-htable ui-jqgrid-btable"
                                   summary="This table shows the forms that are part of the annual registration. It displays
                                            the form name, optionality, the date of the last update, and the form status.">
                                <thead>
                                    <tr>
                                        <th scope="col" width="300px"><div><fmt:message key="label.form" /></div></th>
                                        <th scope="col" width="180px"><div><fmt:message key="label.optionality" /></div></th>
                                        <th scope="col" width="180px"><div><fmt:message key="label.last.update" /></div></th>
                                        <th scope="col" width="180px"><div><fmt:message key="label.status" /></div></th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </s:iterator>
                </s:else>
            </div>

            <div class="clear"></div>
            <br/>
            <s:if test="%{withdrawButtonVisible}">
                    <sj:dialog id="withdrawDialog" autoOpen="false" modal="true" width="950" position="top"
                               onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened"/>
                    <s:url namespace="/investigator/annual/registration/ajax/withdraw" action="enterWithdraw"
                           var="withdrawUrl"/>
                    <sj:a openDialog="withdrawDialog" href="%{#withdrawUrl}" cssClass="button"
                          id="withdrawSubmission">
                        <fmt:message key="button.withdraw"/>
                    </sj:a>
            </s:if><s:elseif test="%{reactivateButtonVisible}">
                <s:a href="javascript:void(0)" cssClass="button" id="reactivateButton" onclick="reactivateInvestigator();">
                        <fmt:message key="button.reactivate"/>
               </s:a>
            </s:elseif>

            <s:if test="createRegistrationAllowed">
                <s:a id="createAnnualRegistration" action="createAnnualRegistration" cssClass="blueButton button float_right" >
                    <s:param name="profile.id" value="profile.id" />
                    <fmt:message key="button.create.registration"/>
                </s:a>
            </s:if>

        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        convertRegistrationTableDates();
        createRegistrationTables();
        setUpAccordion();
    });

    function convertRegistrationTableDates() {
        $(".date").each(function () {
            var date = $(this).text();
            if (isBlank(date)) {
                $(this).text("N/A");
            }
        });
    }

    function createRegistrationTables() {
        var registrations = ${gridTableData};
        $.each(registrations, function (index, registration) {
            buildRegistrationTable(registration);
        });
    }

    function buildRegistrationTable(registration) {
        $('#annualRegistrationsTable_' + registration.id).dataTable({
            aaData: registration.annualRegistrationFormListings,
            bInfo: false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            bSort: false,
            aoColumns: [
                {mDataProp: null, fnRender: function (obj) {
                    return buildFormTitleColumn(obj.aData);
                }},
                {mDataProp: "optionality.display"},
                {mDataProp: "lastUpdateDate", stype: "date",
                    fnRender: function (obj) {
                        return formatDate(obj.aData.lastUpdateDate, true);
                    }},
                {mDataProp: "status.display"}
            ],
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function () {
                indicateLoading(false);
            }
        });
    }

    function buildFormTitleColumn(form) {
        return "<a id='viewForm." + form.id + "' href='${viewRegistrationUrl}&registration.id=" + form.registrationId +
                "&form=" + form.name.toLowerCase().replace(/ /g, '_') + "'>" + form.title + "</a>"
    }

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
        var url = "${viewRegistrationUrl}" + "&registration.id=" + id;
        window.location.href = url;
    }

    function reactivateInvestigator() {
        var url = "${reactivateUrl}";
        $.post(url, null, function () {
            refreshPage(0);
        });
    }
</script>