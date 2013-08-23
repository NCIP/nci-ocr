<%@ tag body-content="empty" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="selectedSponsorExternalIds" required="true" rtexprvalue="true" %>
<%@ attribute name="disabledSponsorIds" required="true" rtexprvalue="true" %>
<%@ attribute name="disabledSponsorKey" required="true"%>


<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <div class="hide" id="instruction_div">
                <firebird:instructionBubble messageKey="${disabledSponsorKey}" />
            </div>
            <s:form id="sponsorOrganizationSelectionForm" action="nextStep" onsubmit="return false">
                <table id="sponsorOrganizationsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                        summary="This table displays the sponsor organizations available for selection. It displays a
                                checkbox for selection, the name of the sponsor organization, the mailing address,
                                and the email.">
                    <thead>
                        <tr>
                            <th scope="col" width="33px"><%--Checkbox Column--%></th>
                            <th scope="col" width="150px"><div><fmt:message key="label.name"/></div></th>
                            <th scope="col" width="150px"><div><fmt:message key="label.address"/></div></th>
                            <th scope="col" width="150px"><div><fmt:message key="label.email"/></div></th>
                        </tr>
                    </thead>
                </table>
            </s:form>

            <div class="btn_bar clear">
                <firebird:userRegistrationPreviousButton/>
                <firebird:userRegistrationNextButton/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var _sponsorSelectionPage = (function(){
        var page = {};
        page.selectedSponsorExternalIds = ${selectedSponsorExternalIds} ? ${selectedSponsorExternalIds} : [];
        page.disabledSponsorIds = ${disabledSponsorIds} ? ${disabledSponsorIds} : [];
        page.createCheckColumn = function(organization) {
            var checkboxHtml = "<input type='checkbox' name='selectedSponsorExternalIds' ";
            checkboxHtml += "id='sponsor_"+ organization.externalId + "' ";
            checkboxHtml += "value='" + organization.externalId + "' ";

            if (_.indexOf(this.selectedSponsorExternalIds, organization.id) >= 0) {
                checkboxHtml += "checked ";
            }
            if (_.indexOf(this.disabledSponsorIds, organization.id) >= 0) {
                checkboxHtml += "disabled ";
            }
            checkboxHtml += "/>";
            return checkboxHtml;
        };

        if(_.size(page.disabledSponsorIds) > 0) {
            $('#instruction_div').show();
        }

        return page;
    })();

    var sponsors = ${sponsorOrganizationsJson};
    var isLongData = sponsors.length > ${minPaginationResults};
    $('#sponsorOrganizationsTable').dataTable( {
        "aaData" : sponsors,
        "bInfo" : isLongData,
        "bLengthChange": isLongData,
        "bPaginate": isLongData,
        "bFilter": isLongData,
        "aoColumns": [
            {"mDataProp" : null, "fnRender" : function(obj) {
                return _sponsorSelectionPage.createCheckColumn(obj.aData);
            }},
            {mDataProp: "name"},
            {mDataProp: "postalAddress", fnRender : function(obj) {
              return addressFormatter(obj.aData.postalAddress);
            }},
            {mDataProp: "email"}
        ],
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
           $(nRow).attr("id", aData.externalId);
           if (_.indexOf(_sponsorSelectionPage.disabledSponsorIds, aData.externalId) >= 0) {
               $(nRow).addClass("selectedSponsor disabledText")
           }
           return nRow;
        }
    });
</script>
