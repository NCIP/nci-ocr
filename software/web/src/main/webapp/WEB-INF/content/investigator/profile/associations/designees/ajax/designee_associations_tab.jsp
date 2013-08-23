<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeOrderingDesigneeConfirmUrl" action="enterDeleteOrderingDesigneeConfirm" namespace="/investigator/profile/associations/person/ajax" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="removeShippingDesigneeConfirmUrl" action="enterDeleteShippingDesigneeConfirm" namespace="/investigator/profile/associations/person/ajax" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="removeIconUrl" value='/images/ico_delete.gif' />
<s:url namespace="/investigator/profile/associations/subinvestigators/ajax" action="enterSearch" var="addPersonUrl" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="addOrderingDesigneeUrl" namespace="/investigator/profile/associations/designees/ajax" action="enterAddOrderingDesignee" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url namespace="/investigator/profile/associations/designees/ajax" action="enterSetShippingDesignee" var="setShippingDesigneeUrl" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<h2>
    <fmt:message key="label.shipping.designee"/>
</h2>
<firebird:instructionBubble messageKey="investigator.profile.associations.shipping.designee.help" hasIndicator="false"/>

<div class="clear"><br></div>

<s:if test="%{profile.shippingDesignee == null}">
  <sj:a id="changeShippingDesignee" openDialog="profileDialog" href="%{#setShippingDesigneeUrl}" cssClass="button">
      <fmt:message key="button.select"/>
  </sj:a>
</s:if>

<div class="clear"><br></div>

<table id="shippingDesignee" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays the information for the Shipping Designee you selected. It shows the person's name,
                email, mailing address, phone number, along with the organization name and a shipping address. You can also
                remove this entry via a link at the end.">
    <thead><tr>
        <th scope="col" width="250px"><div><fmt:message key="label.name" /></div></th>
        <th scope="col" width="75px"><div><fmt:message key="label.email" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.address" /></div></th>
        <th scope="col" width="75px"><div><fmt:message key="label.phone" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.organization" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.shipping.address" /></div></th>
        <th scope="col" width="75px"><div><fmt:message key="label.remove" /></div></th>
    </tr></thead>
</table>

<div class="clear"></div>
<h2>
    <fmt:message key="label.ordering.designees"/>
</h2>
<firebird:instructionBubble messageKey="investigator.profile.associations.ordering.designee.help" hasIndicator="false"/>
<div class="clear"><br></div>
<sj:a openDialog="profileDialog" href="%{addOrderingDesigneeUrl}" cssClass="button" id="addOrderingDesignee">
    <fmt:message key="button.addNew"/>
</sj:a>
<div class="clear"><br></div>

<table id="orderingDesignees" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table shows all of the Ordering Designees which have been added to your profile. It displays their
                name, email, mailing address, phone number, and a link which can be used to remove an entry.">
    <thead><tr>
        <th scope="col" width="250px"><div><fmt:message key="label.name" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.email" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.address" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.phone" /></div></th>
        <th scope="col" width="75px"><div><fmt:message key="label.remove" /></div></th>
    </tr></thead>
</table>

<script>

    $(document).ready(function() {
      var personNameColumn = {mDataProp: "person.sortableName", bUseRendered: false, fnRender: function (obj) {
          return obj.aData.person.displayNameForList;
      }};
      var personEmailColumn = {mDataProp: "person.email"};
      var personAddressColumn = {mDataProp: "person.postalAddress", fnRender : function(obj) {
          return addressFormatter(obj.aData.person.postalAddress);
      }};
      var personPhoneColumn = {mDataProp: "person.phoneNumber"};

      createOrderingDesigneesTable();
      createShippingDesigneeTable();

      function createOrderingDesigneesTable() {
        var dataRows = ${orderingDesigneesJson};
        buildTable("orderingDesignees", dataRows, [
              personNameColumn,
              personEmailColumn,
              personAddressColumn,
              personPhoneColumn,
              {mDataProp : null, fnRender : function(obj) {
                  return buildRemoveAssociationColumn(obj.aData, "${removeOrderingDesigneeConfirmUrl}");
              }}
          ]);
      }

      function createShippingDesigneeTable() {
          var dataRows = ${shippingDesigneeJson};
          buildTable("shippingDesignee", dataRows, [
              personNameColumn,
              personEmailColumn,
              personAddressColumn,
              personPhoneColumn,
              {mDataProp: "organization.name"},
              {mDataProp: "shippingAddress", fnRender : function(obj) {
                  return addressFormatter(obj.aData.shippingAddress);
              }},
              {mDataProp : null, fnRender : function(obj) {
                  return buildRemoveAssociationColumn(obj.aData, "${removeShippingDesigneeConfirmUrl}");
              }}
          ]);
      }

      function buildTable(tableId, dataRows, columns) {
          $('#' + tableId).dataTable( {
              aaData : dataRows,
              bInfo : false,
              bLengthChange: false,
              bPaginate: false,
              bFilter: true,
              sDom: 'lrtip',
              aoColumns: columns,
              fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                  $(nRow).attr("id", aData.id);
                  return nRow;
              },
              fnInitComplete: function() {
                  indicateLoading(false);
              }
          });
      }

      function buildRemoveAssociationColumn(association, url) {
          return ajaxImageFormatter(association.id, {
            url:url,
              imageUrl : '${removeIconUrl}',
              imageTitle : 'Remove Association',
              paramName: "personAssociationId",
              paramValue: 'id',
              action: 'removeAssociation',
              target: 'profileDialog'
              }, association);
      }

    });
</script>