<%@ tag body-content="empty" %>
<%@ attribute name="address" type="gov.nih.nci.firebird.data.Address" rtexprvalue="true" required="true" %>
<%@ attribute name="tagVariableName" description="Variable name for this address display tag and enclosing div ID" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<span id="${tagVariableName}">
    <span id="streetAddress">
        ${address.streetAddress}
    </span>
    <br/>
    <span id="deliveryAddress">
    <c:if test="${not empty address.deliveryAddress}">
        ${address.deliveryAddress} <br/>
    </c:if>
    </span>
    <span id="city">
        ${address.city}
    </span>
    <s:if test="%{#attr.address.stateOrProvince not in #{null,''}}">
        <span id="stateOrProvinceWrapper">
            ,
            <span id="stateOrProvince">
                ${address.stateOrProvince}
            </span>
        </span>
    </s:if>
    <s:else>
        <span id="stateOrProvinceWrapper" style="display:none;">
            ,
            <span id="stateOrProvince"></span>
        </span>
    </s:else>
    <span id="postalCode">
        ${address.postalCode}
    </span>
    <br/>
    <span id="country">
        ${address.country}
    </span>
</span>

<s:if test="%{#attr.tagVariableName not in #{null,''}}">
    <script>
        var ${tagVariableName} = {
              setAddress: function(address) {
                  $("#${tagVariableName} #streetAddress").html(address.streetAddress);
                  var deliveryAddress = "";
                  if (address.deliveryAddress != null && address.deliveryAddress != "") {
                    deliveryAddress = address.deliveryAddress + " <br/>"
                  }
                  $("#${tagVariableName} #deliveryAddress").html(deliveryAddress);
                  $("#${tagVariableName} #city").html(address.city);
                  $("#${tagVariableName} #stateOrProvince").html(address.stateOrProvince);
                  var showState = address.stateOrProvince != null && address.stateOrProvince != "";
                  $("#${tagVariableName} #stateOrProvinceWrapper").toggle(showState);
                  $("#${tagVariableName} #postalCode").html(address.postalCode);
                  $("#${tagVariableName} #country").html(address.country);
            }
        };
    </script>
</s:if>
