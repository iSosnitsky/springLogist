$(document).ready(function () {
    var role_type = "role-type";
    $('button').addClass('ui-state-active ui-state focus');
    // --------LOGOUT-----------------
    $("#logout").on("click", function () {
        // delete auth cookies
        $.cookie('SESSION_CHECK_STRING', null, -1, '/');
        // make redirect to login page
        document.location = '/';
        // window.location.reload();
    });

    $('#requestAccordion').accordion({
        collapsible: true,
        active: false,
        icons: {
            header: "ui-icon-plus",
            activeHeader: "ui-icon-plus"
        }
    });


    $('#routeListForm').on('submit', function (e) {
        e.preventDefault();

        let data = $(this).serializeArray().reduce(function (a, x) {
            a[x.name] = x.value;
            return a;
        }, {});
        // console.log(JSON.stringify(data));

        $.post(
            "content/getData.php",
            {
                status: "addRouteList",
                format: "json",
                data: data
            },
            function (data) {
                if (data) {
                    loadRouteLists(data);
                    console.log(data);
                    requestEditor.field('routeListID').inst().setValue(JSON.parse(data)[0].routeListId);
                }
                $('#requestAccordion').accordion('option', {active: false});
                $(this).trigger('reset');
            })
        // error: function (jXHR, textStatus, errorThrown) {
        //     alert(errorThrown+jXHR+textStatus);
        // },
        // done: function (data) {
        //     alert(data)
        // }
    });


    var requestEditor = new $.fn.dataTable.Editor({
        ajax: "content/getData.php",
        template: '#requestForm',
        table: "#user-grid",
        name: 'Создать новую заявку',
        idSrc: 'requestIDExternal',
        fields: [
            {
                label: 'Клиент (ИНН)',
                name: 'clientID',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Торговый представитель',
                name: 'marketAgentUserId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                },


            },
            {
                label: 'Номер накладной',
                name: 'invoiceNumber'
            },
            {
                label: 'Номер документа',
                name: 'documentNumber'
            },
            {
                label: 'Фирма',
                name: 'firma'
            },
            {
                label: 'Склад отправки',
                name: 'storage'
            },
            {
                label: 'Кол-во коробок',
                name: 'boxQty',
                type: 'mask',
                mask: '099',
                placeholder: "99"
            },
            {
                label: 'Утилизация',
                name: 'Uti',
                type: 'mask',
                mask: '099',
                placeholder: "99"
            },
            {
                label: 'Дата доставки',
                name: 'deliveryDate',
                type: 'mask',
                mask: '00.00.0000',
                placeholder: '31.12.2012'

            },
            {
                label: 'Пункт склада',
                name: 'warehousePointId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },

            {
                label: 'Маршрутный лист',
                name: 'routeListID',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Транспортная компания',
                name: 'transportCompanyId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'text',
                    labelField: 'text',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Модель ТС',
                name: 'vehicleId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'text',
                    labelField: 'text',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Водитель',
                name: 'driverId',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'text',
                    labelField: 'text',
                    dropdownParent: "body"
                }
            }
        ],
        i18n: {
            "create": {
                "title": "Новая заявка",
                "submit": "Добавить"
            },
            "edit": {
                // "button": "Изменить",
                "title": "Изменить заявку",
                "submit": "Обновить"
            },
            "error": {
                "system": "Возникла системная ошибка"
            },
            "multi": {
                "title": "Множество значений",
                "info": "The selected items contain different values for this input. To edit and set all items for this input to the same value, click or tap here, otherwise they will retain their individual values.",
                "restore": "Обратить изменения"
            },
            "datetime": {
                "previous": "Предыдущая",
                "next": "Следующая",
                "months": ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
                "weekdays": ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
                "amPm": ["am", "pm"],
                "unknown": "-"
            }
        }
    });

// requestEditor.field('deliveryDate').input().on('keydown',function () {
//     console.log(requestEditor.field('deliveryDate').input().value);
// });

    requestEditor.on('open', function (e, type) {

        // mask: '00.00.0000',
        //     placeholder: '31.12.2016',
        //     clearIfNotMatch: true
        // $('#dateTimePicker').mask('00.00.0000', {placeholder: '31.12.2016', clearIfNotMatch:true});
        // if ($('#dateTimePicker').val()==''){
        //
        //     $('#dateTimePicker').val(new Date().toLocaleString('ru-RU',{
        //         day:'2-digit', month:'2-digit',year:'numeric', hour:'2-digit',minute:'2-digit', hour12:false
        //     }).replace(',','').replace('/','.').replace('/','.'));
        // }
        // $.post(
        //     "content/getData.php",
        //     {
        //         status: "getAllRouteIdDirectionPairs", format: "json"
        //     },
        //     function (data) {
        //         // console.log(data);
        //         let options = [];
        //         // console.log(data);
        //         data = JSON.parse(data);
        //         data.forEach(function (entry) {
        //             var selectizeOption = {text: entry.directionName, value: entry.routeID};
        //             options.push(selectizeOption);
        //             // let option = "<option value=" + entry.routeID + ">" + entry.directionName + "</option>";
        //             // options.push(option);
        //         });
        //         let $select = $("#routeId").selectize(
        //             {
        //                 sortField: "text",
        //                 diacritics: true,
        //                 maxItems: 1,
        //                 options: options,
        //                 dropdownParent: 'body'
        //             }
        //         );
        //         selectizeValidationFix($select);
        //
        //         $("#routePalletsQty").mask('00');
        //         // $("#routeLicensePlate").mask('AAAAAAAA');
        //         // $("#routeId").selectize.load(function (callback) {
        //         //     callback(selectizePointsOptions);
        //         // });
        //     }
        // );
    });

    function selectizeValidationFix($select) {

        var self = $select[0].selectize;

        self.$input.on('invalid', function (event) {
            event.preventDefault();
            self.focus(true);
            self.$wrapper.addClass('invalid');
        });

        self.$input.on('change', function (event) {
            if (event.target.validity && event.target.validity.valid) {
                self.$wrapper.removeClass('invalid');
            }
        });

    }

    requestEditor.field('clientID')
        .input()
        .on('keyup', function (e, d) {
            var clientINNPart = $(this).val();
            $.get("api/clients",
                {status: "getClientsByINN", format: "json", inn: clientINNPart},
                function (clientsData) {
                    var selectizeOptions = [];
                    // console.log(clientsData);
                    clientsData = JSON.parse(clientsData);
                    clientsData.forEach(function (entry) {
                        var selectizeOption = {
                            "label": "ИНН: " + entry.INN + ", имя: " + entry.clientName,
                            "value": entry.clientID
                        };
                        selectizeOptions.push(selectizeOption);
                    });
                    let clientSelectize = requestEditor.field('clientID').inst();

                    clientSelectize.clear();
                    clientSelectize.clearOptions();
                    clientSelectize.load(function (callback) {
                        callback(selectizeOptions);
                    });
                }
            );
        });

// $('#deliveryDate').mask('00.00.0000', {placeholder: '31.12.2016', clearIfNotMatch:true});
// if (requestEditor.field('deliveryDate').input().val()===''){
//     requestEditor.field('deliveryDate').input().val(new Date().toLocaleString('ru-RU',{
//         day:'2-digit', month:'2-digit',year:'numeric', hour:'2-digit',minute:'2-digit', hour12:false
//     }).replace(',','').replace('/','.').replace('/','.'));
// }

    // requestEditor.field('warehousePointId').input().on('keyup', function (e, d) {
    //     let pointNamePart = $(this).val();
    //     $.post("content/getData.php",
    //         {status: "getPointsByName", format: "json", name: pointNamePart},
    //         function (wareHouseData) {
    //             let options = [];
    //
    //             let selectizePointsOptions = [];
    //             wareHouseData = JSON.parse(wareHouseData);
    //             wareHouseData.forEach(function (entry) {
    //                 let option = "<option value=" + entry.pointID + ">" + entry.pointName + "</option>";
    //                 options.push(option);
    //                 let selectizeOption = {"label": entry.pointName, "value": entry.pointID};
    //                 selectizePointsOptions.push(selectizeOption);
    //             });
    //
    //             let selectize = requestEditor.field('warehousePointId').inst();
    //             selectize.clear();
    //             selectize.clearOptions();
    //             selectize.load(function (callback) {
    //                 callback(selectizePointsOptions);
    //             });
    //         }
    //     );
    // });

    // requestEditor.field('marketAgentUserId').input().on('keyup', function (e, d) {
    //     const marketAgentName = $(this).val();
    //     if (marketAgentName !== '') {
    //         $.post("content/getData.php",
    //             {status: "getMarketAgentsByName", format: "json", name: marketAgentName},
    //             function (marketAgentData) {
    //                 let options = [];
    //
    //                 // console.log(marketAgentData);
    //                 let selectizePointsOptions = [];
    //                 marketAgentData = JSON.parse(marketAgentData);
    //                 marketAgentData.forEach(function (entry) {
    //                     let option = "<option value=" + entry.userID + ">" + entry.userName + "</option>";
    //                     options.push(option);
    //                     let selectizeOption = {"label": entry.userName, "value": entry.userID};
    //                     selectizePointsOptions.push(selectizeOption);
    //                 });
    //
    //                 let selectize = requestEditor.field('marketAgentUserId').inst();
    //                 selectize.clear();
    //                 selectize.clearOptions();
    //                 selectize.load(function (callback) {
    //                     callback(selectizePointsOptions);
    //                 });
    //             }
    //         );
    //     }
    //
    // });


    // requestEditor.field('routeListID').input().on('keyup', function (e, d) {
    //     let routeListNumber = $(this).val();
    //     $.post("content/getData.php",
    //         {status: "getRouteListsByNumber", format: "json", number: routeListNumber},
    //         function (routeListData) {
    //             loadRouteLists(routeListData)
    //             // let options = [];
    //             //
    //             // let selectizeRouteListsOptions = [];
    //             // // console.log(routeListData);
    //             // // console.log(routeListData);
    //             // routeListData = JSON.parse(routeListData);
    //             // routeListData.forEach(function (entry) {
    //             //     let option = "<option value=" + entry.routeListID + ">" + entry.routeListNumber + "</option>";
    //             //     options.push(option);
    //             //     let selectizeOption = {"label": entry.routeListNumber, "value": entry.routeListId};
    //             //     selectizeRouteListsOptions.push(selectizeOption);
    //             // });
    //             //
    //             // let selectize = requestEditor.field('routeListID').inst();
    //             // selectize.clear();
    //             // selectize.clearOptions();
    //             // selectize.load(function (callback) {
    //             //     callback(selectizeRouteListsOptions);
    //             // });
    //         }
    //     );
    // });

    function loadRouteLists(routeListData) {
        let selectizeRouteListsOptions = [];
        routeListData = JSON.parse(routeListData);
        routeListData.forEach(function (entry) {
            let selectizeOption = {"label": entry.routeListNumber, "value": entry.routeListId};
            selectizeRouteListsOptions.push(selectizeOption);
        });

        let selectize = requestEditor.field('routeListID').inst();
        selectize.clear();
        selectize.clearOptions();
        selectize.load(function (callback) {
            callback(selectizeRouteListsOptions);
        });


    }



    requestEditor.on('preSubmit', function (e, data, action) {
        // console.log(JSON.stringify(e));
        // console.log(JSON.stringify(data));
        if (action !== 'remove') {
            let clientID = this.field('clientID');
            let marketAgentUserId = this.field('marketAgentUserId');
            let invoiceNumber = this.field('invoiceNumber');
            let documentNumber = this.field('documentNumber');
            let warehousePointId = this.field('warehousePointId');
            let routeListID = this.field('routeListID');

            if (!clientID.val()) {
                clientID.error('Клиент должен быть указан')
            }
            if (!marketAgentUserId.val()) {
                marketAgentUserId.error('Торговый представитель должен быть указан')
            }
            if (!invoiceNumber.val()) {
                invoiceNumber.error('Номер накладной должен быть указан')
            }
            if (!documentNumber.val()) {
                documentNumber.error('Номер документа должен быть указан')
            }
            if (!warehousePointId.val()) {
                warehousePointId.error('Пункт склада должен быть указан')
            }
            if (!routeListID.val()) {
                routeListID.error('Маршрутный лист должен быть указан')
            }


            // If any error was reported, cancel the submission so it can be corrected
            if (this.inError()) {
                return false;
            } else {
                // console.log(JSON.stringify(data));
                // console.log(requestEditor.field('deliveryDate').input().val().toString());
                if (action == 'edit') {
                    data.status = 'editRequest';
                    data.requestIDExternal = dataTable.row($('#user-grid').find('.selected')).data().requestIDExternal;
                } else {
                    data.status = 'addRequest';
                }
                // data.status = 'addRequest';
            }
        }

    });

    requestEditor.on('create', function (e, json, data) {
        dataTable.draw(false);
    }).on('initEdit', function () {
        // console.log(JSON.stringify());
        if (dataTable.row($('#user-grid').find('.selected')).data().routeListNumber !== null){
            let routeList = JSON.stringify([{
                routeListId: "dummy",
                routeListNumber: dataTable.row($('#user-grid').find('.selected')).data().routeListNumber
            }]);
            // console.log(routeList);
            loadRouteLists(routeList);
            requestEditor.field('routeListID').inst().setValue("dummy");
        }

        if(dataTable.row($('#user-grid').find('.selected')).data().marketAgentUserId !== null){
            let marketAgentUser = requestEditor.field('marketAgentUserId').inst();

            marketAgentUser.clear();
            marketAgentUser.clearOptions();
            marketAgentUser.load(function (callback) {
                callback([{
                    "label": dataTable.row($('#user-grid').find('.selected')).data().marketAgentUserName,
                    "value": "dummy"
                }])
            });
            marketAgentUser.setValue("dummy");
        }

        if(dataTable.row($('#user-grid').find('.selected')).data().INN!==null){
            let client = requestEditor.field('clientID').inst();
            client.clear();
            client.clearOptions();
            client.load(function (callback) {
                callback([{
                    "label": dataTable.row($('#user-grid').find('.selected')).data().INN + "/" + dataTable.row($('#user-grid').find('.selected')).data().clientName,
                    "value": "dummy"
                }])
            });

            client.setValue("dummy");
        }

        if(dataTable.row($('#user-grid').find('.selected')).data().warehousePointName!==null){
            let warehousePoint = requestEditor.field('warehousePointId').inst();

            warehousePoint.clear();
            warehousePoint.clearOptions();
            warehousePoint.load(function (callback) {
                callback([{
                    "label": dataTable.row($('#user-grid').find('.selected')).data().warehousePointName,
                    "value": "dummy"
                }])
            });

            warehousePoint.setValue("dummy");
        }
    });


    // $.post("content/getData.php",
    //     {status: "getCompanies", format: "json"},
    //     function (companiesData) {
    //
    //         // var options = [];
    //         let selectizeOptions = [];
    //         companiesData = JSON.parse(companiesData);
    //
    //         companiesData.forEach(function (entry) {
    //
    //             // var option = "<option value=" + entry.id + ">" + entry.name + "</option>";
    //             // options.push(option);
    //             // console.log("id:"+entry.id+" name:"+entry.name+"\n");
    //             let selectizeOption = {text: entry.name, value: entry.id};
    //             selectizeOptions.push(selectizeOption);
    //         });
    //
    //         let driverInput = requestEditor.field('driverId');
    //         let vehicleInput = requestEditor.field('vehicleId');
    //         let transportCompanyInput = requestEditor.field('transportCompanyId');
    //         driverInput.input().on('change', function (e, d) {
    //             if (!$(this).val().length) return;
    //         });
    //         // $('#driverInput').selectize({
    //         //     sortField: "text",
    //         //     maxItems:1,
    //         //     onChange: function (value) {
    //         //         if (!value.length) return;
    //         //     }
    //         // });
    //         driverInput.inst().disable();
    //
    //
    //         vehicleInput.input().on('change', function () {
    //
    //             let value = $(this).val();
    //             // console.log(value);
    //             if (!value.length) return;
    //             driverInput.inst().disable();
    //             $.post(
    //                 'content/getData.php',
    //                 {
    //                     status: 'getDrivers',
    //                     vehicleId: Number(value)
    //                 }, function (driversData) {
    //                     let driversOptions = [];
    //                     // console.log(driversData);
    //                     driversData = JSON.parse(driversData);
    //                     driversData.forEach(function (entry) {
    //                         let selectizeOption = {text: entry.full_name, value: entry.id};
    //                         driversOptions.push(selectizeOption);
    //                     });
    //                     driverInput.inst().clear();
    //                     driverInput.inst().clearOptions();
    //                     // console.log(JSON.stringify(selectizeOptions));
    //                     driverInput.inst().load(function (callback) {
    //                         callback(driversOptions)
    //                     });
    //                     driverInput.inst().enable();
    //                 }
    //             )
    //         });
    //
    //         // $('#vehicleNumberInput').selectize({
    //         //     sortField: "text",
    //         //     maxItems:1,
    //         //     onChange: function(value){
    //         //         if (!value.length) return;
    //         //         driverInput.disable();
    //         //         $.post(
    //         //             'content/getData.php',
    //         //             {
    //         //                 status:'getDrivers',
    //         //                 vehicleId: Number(value)
    //         //             }, function (driversData) {
    //         //                 var driversOptions = [];
    //         //                 driversData = JSON.parse(driversData);
    //         //
    //         //                 driversData.forEach(function (entry) {
    //         //                     var selectizeOption = {text: entry.full_name, value: entry.id};
    //         //                     driversOptions.push(selectizeOption);
    //         //                 });
    //         //                 driverInput.clear();
    //         //                 driverInput.clearOptions();
    //         //                 driverInput.load(function(callback) {
    //         //                     callback(driversOptions)
    //         //                 });
    //         //                 driverInput.enable();
    //         //             })
    //         //
    //         //     }
    //         // });
    //         // var vehicleInput = $('#vehicleNumberInput')[0].selectize;
    //         vehicleInput.inst().disable();
    //
    //         transportCompanyInput.input().on('change', function () {
    //             let value = $(this).val();
    //             if (!value.length) return;
    //             vehicleInput.inst().disable();
    //             $.post(
    //                 'content/getData.php',
    //                 {
    //                     status: 'getVehicles',
    //                     companyId: Number(value)
    //                 }, function (vehiclesData) {
    //                     let vehicleOptions = [];
    //                     vehiclesData = JSON.parse(vehiclesData);
    //                     vehiclesData.forEach(function (entry) {
    //                         let selectizeOption = {text: entry.license_number + " / " + entry.model, value: entry.id};
    //                         vehicleOptions.push(selectizeOption);
    //                     });
    //                     vehicleInput.inst().clear();
    //                     vehicleInput.inst().clearOptions();
    //                     vehicleInput.inst().load(function (callback) {
    //                         callback(vehicleOptions)
    //                     });
    //                     vehicleInput.inst().enable();
    //                     driverInput.inst().clear();
    //                     driverInput.inst().clearOptions();
    //                     driverInput.inst().disable();
    //                 }
    //             )
    //         });
    //         // $('#companyInput').selectize({
    //         //     placeholder     : "Нажмите, чтобы изменить",
    //         //     sortField: "text",
    //         //     maxItems:1,
    //         //     onChange: function(value){
    //         //         if (!value.length) return;
    //         //         vehicleInput.disable();
    //         //         $.post(
    //         //             'content/getData.php',
    //         //             {
    //         //                 status:'getVehicles',
    //         //                 companyId: Number(value)
    //         //             }, function (vehiclesData) {
    //         //                 var vehicleOptions = [];
    //         //                 console.log(vehiclesData);
    //         //                 vehiclesData = JSON.parse(vehiclesData);
    //         //
    //         //                 vehiclesData.forEach(function (entry) {
    //         //                     var selectizeOption = {text: entry.license_number+" / "+entry.model, value: entry.id};
    //         //                     vehicleOptions.push(selectizeOption);
    //         //                 });
    //         //                 vehicleInput.clear();
    //         //                 vehicleInput.clearOptions();
    //         //                 vehicleInput.load(function(callback) {
    //         //                     callback(vehicleOptions)
    //         //                 });
    //         //                 vehicleInput.enable();
    //         //
    //         //                 driverInput.clear();
    //         //                 driverInput.clearOptions();
    //         //                 driverInput.disable();
    //         //             })
    //         //
    //         //     }
    //         // });
    //         // var transportCompanyInput = $('#companyInput')[0].selectize;
    //         // var clientSelectize = usersEditor.field('clientID').inst();
    //
    //         // transportCompanyInput.u();
    //         // transportCompanyInput.selectize.clearOptions();
    //         transportCompanyInput.inst().load(function (callback) {
    //             callback(selectizeOptions);
    //         });
    //     }
    // );


// --------DATATABLE INIT--------------
//noinspection JSJQueryEfficiency
    var dataTable = $('#user-grid').DataTable({

        processing: true,
        serverSide: true,
        colReorder: true,
        ScrollXInner: "100%",
        scrollY: "500px",
        scrollX: true,
        autoWidth: true,
        scrollCollapse: true,
        search: {
            caseInsensitive: true
        },
        order: [[2, "desc"]],
        //  jQueryUI: true,
        //   paging:         true,
        //  paginate: false,
        fixedColumns: {
            leftColumns: 2
        },
        // fixedHeader: {
        //  header: true,
        //   footer: false
        //  },
        stateSave: true,
        stateDuration: 0, // 0 a special value as it indicates that the state can be stored and retrieved indefinitely with no time limit
        // format for data object: https://datatables.net/reference/option/stateSaveCallback
        // save all but paging
        stateSaveParams: function (settings, data) {
            data.start = 0;
            data.length = dataTable.page.len();
        },
        pageLength: 40,
        select: {
            style: 'single'
        },
        dom: 'Brtip',
        language: {
            url: '/localization/dataTablesRus.json'
        },
        // Note that when language url parameter is set, DataTables' initialisation will be asynchronous due to the Ajax data load.
        // That is to say that the table will not be drawn until the Ajax request as completed.
        // As such, any actions that require the table to have completed its initialisation should be placed into the initComplete callback.
        initComplete: function (settings, json) {

            if (json['recordsFiltered'] == 0) {
                alert('Данных не найдено');
            }

            // ------SEARCH INPUTS-------------
            var state = dataTable.state();


            dataTable.columns().every(function () {

                var $footer = $(this.footer());

                var title = $footer.text();
                var searchInput = $('<input type="text" class="searchColumn" style="display: none; position: absolute" placeholder="Поиск ' + title + '" />');

                $("body").append(searchInput);

                var searchDiv = $('<div>');
                searchDiv.height(20);
                var search = $('<i class="fa fa-search">');
                searchDiv.html(search);
                $footer.html(searchDiv);

                searchDiv.on("click", function () {
                    // console.log("CLICKED");
                    var position = $(this).offset();
                    searchInput.offset(position);
                    searchInput.css({top: position.top, left: position.left, display: 'inline-block'});
                    searchInput.focus();
                });

                var that = this;
                // searchInput.on('keyup change', function (e) {
                //     var enterPressed = (e.keyCode == '13');
                //     if (enterPressed && (that.search() !== this.value)) {
                //         that
                //             .search(this.value)
                //             .draw();
                //         $(this).attr("currentFilter", this.value);
                //         searchInput.css({'display': 'none'});
                //     }
                // }).blur(function() {
                //     var filterValue = $(this).attr("currentFilter");
                //     $(this).val(filterValue);
                //     searchInput.css({'display': 'none'});
                // });

                searchInput.on('keyup change', function (e) {
                    var enterPressed = (e.keyCode == '13');
                    if ((that.search() !== this.value) && (enterPressed || localStorage.getItem('liveSearch') == 'true')) {
                        that
                            .search(this.value)
                            .draw();
                        $(this).attr("currentFilter", this.value);
                        // searchInput.css({'display': 'none'});
                    }
                }).blur(function () {
                    var filterValue = $(this).attr("currentFilter");
                    $(this).val(filterValue);
                    searchInput.css({'display': 'none'});
                    if (this.value != '') {

                        // $footer.css("background-color", "#c22929")
                        $footer.addClass("footer-search", 1000, "easeInBack");
                    } else {
                        $footer.removeClass("footer-search",1000, "easeInBack");
                    }
                });

                // manually load filters data into filter inputs
                var column = state.columns[this.index()];
                var historySearch = column.search.search;
                if (historySearch) {
                    searchInput.val(historySearch);
                    searchInput.attr("currentFilter", historySearch);
                    $footer.addClass("footer-search", 200, "easeInBack");
                    // $footer.css("background-color", "#c22929")
                }

            });


            //TODO fix it
            // if user role is CLIENT_MANAGER then delete 'изменить статус МЛ' button
            if ($("#userRoleContainer").html().trim() === "Пользователь_клиента") {
                dataTable.buttons(2).remove();
            }

            var role = $('#data-role').attr('data-role');
            if (localStorage.getItem(role_type) == undefined) {
                localStorage.setItem(role_type, role);
                // $.getDefaultColumns(dataTable, role);
            }
            else if (localStorage.getItem(role_type) != role) {
                localStorage.setItem(role_type, role);
                // $.getDefaultColumns(dataTable, role);
            }

            //link to map page
            if (role=="TRANSPORT_COMPANY"){
                $('.dropdown-content table tr:first').before('<tr>'+
                    '<td><i class="fa fa-building" aria-hidden="true"></i></td>'+
                    '<td><a href="/?TCPage=0" target="_blank">Транспортная компания</a></td>'+
                    '</tr>');
            }

            if (role=="ADMIN"||role=="MARKET_AGENT"){
                $('.dropdown-content table tr:first').before('<tr>'+
                    '<td><i class="fa fa-map-o" aria-hidden="true"></i></td>'+
                    '<td><a href="/?map=0" target="_blank">Карта присутствия</a></td>'+
                    '</tr>');
            }
            //Button-link to admin page
            if (role == "DISPATCHER" || role == "ADMIN") {

                $('.dropdown-content table tr:first').before('<tr>\n' +
                    '                            <td><i class="fa fa-cogs" aria-hidden="true"></i></td>\n' +
                    '                            <td><a href="/admin_page/" target="_blank">Админ. страница</a></td>\n' +
                    '                        </tr>');

                // dataTable.button().add(7, {
                //     text: 'Админ. Страница',
                //     action: function (e, dt, node, config) {
                //         window.location = "/admin_page"
                //     }
                // });
                if (role === "ADMIN"||role==="DISPATCHER") {


                    dataTable.button().add(8, {
                            extend: "create",
                            editor: requestEditor,
                            text: 'Создать'
                        }
                    );
                    dataTable.button().add(10, {
                            extend: 'edit',
                            text: 'Изменить',
                            editor: requestEditor
                        }
                    );
                    dataTable.button().add(9, {
                            extend: 'selectedSingle',
                            text: 'Удалить',
                            action: function (e, dt, node, config) {
                                if (!confirm("Вы действительно хотите удалить заявку?")) return;
                                $.post(
                                    "content/getData.php",
                                    {
                                        status: "deleteRequest",
                                        format: "json",
                                        requestIDExternal: dataTable.row($('#user-grid').find('.selected')).data().requestIDExternal
                                    },
                                    function (data) {
                                        dataTable.draw(false);
                                    });
                            }
                        }
                    );
                }
            }
        },
        buttons: [
            {
                text: 'Настройки таблицы',
                action: function (e, dt, node, config) {
                    $.showColumnSelectDialog(dataTable);
                }
            },
            {
                extend: 'selectedSingle',
                className: 'changeStatusForRequest',
                text: 'Изменить статус накладной',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForRequest", dataTable);
                }
            },
            {
                name: 'changeRouteListStatus',
                enabled: false,
                className: 'changeStatusForSeveralRequests',
                text: 'Изменить статус МЛ',
                action: function (e, dt, node, config) {
                    $.showRequestStatusDialog("changeStatusForSeveralRequests", dataTable);
                }
            },
            {
                className: 'statusHistory',
                text: 'История статусов',
                enabled: false,
                action: function (e, dt, node, config) {
                    var url =
                        "?clientId=" +
                        dataTable.row($('#user-grid .selected')).data().clientIDExternal +
                        "&invoiceNumber=" +
                        dataTable.row($('#user-grid .selected')).data().invoiceNumber;
                    url = encodeURI(url);

                    window.open(url);
                }
            },
            {
                extend: 'selectedSingle',
                className: 'documents',
                text: 'Документы',
                action: function (e, dt, node, config) {
                    var url =
                        "?reqIdExt=" +
                        dataTable.row($('#user-grid .selected')).data().requestIDExternal;
                    url = encodeURI(url);

                    window.open(url);
                }
            },
            {
                text: 'Сброс фильтров',
                action: function (e, dt, node, config) {
                    $('.searchColumn').each(function () {
                        $(this).val("").attr("currentFilter", "");
                    });
                    dataTable.columns().every(function () {

                        // $(this.footer()).css("background-color", "f6f6f6");
                        $(this.footer()).removeClass("footer-search",1000,"easeInBack");
                        this.search("");
                    });
                    dataTable.columns().draw();
                }
            },



            {
                text: (localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Стандартный поиск',

                action: function (e, dt, node, config) {

                    (localStorage.getItem("liveSearch") === 'true') ? localStorage.setItem("liveSearch", false) : localStorage.setItem("liveSearch", true);
                    this.text((localStorage.getItem("liveSearch") === 'true') ? 'Живой поиск' : 'Стандартный поиск');
                }
            }

        ],
        ajax: {
            contentType: 'application/json',
            processing: true,
            type: "post",
            url: "/data/matViewBigSelect", // json datasource
            data: function(d) {
                return JSON.stringify(d);
            }
            // type: "get",  // method  , by default get

            // data: {"status": "getRequestsForUser"},

            // success: function (data) {
            //     // console.log(data);
            //     // alert(JSON.stringify(data));
            // },
            // error: function () {  // error handling
            //     console.log("error");
            //     //$(".user-grid-error").html("");
            //     //$("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
            //     //$("#user-grid_processing").css("display", "none");
            //
            // }
        },
        columns: [
            {"data": "requestIDExternal"},
            {"data": "requestNumber"},
            {"data": "requestDate"},
            {"data": "invoiceNumber"},
            {"data": "invoiceDate"},
            {"data": "documentNumber"},
            {"data": "documentDate"},
            {"data": "firma"},
            {"data": "storage"},
            {"data": "commentForStatus"},
            {"data": "boxQty"},
            {"data": "requestStatusRusName"},
            {"data": "clientIDExternal"},
            {"data": "INN"},
            {"data": "clientName"},
            {"data": "marketAgentUserName"},
            {"data": "deliveryPointName"},
            {"data": "warehousePointName"},
            {"data": "lastVisitedPointName"},
            {"data": "nextPointName"},
            {"data": "routeName"},
            {"data": "driverUserName"},
            {"data": "licensePlate"},
            {"data": "palletsQty"},
            {"data": "routeListNumber"},
            {"data": "arrivalTimeToNextRoutePoint"},
            // {"data": "boxQty"},

        ],
        columnDefs: [
            {"name": "requestIDExternal", "searchable": true, "targets": 0},
            {"name": "requestNumber", "searchable": true, "targets": 1},
            {"name": "requestDate", "searchable": true, "targets": 2},
            {"name": "invoiceNumber", "searchable": true, "targets": 3},
            {"name": "invoiceDate", "searchable": true, "targets": 4},
            {"name": "documentNumber", "searchable": true, "targets": 5},
            {"name": "documentDate", "searchable": true, "targets": 6},
            {"name": "firma", "searchable": true, "targets": 7},
            {"name": "storage", "searchable": true, "targets": 8},
            {"name": "commentForStatus", "searchable": true, "targets": 9},
            {"name": "boxQty", "searchable": true, "targets": 10},
            {"name": "requestStatusRusName", "searchable": true, "targets": 11},
            {"name": "clientIDExternal", "searchable": true, "targets": 12},
            {"name": "INN", "searchable": true, "targets": 13},
            {"name": "clientName", "searchable": true, "targets": 14},
            {"name": "marketAgentUserName", "searchable": true, "targets": 15},
            {"name": "deliveryPointName", "searchable": true, "targets": 16},
            {"name": "warehousePointName", "searchable": true, "targets": 17},
            {"name": "lastVisitedPointName", "searchable": true, "targets": 18},
            {"name": "nextPointName", "searchable": true, "targets": 19},
            {"name": "routeName", "searchable": true, "targets": 20},
            {"name": "driverUserName", "searchable": true, "targets": 21},
            {"name": "licensePlate", "searchable": true, "targets": 22},
            {"name": "palletsQty", "searchable": true, "targets": 23},
            {"name": "routeListNumber", "searchable": true, "targets": 24},
            {"name": "arrivalTimeToNextRoutePoint", "searchable": true, "targets": 25},
            // {"name": "boxQty", "searchable": true, "targets": 26},
            {"className": "dt-center", "targets": 10}

        ],
    });
//Opens request status dialog by double click on a row
    $('#user-grid tbody').on('dblclick', 'tr', function () {
        $.showRequestStatusDialog("changeStatusForRequest", dataTable);
    } );

// set padding for dataTable
    $('#user-grid_wrapper').css('padding-top', '40px');
    $(".dataTables_scrollHeadInner").css({"width": "100%"});
    $(".dataTable ").css({"width": "100%"});
    var disabled = 0;
//var buttons = dataTable.buttons(['.changeStatusForRequest', '.changeStatusForSeveralRequests', '.statusHistory']);
    dataTable.on('deselect', function (e, dt, type, indexes) {
        dataTable.button(2).disable();
        dataTable.button(3).disable();
    });
    dataTable.on('select', function (e, dt, type, indexes) {


        var routeListID = dataTable.row($('#user-grid .selected')).data().routeListNumber;
        var invoiceNumber = dataTable.row($('#user-grid .selected')).data().invoiceNumber.trim();
        var clientId = dataTable.row($('#user-grid .selected')).data().clientIDExternal.trim();
        // console.log(invoiceNumber+'\n'+clientId);
        // console.log(routeListID+' \n'+$("#data-role").html().trim());
        if ((routeListID == null) || $("#data-role").html().trim()=="Пользователь_клиента"){
            dataTable.button(2).disable();
            // console.log('disabled');
        } else if (routeListID!=null && $("#data-role").html().trim()!="Пользователь_клиента"){
            dataTable.button(2).enable();
            // console.log('enabled');
        }

        if(clientId!='' && invoiceNumber!=''){
            dataTable.button(3).enable();
        } else {
            dataTable.button(3).disable();
        }
    });

});
