$(document).ready(function () {

    $.ajaxSetup({
        contentType: "application/json; charset=utf-8"
    });

    const STATUS_SELECT_MENU_WIDTH = 600,
        DIALOG_HEIGHT = 430,
        DIALOG_WIDTH = 800,
        COMMENT_WIDTH = 595,
        DEPARTURE_STATUS = "DEPARTURE";

    // create divs that will be dialog container
    $("body").append(
        '<div id="statusChangeDialog" title="Выбор нового статуса">' +
        '<table>' +
        '<tr valign="top" id="currentStatusTR" ><td width="200" padding="10px"><label for="statusCurrent">Текущий статус: </label></td><td><strong id="statusCurrent"></strong></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="statusSelect">Новый статус: </label></td><td><select id="statusSelect"></select></td></tr>' +
        '<tr id="companyTr" valign="top" >  <td width="200"><label for="companyInput">Транспортная компания: </label></td><td><input id="companyInput" /></td></tr>' +
        '<tr id="vehicleNumberTr" valign="top" ><td width="200"><label for="vehicleNumberInput">Транспортное средство #1: </label></td><td><input id="vehicleNumberInput"/></td></tr>' +
        // '<tr id="vehicleNumber2Tr" valign="top" ><td width="200"><label for="vehicleNumber2Input">Транспортное средство #2: </label></td><td><input id="vehicleNumber2Input"/></td></tr>' +
        // '<tr id="vehicleNumber3Tr" valign="top" ><td width="200"><label for="vehicleNumber3Input">Транспортное средство #3: </label></td><td><input id="vehicleNumber3Input"/></td></tr>' +
        '<tr id="driverTr" valign="top" ><td width="200"><label for="driverInput">Водитель: </label></td><td><input id="driverInput" /></td></tr>' +
        '<tr id="linkTr"><td></td><td><a href="../../admin_page/#tabs-3">Добавить компанию/ТС/Водителя</a></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="dateTimePickerInput">Дата и время: </label></td><td><input id="dateTimePicker" type="text"></td></tr>' +
        '<tr id="goodCostTr" valign="top" ><td width="200"><label for="goodCost"> Стоймость </label></td><td><input id="goodCost" type="text"></td></tr>' +
        '<tr id="hoursAmountTr" valign="top" ><td width="200"><label for="hoursAmount">Кол-во часов: </label></td><td><input id="hoursAmount" type="text"></td></tr>' +
        '<tr id="palletsQtyTr" valign="top" ><td width="200"><label for="palletsQtyInput">Количество паллет: </label></td><td><input id="palletsQtyInput" type="text"/></td></tr>' +
        '<tr id="boxQtyTr" valign="top" ><td width="200"><label for="boxQtyInput">Количество коробок: </label></td><td><input id="boxQtyInput" type="text"/></td></tr>' +
        '<tr valign="top" ><td width="200"><label for="commentInput">Комментарий: </label></td><td><textarea id="commentInput" maxlength="500"/></td></tr>' +
        '<tr id="selectRequestsTr" valign="top"><td width="200"><label for="statusSelect">Накладные: </label></td><td><div id="requestCheckBoxes2"><table id="requestCheckBoxes"></table></div></td></tr>' +
        // '<tr id="selectNumbersRequestsTr" valign="top"><td width="200"><label for="statusSelect">Номера накладных: </label></td><td><div id="numberRequestCheckBoxes"></div></td></tr>' +
        '</table>' +
        '</div>'
    );
    if ($("#data-role").html().trim() == "Пользователь_клиента") {
        $('#vehicleNumberTr').hide();
        $('#linkTr').hide();
        $('#companyTr').hide();
        $('#driverTr').hide();
    }

    //populate TC select


    // var companySelectize = $('#companyInput')[0].selectize;
    // var clientSelectize = usersEditor.field('clientID').inst();

    // companySelectize.clear();
    // companySelectize.clearOptions();
    // companySelectize.load(function (callback) {
    //     callback(selectizeOptions);
    // });
    $('#vehicleNumberInput').selectize({
        sortField: "label",
        searchField: "label",
        valueField: "value",
        labelField: "label",
        onChange: function (value) {
            if (!value.length) return;
        }
    });

    // $('#vehicleNumber2Input').selectize({
    //     sortField: "label",
    //     searchField: "label",
    //     valueField: "value",
    //     labelField: "label",
    //     onChange: function (value) {
    //         if (!value.length) return;
    //     }
    // });
    //
    // $('#vehicleNumber3Input').selectize({
    //     sortField: "label",
    //     searchField: "label",
    //     valueField: "value",
    //     labelField: "label",
    //     onChange: function (value) {
    //         if (!value.length) return;
    //     }
    // });

    vehicleInputs = {
        vehicles: [],
        addVehicleInput: function (vehicle) {
            this.vehicles.push(vehicle);
        },
        disable: function () {
            this.vehicles.forEach(function (vehicle) {
                vehicle.disable();
            })
        },
        enable: function () {
            this.vehicles.forEach(function (vehicle) {
                vehicle.enable();
            })
        },
        load: function (callback) {
            this.vehicles.forEach(function (vehicle) {
                vehicle.clear();
                vehicle.clearOptions();
                vehicle.load(callback)
            })
        }

    };

    var driverInput = $('#driverInput').selectize({
        sortField: "label",
        searchField: "label",
        valueField: "value",
        labelField: "label",
        onChange: function (value) {
            if (!value.length) return;
        }
    })[0].selectize;

    vehicleInputs.addVehicleInput($('#vehicleNumberInput')[0].selectize);
    // vehicleInputs.addVehicleInput($('#vehicleNumber2Input')[0].selectize);
    // vehicleInputs.addVehicleInput($('#vehicleNumber3Input')[0].selectize);
    vehicleInputs.disable();
    driverInput.disable();


    var transportCompanySelectize = $('#companyInput').selectize({
        placeholder: "Нажмите, чтобы изменить",
        sortField: "label",
        searchField: "label",
        valueField: "value",
        labelField: "label",
        search: true,
        maxItems: 1,
        onChange: function (value) {
            if (!value.length) return;
            vehicleInputs.disable();
            $.get(
                `api/transportCompanies/${value}/vehicles`, function (vehiclesData) {
                    console.log("Vehicles:");
                    console.log(JSON.stringify(vehiclesData));
                    let vehicleOptions = [];
                    vehiclesData._embedded.vehicles.forEach(function (entry) {
                        vehicleOptions.push({label: entry.licenseNumber, value: entry.id});
                    });

                    vehicleInputs.load(function (callback) {
                        callback(vehicleOptions)
                    });
                    console.log(JSON.stringify(vehicleOptions));
                    vehicleInputs.enable();

                    driverInput.disable();
                    driverInput.clear();
                    driverInput.clearOptions();
                    $.get(
                        `api/transportCompanies/${value}/drivers`,
                        function (driversData) {
                            console.log("Drivers:");
                            console.log(driversData);
                            var driversOptions = [];
                            driversData._embedded.drivers.forEach(function (entry) {
                                driversOptions.push({label: entry.fullName, value: entry.id});
                            });


                            driverInput.load(function (callback) {
                                callback(driversOptions)
                            });

                        });
                    driverInput.enable();
                })

        }, onType: function (value) {
            this.clear();
            this.clearOptions();
            this.load(function (callback) {
                $.get("api/transportCompanies/search/findTop10ByNameContaining/?companyName=" + value,
                    function (data) {

                        var options = [];
                        // console.log(data._embedded.transportCompanies);
                        for (let i = 0; i < data._embedded.transportCompanies.length; i++) {
                            options.push({"label": data._embedded.transportCompanies[i].name, "value": data._embedded.transportCompanies[i].id})
                        }
                        // data._embedded.transportCompanies.forEach(function (entry) {function
                        //     options.push({"label": entry.name, "value": entry._links.self.href});
                        // });
                        callback(options);
                    });
            });
        }
    });

    // $.get("api/transporCompanies/search/findAll",
    //     function (companiesData) {
    //
    //         // var options = [];
    //         var selectizeOptions = [];
    //         companiesData = JSON.parse(companiesData);
    //
    //         companiesData.forEach(function (entry) {
    //
    //             // var option = "<option value=" + entry.id + ">" + entry.name + "</option>";
    //             // options.push(option);
    //             // console.log("id:"+entry.id+" name:"+entry.name+"\n");
    //             var selectizeOption = {text: entry.name, value: entry.id};
    //             selectizeOptions.push(selectizeOption);
    //         });

    //         var driverInput = $('#driverInput')[0].selectize;
    //         driverInput.disable();
    //

    //     }
    // );

    // create status select menu
    var $statusSelect = $("#statusSelect");
    var $requestCheckBoxes = $("#requestCheckBoxes");
    var $statusesRequest = $("#statusesRequest");
    var $numberRequestCheckBoxes = $("#numberRequestCheckBoxes");
    var $selectRequestsTr = $("#selectRequestsTr");
    var $selectNumbersRequestsTr = $("#selectNumbersRequestsTr");

    function populateVehicleSelectMenu() {
        var options = [];
    }


    function populateStatusSelectMenu() {
        var options = [];
        var storedStatuses = '';
        // if (window.localStorage["USER_STATUSES"]) {
        //     storedStatuses = JSON.parse(window.localStorage["USER_STATUSES"]);
        // }
        for (var prop in userRequestStatuses) {
            options.push("<option value='" + prop + "'>" + userRequestStatuses[prop] + "</option>");
        }
        // for (var i = 0; i < storedStatuses.length; i++) {
        //     options.push("<option value='" + storedStatuses[i].requestStatusID + "'>" + storedStatuses[i].requestStatusRusName + "</option>");
        // }
        $statusSelect.html(options.join("")).selectmenu({width: STATUS_SELECT_MENU_WIDTH});
        // console.log(JSON.stringify(storedStatuses));
    }

    // create palletsQty input
    $("#palletsQtyInput").mask("00", {
        placeholder: "0-99"
    });

    // create boxQty input
    $("#boxQtyInput").mask("0000", {
        placeholder: "0-9999"
    });

    // create comment input
    $("#commentInput")
        .addClass("ui-widget ui-state-default ui-corner-all")
        .attr("rows", "5")
        .attr("placeholder", "Введите комментарий")
        .width(COMMENT_WIDTH)
        .css("resize", "none");


    // create and init dateTimePicker
    // createDateTimePickerLocalization();
    // var $dateTimePicker = $("#dateTimePicker");
    // $dateTimePicker.datetimepicker();
    $('#dateTimePicker').mask('00.00.0000 00:00', {placeholder: '31.12.2016 12:36', clearIfNotMatch: true});
    if ($('#dateTimePicker').val() == '') {

        $('#dateTimePicker').val(new Date().toLocaleString('ru-RU', {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', hour12: false
        }).replace(',', '').replace('/', '.').replace('/', '.'));
    }
    $('#hoursAmount').mask('00000', {placeholder: '0-9999'});
    $('#goodCost').mask('00000', {placeholder: '0-9999'});

    var $statusChangeDialog = $("#statusChangeDialog");
    $statusChangeDialog.dialog({
        autoOpen: false,
        resizable: false,
        minWidth: DIALOG_WIDTH,
        minHeight: DIALOG_HEIGHT,
        maxWidth: DIALOG_WIDTH,
        maxHeight: DIALOG_HEIGHT,
        width: DIALOG_WIDTH,
        height: DIALOG_HEIGHT,
        modal: true,
        // when dialog open transform it to one of two types
        open: function () {

            var dialogType = $statusChangeDialog.data('dialogType');
            var dataTable = $statusChangeDialog.data('dataTable');


            // if ($statusSelect[0][$statusSelect[0].selectedIndex].value === "DELIVERED"){
            //     $('#hoursAmountTr').hide();
            // } else {
            //     $('#hoursAmountTr').show();
            // }

            $statusSelect.on("selectmenuchange", function (e, ui) {

                if ($statusSelect[0][$statusSelect[0].selectedIndex].value === "DELIVERED" && $("#data-role").html().trim() != "Пользователь_клиента") {
                    $('#hoursAmountTr').show();
                } else {
                    $('#hoursAmountTr').hide();
                }


            });

            $("#palletsQtyTr").hide();
            $("#boxQtyTr").hide();

            let routeListId = dataTable.row($('#user-grid .selected')).data().routeListID;
            switch (dialogType) {

                case "changeStatusForRequest":
                    $selectRequestsTr.hide();
                    // $statusSelect.off("selectmenuchange");
                    $('#statusCurrent').html(userRequestStatuses[dataTable.row($('#user-grid .selected')).data().requestStatusID]);
                    $requestCheckBoxes.html("");
                    $numberRequestCheckBoxes.html("");
                    $selectRequestsTr.hide();
                    break;
                case "changeStatusForSeveralRequests":
                    $selectRequestsTr.show();
                    $('#palletsQtyTr').show();
                    $('#boxQtyTr').show();
                    $statusSelect.on("selectmenuchange", function (e, ui) {
                        // console.log(($statusSelect[0][$statusSelect[0].selectedIndex].value));
                    });

                    $.get(
                        "/api/routeLists/" + routeListId + "/requests",
                        function (data) {
                            var requests = data._embedded.requests;
                            $statusChangeDialog.data('requestsForSelectedRouteList', requests);
                            $requestCheckBoxes.html("");
                            $numberRequestCheckBoxes.html("");
                            $('#statusCurrent').html(requests[0].requestStatusId);
                            $('#currentStatusTR').hide();
                            requests.forEach(function (request) {

                                $('#selectNumbersRequestsTr').show();
                                $statusesRequest.html('<span style="font-weight:bold;">' + request.requestStatusId + '</span>' + '  ');
                                $requestCheckBoxes.append('<tr><td><label><span style="font-weight:bold;">' + '<input type="checkbox" value=' + request.id+ ' checked>  ' + request.invoiceNumber + '</span></label></td><td>  <span>' + userRequestStatuses[request.requestStatusId] + '</span></td></tr>');
                                // $requestCheckBoxes.append('<label style="font-weight:bold;">'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.invoiceNumber+'</label><br>');
                                //$numberRequestCheckBoxes.append('<span style="font-weight:bold;">'+request.invoiceNumber+'</span>'+'&nbsp;&nbsp;');
                                //<label>'+'<input type="checkbox" value='+request.requestIDExternal+' checked>'+request.requestIDExternal+'</label></td><td>

                            });
                            $selectRequestsTr.show();
                            $selectNumbersRequestsTr.show();
                        }
                    );
                    break;
            }
        },


        buttons: {

            "Сохранить": function () {

                // get all common variables
                var dialogType = $statusChangeDialog.data('dialogType');
                var dataTable = $statusChangeDialog.data('dataTable');
                var newStatusID = $statusSelect[0][$statusSelect[0].selectedIndex].value;
                var date = $('#dateTimePicker')[0].value;
                var comment = $("#commentInput").val();
                var vehicleNumber = $("#vehicleNumberInput").val();
                var hoursAmount = $("#hoursAmount").val();
                var goodCost = $("#goodCost").val();
                var companyId = $("#companyInput")[0].selectize.getValue();
                var vehicleId = $("#vehicleNumberInput")[0].selectize.getValue().split(",");
                var driverID = $('#driverInput')[0].selectize.getValue().split(",");

                // console.log(companyId+' '+vehicleId+' '+driverID);
                if (dialogType === "changeStatusForRequest") {

                    // get specific vars for "changeStatusForRequest" dialogType

                    var requestIDExternal = dataTable.row($('#user-grid .selected')).data().requestIDExternal;
                    var requestId = dataTable.row($('#user-grid .selected')).data().requestID;
                    if (date)
                        $.ajax(
                            {
                                url: "api/requests/" + requestId,
                                type: "PATCH",
                                dataType: 'json',
                                contentType: 'application/json',
                                data: JSON.stringify({
                                    requestStatusId: newStatusID,
                                    lastStatusUpdated: date,
                                    commentForStatus: comment,
                                    hoursAmount: Number(hoursAmount),
                                    goodCost: Number(goodCost)
                                }),

                                success: function (data) {
                                    dataTable.draw(false);
                                    $statusChangeDialog.dialog("close");
                                },

                                error: function (data) {
                                    alert(data.message);
                                }

                            }
                        );
                    else {
                        alert("date should not be empty"); // TODO
                    }

                } else if (dialogType === "changeStatusForSeveralRequests") {

                    // get specific vars for "changeStatusForSeveralRequests" dialogType
                    //var routeListID = dataTable.row($('#user-grid .selected')).data().routeListID;
                    var palletsQty = $("#palletsQtyInput").cleanVal();
                    var boxQty = $("#boxQtyInput").cleanVal();

                    var requests = [];
                    $requestCheckBoxes.find("input[type='checkbox']:checked").each(function (index) {
                        requests.push($(this).attr('value'));
                    });
                    // var databind = ;

                    // console.log(requests);
                    if ((newStatusID !== DEPARTURE_STATUS && date) || (newStatusID === DEPARTURE_STATUS && date && palletsQty) || (newStatusID === DEPARTURE_STATUS && date && boxQty)){
                        console.log(dataTable.row($('#user-grid .selected')).data().routeListID);
                        $.post(
                            "/commands/updateRequests",
                            // JSON.stringify(requests[0]),
                            JSON.stringify({
                                requestStatus: newStatusID,
                                requestDate: date,
                                comment: comment,
                                vehicleNumber: vehicleNumber,
                                palletsQty: palletsQty,
                                boxQty: boxQty,
                                requests: requests,
                                routeListId: dataTable.row($('#user-grid .selected')).data().routeListID,
                                hoursAmount: Number(hoursAmount),
                                goodCost: Number(goodCost),
                                transportCompany: companyId,
                                vehicles: vehicleId,
                                drivers: driverID
                            }),
                            function (data) {
                                if (data == '1') {
                                    dataTable.draw(false);
                                    $statusChangeDialog.dialog("close");
                                }
                            }
                        )}
                    else {
                        alert("date and palletsQty should not be empty"); // TODO
                        alert("date and boxQty should not be empty"); // TODO
                    }
                }
            },
            "Претензия": function () {
                var dataTable = $statusChangeDialog.data('dataTable');
                var clientID = dataTable.row($('#user-grid .selected')).data().clientIDExternal,
                    invoiceNumber = dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                var url =
                    "?clientId=" +
                    clientID +
                    "&invoiceNumber=" +
                    invoiceNumber +
                    "&pretensionModal=1";
                url = encodeURI(url);
                pretensionWindow = window.open(url, "width=400, height=700");
            },
            "Отмена": function () {
                $(this).dialog("close");
            }
        }
    });

    // external function
    $.showRequestStatusDialog = function (dialogType, dataTable) {
        $statusChangeDialog
            .data("dialogType", dialogType)
            .data("dataTable", dataTable)
            .dialog("open");
        populateStatusSelectMenu();

    };

    function createDateTimePickerLocalization() {
        $.datepicker.regional['ru'] = {
            closeText: 'Закрыть',
            prevText: '<Пред',
            nextText: 'След>',
            currentText: 'Сегодня',
            monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            dayNames: ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
            dayNamesShort: ['вск', 'пнд', 'втр', 'срд', 'чтв', 'птн', 'сбт'],
            dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
            weekHeader: 'Не',
            dateFormat: 'dd.mm.yy',
            firstDay: 1,
            isRTL: false,
            showMonthAfterYear: false,
            yearSuffix: ''
        };
        $.datepicker.setDefaults($.datepicker.regional['ru']);
        $.timepicker.regional['ru'] = {
            timeOnlyTitle: 'Выберите время',
            timeText: 'Время',
            hourText: 'Часы',
            minuteText: 'Минуты',
            secondText: 'Секунды',
            millisecText: 'Миллисекунды',
            timezoneText: 'Часовой пояс',
            currentText: 'Сейчас',
            closeText: 'Закрыть',
            timeFormat: 'HH:mm',
            amNames: ['AM', 'A'],
            pmNames: ['PM', 'P'],
            isRTL: false
        };
        $.timepicker.setDefaults($.timepicker.regional['ru']);
    }
});