<%@ tag body-content="scriptless" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="labelKey"%>
<%@ attribute name="required" required="false" type="java.lang.Boolean"%>
<%@ attribute name="searchAction" required="false" rtexprvalue="true" %>
<%@ attribute name="setFocus" required="false" type="java.lang.Boolean" description="Whether or not to set focus to the search box on load"%>
<script type="text/javascript" src="<s:url value="/scripts/search_support.js"/>"></script>

<s:if test="%{#attr.searchAction not in #{null,''}}">
<s:url var="searchUrl" action="%{#attr.searchAction}" namespace="/search"/>
</s:if><s:else>
<s:url var="searchUrl" action="searchForOrganizations" namespace="/search"/>
</s:else>

<div class="formcol_org_search">
    <firebird:instructionBubble messageKey="organization.search.instructions"/>
    <s:if test="%{#attr.labelKey not in #{null,''}}">
        <firebird:label forId="organizationSearchInput" messageKey="${attr.labelKey}" required="${attr.required}" />
    </s:if>
    <input id="organizationSearchInput" class="autosearch"/>
    <div class="search_icon"><img src="<s:url value='/images/ico_search.gif'/>" alt="Search" /></div>
    <div class="loading_icon hide"><img src="<s:url value='/images/loading.gif'/>" alt="Searching" /></div>
    <div class="side_buttons"><jsp:doBody/></div>
</div>
<div class="dotted_line2 blank_space"></div>
<div class="clear blank_space">
    <firebird:label messageKey="label.search.results" />
</div>
<table id="organizationSearchResultsTable" class="ui-jqgrid-htable ui-jqgrid-btable" style="max-width: 100%"
       summary="This table shows the results from searching for an organization. It displays a button to select the organization
                the organization name, the mailing address, and the email.">
    <thead>
        <tr>
            <th scope="col" width="40px"><%--Select Button--%></th>
            <th scope="col" width="100px"><fmt:message key="profile.organization.name"/></th>
            <th scope="col" width="150px"><fmt:message key="profile.postalAddress"/></th>
            <th scope="col" width="100px"><fmt:message key="textfield.emailAddress"/></th>
        </tr>
    </thead>
</table>
<div class="clear blank_space">
</div>

<script type="text/javascript">
    var organizationSearch = new SearchSupport(
            'organizationSearchInput',
            'organizationSearchResultsTable',
            '<s:property value="#searchUrl"/>');

    $(document).ready(function () {
        $('#organizationSearchInput').keyup(function () {
            organizationSearch.searchKeyEvent();
        });

        if (!organizationSearch.isTableConfigured()) {
            $('#organizationSearchResultsTable').dataTable({
                aaData: [],
                iDisplayLength: 5,
                aLengthMenu: [
                    [5, 25, 100],
                    [5, 25, 100]
                ],
                bFilter: false,
                oLanguage: {
                    sInfoEmpty: '<fmt:message key="no.matching.search.results"/>',
                    sEmptyTable: ''
                },
                aoColumns: [
                    {mDataProp: null, sWidth: 75, fnRender: function (obj) {
                        return organizationSearch.createSelectButtonColumn(obj.aData.externalId, '<fmt:message key="button.select"/>');
                    }},
                    {mDataProp: "name"},
                    {mDataProp: null, fnRender: function (obj) {
                        return addressFormatter(obj.aData.postalAddress);
                    }},
                    {mDataProp: "email"},
                    {mDataProp: "postalAddress.streetAddress", bVisible: false},
                    {mDataProp: "postalAddress.deliveryAddress", bVisible: false},
                    {mDataProp: "postalAddress.city", bVisible: false},
                    {mDataProp: "postalAddress.stateOrProvince", bVisible: false},
                    {mDataProp: "postalAddress.postalCode", bVisible: false},
                    {mDataProp: "postalAddress.country", bVisible: false},
                    {mDataProp: "phoneNumber", bVisible: false},
                    {mDataProp: "ctepId", bVisible: false}
                ],
                fnRowCallback: function (nRow, organization) {
                    return organizationSearch.completeRow(nRow, organization);
                },
                fnDrawCallback: function () {
                    organizationSearch.finishTable();
                }
            });
        }

        if ("${setFocus}" === "true") {
            $('#organizationSearchInput').focus();
        }

    });

</script>