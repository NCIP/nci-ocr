<%@ tag body-content="scriptless" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="labelKey" %>
<%@ attribute name="instructionsKey" required="false" %>
<%@ attribute name="setFocus" required="false" type="java.lang.Boolean" description="Whether or not to set focus to the search box on load"%>
<script type="text/javascript" src="<s:url value="/scripts/search_support.js"/>"></script>

<s:set var="instructionsKey" value="%{#attr.instructionsKey == null ? 'person.search.instructions' : #attr.instructionsKey}" />
<s:url var="searchUrl" action="searchForPersons" namespace="/search"/>

<div class="formcol_org_search">
        <firebird:instructionBubble messageKey="${instructionsKey}"/>
        <s:if test="%{#attr.labelKey not in #{null,''}}">
            <label for="personSearchInput">
                <s:property escapeHtml="false" value="%{getText(#attr.labelKey)}"/><fmt:message key="label.separator"/>
            </label>
        </s:if>
        <input id="personSearchInput" class="autosearch"/>
        <div class="search_icon"><img src="<s:url value='/images/ico_search.gif'/>" alt="Search" /></div>
        <div class="loading_icon hide"><img src="<s:url value='/images/loading.gif'/>" alt="Searching" /></div>
    <div class="side_buttons"><jsp:doBody/></div>
</div>
<div class="dotted_line2 blank_space"></div>
<div class="clear blank_space">
  <firebird:label messageKey="label.search.results" />
</div>
<table id="personSearchResultsTable" class="ui-jqgrid-htable ui-jqgrid-btable" style="max-width: 100%"
       summary="This table shows the results from searching for an persons. It displays a button to select the person
                the person's name, the mailing address, and the email.">
    <thead>
        <tr>
            <th scope="col" width="40px" style="max-width: 40px"></th>
            <th scope="col" width="100px"><fmt:message key="profile.person.displayName"/></th>
            <th scope="col" width="150px"><fmt:message key="profile.postalAddress"/></th>
            <th scope="col" width="100px"><fmt:message key="textfield.emailAddress"/></th>
        </tr>
    </thead>
</table>
<div class="clear blank_space">
</div>

<script type="text/javascript">
    var personSearch = new SearchSupport(
            'personSearchInput',
            'personSearchResultsTable',
            '<s:property value="#searchUrl"/>');

    $(document).ready(function () {
        $('#personSearchInput').keyup(function () {
            personSearch.searchKeyEvent();
        });

        $('#personSearchResultsTable').dataTable({
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
                    return personSearch.createSelectButtonColumn(obj.aData.externalId, '<fmt:message key="button.select"/>');
                }},
                {mDataProp: "sortableName", bUseRendered: false, fnRender: function (obj) {
                    return obj.aData.displayNameForList;
                }},
                {mDataProp: null, fnRender: function (obj) {
                    return addressFormatter(obj.aData.postalAddress);
                }},
                {mDataProp: "email"}
            ],
            fnRowCallback: function (nRow, person) {
                return personSearch.completeRow(nRow, person);
            },
            fnDrawCallback: function () {
                personSearch.finishTable();
            }
        });

        var prepopulatedSearchString = '<s:property value="prepopulatedSearchString" default="" />';
        if (!isBlank(prepopulatedSearchString)) {
            $('#personSearchInput').val(prepopulatedSearchString);
            personSearch.autosearch();
        }

        if ("${setFocus}" === "true") {
            $('#personSearchInput').focus();
        }

    });

</script>