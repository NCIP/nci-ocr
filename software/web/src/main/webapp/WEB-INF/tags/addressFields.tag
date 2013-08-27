<%@ tag body-content="empty" %>
<%@ attribute name="beanPrefix" required="true" %>
<%@ attribute name="varPrefix" %>
<%@ attribute name="fieldsDisabled" required="false" rtexprvalue="true"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:set var="prefix" value="%{#attr.varPrefix.replaceAll('\\\\.','_')}"/>

<div class="${prefix}_addressInput addressInput">
    <s:textfield
        id="%{#attr.beanPrefix}.streetAddress"
        name="%{#attr.beanPrefix}.streetAddress"
        maxlength="254" size="50" cssStyle="width: 19em;" requiredLabel="true"
        label="%{getText('textfield.line1Address')}" labelposition="left" />
    <br>
    <div class="addressLine2">
        <s:textfield
            id="%{#attr.beanPrefix}.deliveryAddress"
            name="%{#attr.beanPrefix}.deliveryAddress"
            maxlength="254" size="50" cssStyle="width: 19em;"
            label="%{getText('textfield.line2Address')}" labelposition="left" />
    </div>
    <br>
    <s:textfield
        id="%{#attr.beanPrefix}.city"
        name="%{#attr.beanPrefix}.city"
        maxlength="50" size="30" cssStyle="width: 19em;" requiredLabel="true"
        label="%{getText('textfield.city')}" labelposition="left" />
    <br>
    <s:select id="%{#attr.beanPrefix}.country" list="countries" listKey="alpha3"
        listValue="name" label="%{getText('dropdown.country')}"
        name="%{#attr.beanPrefix}.country"
        labelposition="left" requiredLabel="true" onchange="%{#prefix}_addressForm.showFieldsForSelectedCountry()"/>
    <br>
    <div class="stateOrProvinceDiv">
        <div class="usStateDiv">
            <s:select
                id="%{#attr.beanPrefix}.stateOrProvince.us"
                headerKey="" headerValue="--- Select ---" list="states"
                listKey="code" listValue="name"
                name="%{#attr.beanPrefix}.stateOrProvince"
                requiredLabel="true" label="%{getText('dropdown.state')}"
                labelposition="left" />
                <s:if test="%{#attr.fieldsDisabled}">
                    <s:hidden name="%{#attr.beanPrefix}.stateOrProvince" />
                </s:if>
            <br>
        </div>
        <div class="internationalStateDiv">
            <s:textfield
                id="%{#attr.beanPrefix}.stateOrProvince.international"
                name="%{#attr.beanPrefix}.stateOrProvince"
                maxlength="50" size="30" cssStyle="width: 19em;" requiredLabel="false"
                label="%{getText('textfield.stateOrProvince')}" labelposition="left" />
            <br>
        </div>
    </div>
    <firebird:postalCode beanPrefix="${beanPrefix}" varPrefix="${prefix}"/>
</div>
<script>
var ${prefix}_addressForm = (function() {
    var form = ${prefix}_addressForm || {};
    var beanPrefix = "${beanPrefix}";
    var countrySelector = escapeForJquery("#" + beanPrefix + ".country");

    form.showFieldsForSelectedCountry = function() {
        var postalCodeId = beanPrefix + ".postalCode";

        if (form.isUSSelected()) {
            _fbUtil.switchObjects(form.${prefix}_internationalStateDiv, form.${prefix}_usStateDiv);
        } else {
            _fbUtil.switchObjects(form.${prefix}_usStateDiv, form.${prefix}_internationalStateDiv);
            form.removeFieldErrorsFromInternationalStateDiv();
        }

        if (form.phoneNumberField !== undefined) {
          form.phoneNumberField.updateMask();
        }
        if (form.postalCodeField !== undefined) {
          form.postalCodeField.updateMask();
        }
    };

    form.removeFieldErrorsFromInternationalStateDiv = function() {
        $(".${prefix}_addressInput .internationalStateDiv .errorMessage").detach();
        $(".${prefix}_addressInput .internationalStateDiv .fieldError").removeClass("fieldError");
    }

    form.isUSSelected = function() {
        return $(countrySelector).val() == US_COUNTRY_CODE;
    };

    form.isCANSelected = function() {
        return $(countrySelector).val() == CAN_COUNTRY_CODE;
    };

    form.initialize = function() {
        setDiv('.${prefix}_addressInput .usStateDiv', '${prefix}_usStateDiv');
        setDiv('.${prefix}_addressInput .internationalStateDiv', '${prefix}_internationalStateDiv');
        form.showFieldsForSelectedCountry();
    };

    function setDiv(divId, propertyName) {
        var div$ = $(divId);
        if (div$.length > 0) {
            form[propertyName] = div$;
        }
    }
    return form;

})();


$(document).ready(function() {
    if ('${fieldsDisabled}' == "true") {
        $('.${prefix}_addressInput select').attr("disabled", true);
        $('.${prefix}_addressInput input').attr("readonly", true);
    }
    ${prefix}_addressForm.initialize();
});

</script>