<%@ tag body-content="empty" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ attribute name="rangeInYears"%>
<%@ attribute name="forwardRangeInYears"%>
<%@ attribute name="fieldName" required="true"%>
<%@ attribute name="label" required="true"%>
<%@ attribute name="required"%>
<%@ attribute name="isFuture"%>

<s:set var="rangeInYears" value="%{#attr.rangeInYears == null ? getText('default.date.year.range') : #attr.rangeInYears}" />
<s:set var="isForwardRangeInYearsSupplied" value="%{#attr.forwardRangeInYears == null ? 'false' : 'true'}" />
<s:set var="forwardRangeInYears" value="%{#attr.forwardRangeInYears == null ? #rangeInYears : #attr.forwardRangeInYears}" />

<s:fielderror>
    <s:param>${fieldName}</s:param>
</s:fielderror>

<div class="formcol_xthin">
    <s:select id="%{#attr.fieldName}MonthSelect"
        list="#{1 : 'Jan', 2 : 'Feb', 3 : 'Mar', 4 : 'Apr', 5 : 'May', 6 : 'Jun', 7 : 'Jul', 8 : 'Aug', 9 : 'Sep', 10 : 'Oct', 11 : 'Nov', 12 : 'Dec'}"
        headerKey="" headerValue="-- Month --"
        requiredLabel="%{#attr.required}"
        label="%{getText(#attr.label)}"/>
</div>
<s:div id="%{#attr.fieldName}Divider" cssClass="formcol_icon dateSelectDivider">
    <p id="divider">/</p>
</s:div>

<div class="formcol_xthin">
    <label for="${fieldName}YearSelect">&nbsp;</label>
    <br/>
    <div>
        <select id="${fieldName}YearSelect">
            <option value="">-- Year --</option>
        </select>
    </div>
    <s:hidden name="%{#attr.fieldName}" id="%{#attr.fieldName}" />
</div>


<!--/Content-->
<script type="text/javascript">

    $(document).ready(function() {
        initializeYearSelect();
        setDateSelectors('${fieldName}');
        setupSelectChangeEvents();

        if (hasErrorMessage()) {
          highlightMonth();
          highlightYear();
          adjustDividerPosition();
        }
    });

    function initializeYearSelect() {
        var currentDate = new Date();
        var currentYear = currentDate.getFullYear();
        var isFuture = '${isFuture}' == 'true';

        if (isFuture) {
            buildDropDownFuture(currentYear);
        } else {
            buildDropDownPast(currentYear);
        }
    }

    function buildDropDownFuture(year) {
        var topRange = year + <s:property value="forwardRangeInYears"/>;
        for ( var i = year; i <= topRange; i++) {
            $('<option></option>').text(i).appendTo(
                    escapeForJquery('#${fieldName}YearSelect'));
        }
    }

    function buildDropDownPast(year) {
        var startYear = year - <s:property value="rangeInYears"/>;
        var endYear = year;
        var isForwardRangeInYearsSupplied = '${isForwardRangeInYearsSupplied}' == 'true';
        if (isForwardRangeInYearsSupplied) {
          endYear += <s:property value="forwardRangeInYears"/>;
        }
        for ( var i = endYear; i >= startYear; i--) {
            $('<option></option>').text(i).appendTo(
                    escapeForJquery('#${fieldName}YearSelect'));
        }
    }

    function setDateSelectors(fieldName) {
        if (!isBlank($('#' + fieldName).val())) {
            var splitDate = $('#' + fieldName).val().split('/');
            var month = makeUnPaddedMonth(splitDate[0]);
            var year = splitDate[1];
            var monthSelect = escapeForJquery('#' + fieldName + 'MonthSelect');
            var yearSelect = escapeForJquery('#' + fieldName + 'YearSelect');

            $(monthSelect).val(month);
            $(yearSelect).val(year);
        }
    }

    function makeUnPaddedMonth(month) {
        if (month.charAt(0) == '0') {
            return month.charAt(1);
        } else {
            return month;
        }
    }

    function setupSelectChangeEvents() {
        var monthSelect = escapeForJquery('#${fieldName}MonthSelect');
        var yearSelect = escapeForJquery('#${fieldName}YearSelect');

        $(monthSelect + ', ' + yearSelect).change(function() {
            updateDate(monthSelect, yearSelect, '${fieldName}');
        });
    }

    function updateDate(monthSelect, yearSelect, fieldName) {
        var selectedMonth = (isBlank($(monthSelect).val()) ? null : $(
                monthSelect).val());
        var selectedYear = $(yearSelect).val();
        if (isBlank(selectedMonth) || isBlank(selectedYear)) {
            $('#' + fieldName).val('');
        } else {
            $('#' + fieldName).val(selectedMonth + "/" + selectedYear);
        }
    }

    function hasErrorMessage() {
      return $("#${fieldName}Divider").parent().find(".errorMessage").length > 0;
    }

    function highlightMonth() {
        var monthSelect = escapeForJquery('#${fieldName}MonthSelect');
        $(monthSelect).parent().addClass("monthSelectFieldError");
    }

    function highlightYear() {
        var yearSelect = escapeForJquery('#${fieldName}YearSelect');
        $(yearSelect).parent().addClass("yearSelectFieldError");
    }

    function adjustDividerPosition() {
        var divider = $(escapeForJquery('#${fieldName}Divider'));
        divider.removeClass("dateSelectDivider");
        divider.addClass("dateSelectDividerFieldError");
    }

</script>
