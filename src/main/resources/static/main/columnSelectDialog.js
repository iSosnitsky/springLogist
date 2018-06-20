$(document).ready(function () {
    var ALL_NAMES = [
        "requestIDExternal",
        "requestNumber",
        "requestDate",
        "invoiceNumber",
        "invoiceDate",
        "documentNumber",
        "documentDate",
        "firma",
        "storage",
        "commentForStatus",
        "boxQty",
        "requestStatusRusName",
        "clientIDExternal",
        "INN",
        "clientName",
        "marketAgentUserName",
        "deliveryPointName",
        "warehousePointName",
        "lastVisitedPointName",
        "nextPointName",
        "routeName",
        "driverUserName",
        "licensePlate",
        "palletsQty",
        "routeListNumber",
        "arrivalTimeToNextRoutePoint"
    ];

    var ADMIN_COL_VISIBLE = [
        "requestIDExternal",
        "requestNumber",
        "requestDate",
        "invoiceNumber",
        "invoiceDate",
        "documentNumber",
        "documentDate",
        "firma",
        "storage",
        "commentForStatus",
        "boxQty",
        "requestStatusRusName",
        "clientIDExternal",
        "INN",
        "clientName",
        "marketAgentUserName",
        "deliveryPointName",
        "warehousePointName",
        "lastVisitedPointName",
        "nextPointName",
        "routeName",
        "driverUserName",
        "licensePlate",
        "palletsQty",
        "routeListNumber",
        "arrivalTimeToNextRoutePoint"
    ];
    var CLIENT_MANAGER_COL_VISIBLE = [
        "invoiceNumber",
        "invoiceDate",
        "boxQty",
        "requestStatusRusName",
        "deliveryPointName",
        "lastVisitedPointName",
        "nextPointName",
        "arrivalTimeToNextRoutePoint"
    ];
    var DISPATCHER_COL_VISIBLE = [
        "invoiceNumber",
        "invoiceDate",
        "storage",
        "boxQty",
        "requestStatusRusName",
        "deliveryPointName",
        "warehousePointName",
        "lastVisitedPointName",
        "nextPointName",
        "routeName",
        "driverUserName",
        "palletsQty",
        "routeListNumber",
        "arrivalTimeToNextRoutePoint"
    ];
    var MARKET_AGENT_COL_VISIBLE = [
        "invoiceNumber",
        "invoiceDate",
        "storage",
        "boxQty",
        "requestStatusRusName",
        "deliveryPointName",
        "warehousePointName",
        "lastVisitedPointName",
        "nextPointName",
        "routeName",
        "driverUserName",
        "palletsQty",
        "routeListNumber",
        "arrivalTimeToNextRoutePoint"
    ];
    var W_DISPATCHER_COL_VISIBLE = [
        "invoiceNumber",
        "invoiceDate",
        "storage",
        "boxQty",
        "requestStatusRusName",
        "deliveryPointName",
        "warehousePointName",
        "lastVisitedPointName",
        "nextPointName",
        "routeName",
        "driverUserName",
        "palletsQty",
        "routeListNumber",
        "arrivalTimeToNextRoutePoint"
    ];

    var role = $('#data-role').attr('data-role');

    function getVisibleColsByRole(role) {
        if (role == 'ADMIN') {
            return ADMIN_COL_VISIBLE;
        } else if (role == 'CLIENT_MANAGER') {
            return CLIENT_MANAGER_COL_VISIBLE;
        } else if (role == 'DISPATCHER') {
            return DISPATCHER_COL_VISIBLE;
        } else if (role == 'MARKET_AGENT') {
            return MARKET_AGENT_COL_VISIBLE;
        } else if (role == 'W_DISPATCHER') {
            return W_DISPATCHER_COL_VISIBLE;
        }
    }

    function getInputs(dataTable) {
        var inputs = {};
        ALL_NAMES.forEach(function (elem) {
            var column = dataTable.column(elem + ":name");
            var columnIndex = column.index();
            var columnVisibility = column.visible();
            var columnRusName = $(column.header()).attr("aria-label").split(":")[0];
            if (columnRusName != '' && columnRusName != undefined) {
                var input = $("<input>").attr("type", "checkbox").attr("id", columnIndex).prop("checked", columnVisibility);
                input.change(function () {
                    column.visible(this.checked);
                });
                var label = $("<label>").attr("for", columnIndex).html(columnRusName);
                $inputsContainer.append(input, label, "<br>");
                inputs[elem] = input;
            }
        });
        return inputs;
    }

    var html =
        '<div id="columnSelectDialogContainer" title="Выбор столбцов">' +
        '<div id="inputsContainer" title="Выбор столбцов"></div>' +
        '<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">' +
        '<div class="ui-dialog-buttonset" id="columnSelectDialogButtonContainer"></div></div>' +
        '</div>';
    $("body").append(html);

    var $columnSelectDialogContainer = $("#columnSelectDialogContainer");
    var $inputsContainer = $("#inputsContainer");
    var $buttonContainer = $("#columnSelectDialogButtonContainer");

    $.showColumnSelectDialog = function (dataTable) {
        $buttonContainer.html("");
        $inputsContainer.html("");
        var inputs = getInputs(dataTable);

        var visibleColsForRole = getVisibleColsByRole(role);

        $("<input>")
            .attr("type", "button")
            .val((localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Обычный поиск')
            .addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only")
            .on("click", function () {
                (localStorage.getItem("liveSearch") === 'true') ? localStorage.setItem("liveSearch", false) : localStorage.setItem("liveSearch", true);
                $(this).val((localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Обычный поиск');
            }).appendTo($buttonContainer);

        $("<input>")
            .attr("type", "button")
            .val("Сброс фильтров")
            .addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only")
            .on("click", function () {
                $('.searchColumn').each(function () {
                    $(this).val("").attr("currentFilter", "");
                });
                dataTable.columns().every(function () {

                    // $(this.footer()).css("background-color", "f6f6f6");
                    $(this.footer()).removeClass("footer-search",1000,"easeInBack");
                    this.search("");
                });
                dataTable.columns().draw();
            }).appendTo($buttonContainer);

        $("<input>")
            .attr("type", "button")
            .val("Сохранить")
            .addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only")
            .on("click", function () {
                $columnSelectDialogContainer.dialog("close");
            })
            .appendTo($buttonContainer);

        $("<input>")
            .attr("type", "button")
            .val("Сброс")
            .addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only")
            .on("click", function () {
                visibleColsForRole.forEach(function (elem) {
                    inputs[elem].prop("checked", true);
                    dataTable.column(elem + ":name").visible(true);
                });
                allNamesFiltered(visibleColsForRole).forEach(function (elem) {
                    inputs[elem].prop("checked", false);
                    dataTable.column(elem + ":name").visible(false);
                });
            })
            .appendTo($buttonContainer);


        $columnSelectDialogContainer.dialog("open");
    };

    $.getDefaultColumns = function (dataTable, role) {
        var inputs = getInputs(dataTable);

        var visibleColsForRole = getVisibleColsByRole(role);

        visibleColsForRole.forEach(function (elem) {
            inputs[elem].prop("checked", true);
            dataTable.column(elem + ":name").visible(true);
        });
        allNamesFiltered(visibleColsForRole).forEach(function (elem) {
            inputs[elem].prop("checked", false);
            dataTable.column(elem + ":name").visible(false);
        });
    };

    $columnSelectDialogContainer.dialog({
        autoOpen: false,
        resizable: false,
        height: 710,
        width: 500,
        modal: true
    });

    function allNamesFiltered(arr) {
        return ALL_NAMES.filter(function (elem) {
            return arr.indexOf(elem) == -1;
        });
    }
});