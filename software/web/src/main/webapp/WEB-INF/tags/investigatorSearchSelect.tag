<%@ tag body-content="scriptless" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<script type="text/javascript" src="<s:url value="/scripts/search_support.js"/>"></script>

<div class="formcol">
    <s:form id="investigatorSelectionForm" action="nextStep" onsubmit="return false">
        <firebird:instructionBubble messageKey="user.registration.investigator.selection.search.label"/>
        <input id="investigatorSearchInput" class="autosearch"/>
        <div class="search_icon"><img src="<c:url value='/images/ico_search.gif'/>" alt="Search" /></div>
        <div class="loading_icon hide"><img src="<c:url value='/images/loading.gif'/>" alt="Searching" /></div>
    </s:form>
</div>
<div class="dotted_line2 blank_space"></div>
<div class="clear blank_space">
<firebird:label messageKey="label.search.results" />
</div>
<div class="formcol_xxwide">
    <table id="searchedInvestigators" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table shows the search results for queried investigators. It displays a button to select the
                    result, the investigator's name, and their email.">
        <thead>
            <tr>
                <th scope="col"><div class="btn_select"></div></th>
                <th scope="col"><fmt:message key="profile.person.displayName"/></th>
                <th scope="col"><fmt:message key="profile.email"/></th>
            </tr>
        </thead>
    </table>
</div>
<div class="formcol_right">
    <fieldset>
        <legend><fmt:message key="user.registration.investigator.selection.selected.investigators.section"/><fmt:message key="label.separator"/> </legend>
        <ul id="selectedInvestigators">
            <li id="noneSelected"><fmt:message key="registration.coordinator.add.investigators.none.selected"/></li>
        </ul>
    </fieldset>
</div>
<div class="clear blank_space"></div>

<s:url var="searchUrl" action="investigator-search" namespace="/search"/>
<script type="text/javascript">
var _investigatorSearch = new SearchSupport(
        'investigatorSearchInput',
        'searchedInvestigators',
        '<s:property value="#searchUrl"/>');

var _investigatorSelectionPage = (function() {
    var investigatorSelectionPage = {};

    investigatorSelectionPage.selectedInvestigators = [];

    investigatorSelectionPage.addInvestigatorToList = function(id, investigatorName, isRemoveable) {
        $('#noneSelected').remove();
        this.addToAlreadySelectedList(id);
        var list = $('#selectedInvestigators')
        var li = $("<li></li>").attr('id', "profile_" + id);
        var investigatorNameSpan = $("<span />").addClass("float").append(investigatorName);
        var div = $("<div />").css("width","22em").append(investigatorNameSpan).addClass("clear");
        if(isRemoveable !== false) {
            var linkSpan = $("<span />").css("float","right");
            var link = $("<a />").attr("href","javascript:void(0)").attr("id", "delete_" + id);
            link.click(function() {
                _investigatorSelectionPage.removeFromList(this);
            });
            var img = $(createImage("ico_delete.gif", "Delete " + investigatorName));

            $(link).append(img);
            $(linkSpan).append(link);
            $(div).append(linkSpan);
        }
        $(li).append(div);
        $(list).append(li);
    }

    investigatorSelectionPage.addToAlreadySelectedList = function(profileId){
        _investigatorSelectionPage.selectedInvestigators.push(profileId);
    }

    investigatorSelectionPage.removeFromList = function(item) {
        var itemId = $(item).attr('id');
        var name = $(item).parent().parent().text();
        var profileId = itemId.slice(itemId.indexOf('_') + 1);
        switchToLoading(item);
        var isSuccessful = this.saveRemoval(profileId);
        if (isSuccessful) {
            this.selectedInvestigators.splice(_.indexOf(this.selectedInvestigators, profileId), 1);
            setPageSuccessMessage(name + ' <fmt:message key="user.registration.investigator.selection.person.removed"/>');
            changeCheckToButton(profileId);
            $('#profile_' + profileId).remove();
        } else {
            switchFromLoading(item);
            setPageErrorMessages('<fmt:message key="user.registration.investigator.selection.problem.removing"/> ' + name);
        };
    }

    function changeCheckToButton(profileId) {
        if($('#added_' + profileId).length > 0) {
            var addedCheck = $('#added_' + profileId);
            var parent = $(addedCheck).parent();
            $(addedCheck).remove();
            $(parent).append($(parent).data('link'));
        }
    }

    investigatorSelectionPage.saveRemoval = function(profileId){
        //Stub, Page should implement this to make sure the correct functionality occurs.
        //Function should return a boolean value indicating the success or failure of the action.
        alert("ERROR: saveRemoval() not implemented for this page");
    }

    investigatorSelectionPage.loadingIcon =  $('<div class="btn_div">' + createImage('loading.gif','Loading') + '</div>');

    function switchToLoading(item) {
        _fbUtil.switchObjects(item, investigatorSelectionPage.loadingIcon);
    }

    function switchFromLoading(item) {
        _fbUtil.switchObjects(investigatorSelectionPage.loadingIcon, item);
    }

    investigatorSelectionPage.createAddButtonColumn = function (profileId) {
        if(_.indexOf(this.selectedInvestigators, profileId) >= 0) {
            return this.getCheckMark(profileId);
        } else {
            return this.createAddButton(profileId);
        }
    }

    investigatorSelectionPage.getCheckMark = function(profileId) {
        return '<div class="btn_div" id="added_' + profileId + '">' + createImage('ico_checkmark.png','Added') + '</div>';
    }

    investigatorSelectionPage.createAddButton = function(profileId) {
        return '<a id="add_' + profileId + '" class="button" href="javascript:void(0)"><fmt:message key="button.select"/></a>';
    }

    investigatorSelectionPage.createPersonDisplayColumn = function(person) {
        var personString = person.displayNameForList + "<br>";
        personString += __addressFormatter(person.postalAddress);
        return personString;
    };

    investigatorSelectionPage.completeRow = function(row, profileId, person) {
        $(row).attr("profileId", profileId);
        var parent = $(row).find("td").first();
        var addButtonLink = this.getOrCreateAddButton(row, profileId);

        $(addButtonLink).click(function() {
            _investigatorSelectionPage.clickAddButton(this, profileId, person);
        });
        $(parent).data("link", addButtonLink);
        return row;
    }

    investigatorSelectionPage.getOrCreateAddButton = function(row, profileId) {
        var addButtonLink = $(row).find("a");
        if($(addButtonLink).length === 0) {
            addButtonLink = $(this.createAddButton(profileId));
        }
        return addButtonLink;
    }

    investigatorSelectionPage.clickAddButton = function(addButton, profileId, person) {
        switchToLoading(addButton);
        var isSuccessful = this.saveAddition(profileId);
        if (isSuccessful) {
            _investigatorSelectionPage.addInvestigatorToList(profileId, person.displayName);
            switchFromLoading(_investigatorSelectionPage.getCheckMark(profileId));
            setPageSuccessMessage(person.displayName + ' <fmt:message key="user.registration.investigator.selection.person.added"/>');
            $('#investigatorSearchInput').val('');
        } else {
            switchFromLoading(addButton);
            setPageErrorMessages('<fmt:message key="user.registration.investigator.selection.problem.adding"/> ' + person.displayName);
        };
    }

    investigatorSelectionPage.saveAddition = function(profileId){
        //Stub, Page should implement this to make sure the correct functionality occurs.
        //Function should return a boolean value indicating the success or failure of the action.
        alert("ERROR: saveAddition() not implemented for this page");
    }

    return investigatorSelectionPage;
})();

$(document).ready(function() {
    $('#investigatorSearchInput').keyup(function() {
        _investigatorSearch.searchKeyEvent();
    });

    $('#searchedInvestigators').dataTable( {
        aaData : [],
        bLengthChange: false,
        iDisplayLength : 5,
        bFilter: false,
        oLanguage: {
            sInfoEmpty : '<fmt:message key="no.matching.search.results"/>',
            sEmptyTable : ''
        },
        aoColumns:
        [
            {mDataProp: null, bSortable: false, sWidth : "195px", fnRender : function(obj) {
                return _investigatorSelectionPage.createAddButtonColumn(obj.aData.key);
            }},
            {mDataProp: null, fnRender : function(obj) {
                return _investigatorSelectionPage.createPersonDisplayColumn(obj.aData.person);
            }},
            {mDataProp: 'person.email'}
        ],
       fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
           return _investigatorSelectionPage.completeRow(nRow, aData.key, aData.person);
       },
       fnDrawCallback: function() {
           _investigatorSearch.finishTable();
       }
    } );
});


</script>