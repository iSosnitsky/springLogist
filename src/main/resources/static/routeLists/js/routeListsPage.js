$(document).ready(function () {
    var freightAssigner = new $.fn.dataTable.Editor({
            ajax: "content/getData.php",
            table: "#routeListsTable",
            idSrc: 'routeListID',
            fields:
                [
                    {
                        label: "Рейс:",
                        name: "freightId", type: 'selectize', options: [],
                        opts: {
                            diacritics: true,
                            searchField: 'text',
                            labelField: 'text',
                            dropdownParent: "body"
                        }
                    }
                ]
        }
    );


    var freightCreator = new $.fn.dataTable.Editor({
        ajax: "content/getData.php",
        table: "#routeListsTable",
        idSrc: 'routeListID',
        fields:
            [
                {
                    label: "Номер рейса:",
                    name: "freightNumber"
                },
                // {label: 'Номер ТК', name: 'routeListIDExternal', type: 'text'},
                // {label: 'Номер ТК', name: 'routeListNumber', type: 'text'},
                // {label: 'Номер ТК', name: 'dataSourceName', type: 'text'},
                // {label: 'Номер ТК', name: 'routeListStatusName', type: 'text'},
                // {label: 'Номер ТК', name: 'departureDate', type: 'text'},
                // {label: 'Номер ТК', name: 'creationDate', type: 'text'},
                // {label: 'Номер ТК', name: 'routeListNumber', type: 'text'},
                {
                    label: 'Номер ТК', name: 'transportCompanyId', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Транспортное средство №1', name: 'vehicleId', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Транспортное средство №2', name: 'vehicle2Id', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Транспортное средство №3', name: 'vehicle3Id', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Водитель', name: 'driverId', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {
                    label: 'Маршрут', name: 'routeId', type: 'selectize',
                    options: [],
                    opts: {
                        diacritics: true,
                        searchField: 'text',
                        labelField: 'text',
                        dropdownParent: "body"
                    }
                },
                {label: 'Фактическая протяженность<sup>КМ</sup>', name: 'distanceKM', type: 'mask', mask: '#'},
                {label: 'Фактическая длительность<sup>Ч</sup>', name: 'continuanceH', type: 'mask', mask: '#'},
                {label: 'Фактический расход топлива<sup>Л</sup>', name: 'fuelConsumption', type: 'mask', mask: '#'},
                {label: 'Часы простоя <sup>Ч</sup>', name: 'stallHours', type: 'mask', mask: '#'},
                {
                    label: 'Показания спидометра на конец поездки<sup>КМ/Ч</sup>',
                    name: 'speedReadingsEnd',
                    type: 'mask',
                    mask: '#'
                },
            ]
    });

    freightCreator.on('preSubmit', function (e, data, action) {
        if (action == 'edit') {
            if (!this.field("routeId").val()) {
                console.log("fgsfds");
                this.field("routeId").error('Маршрут должен быть указан');
                return false;
            } else {
                data.status = 'uniteRouteLists';
            }
        }
    });

    freightAssigner.on('preSubmit', function (e, data, action) {
        if (action === 'edit') {
            if (!this.field("freightId").val()) {
                console.log("fgsfds");
                this.field("freightId").error('Рейс должен быть указан');
                return false;
            } else {
                data.status = 'assignFreightToRouteLists';
            }
        }
    });


    var dataTable = $('#routeListsTable').DataTable({
        processing: true,
        serverSide: true,

        search: {
            caseInsensitive: true
        },
        fixedColumns: {
            leftColumns: 1
        },
        ajax: {
            // url: "content/getData.php", // json datasource
            processing: true,
            contentType: "application/json",
            url: "http://localhost:8080/data/routeLists", // json datasource
            type: "post",  // method  , by default get
            data: function(d) {
                return JSON.stringify(d);
            },
            // success: function (data) {
            //     console.log(data);
            // }
            // data: {"status": "getRouteListsData"}
        },
        columnDefs: [
            // {"name": "routeListIDExternal", "data": "routeListIDExternal", "targets": 0},
            {"name": "routeListIdExternal", "data": "routeListIdExternal", "targets": 0, "render":
                function ( data, type, row, meta ) {
                return '<a href="?routeListHistory='+data+'">'+data+'</a>';
            }},
            {"name": "routeListNumber", "data": "routeListNumber", "targets": 1},
            {"name": "dataSourceName", "data": "dataSourceId", "targets": 2, "render": {"REQUESTS_ASSIGNER":"Создано автоматически","LOGIST_1C":"Создано из 1С", "ADMIN_PAGE":"Создано из системы"}},
            // {"name": "dataSourceName", "data": "dataSourceId", "targets": 3},
            {"name": "routeListStatusRusName", "data": "status.routeListStatusRusName", "targets": 3},
            {"name": "departureDate", "data": "departureDate", "targets": 4},
            {"name": "creationDate", "data": "creationDate", "targets": 5},
            {"name": "fgsfds", "data": "freight.freightNumber", "targets": 6, "defaultContent":"Отсутствует"},
            {"name": "routeId.routeName" , "data" : "routeId.routeName","targets": 7}
            // {"name": "route_list_id", "data": "route_list_id"}
            // {"name": "routeListNumber", "data": "routeListNumber", "targets": 4, visible: false},
            // {"name": "boxQty", "data": "boxQty", "targets": 5, visible: false}
        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        // responsive: true,
        select: {
            style: 'multi'
        },
        buttons: [
            {

                text: "На главную",
                action: function (e, dt, node, config) {
                    var url = "index.php";
                    url = encodeURI(url);
                    window.open(url);
                }
            },
            {

                text: "История статусов",
                extend: 'selectedSingle',
                action: function (e, dt, node, config) {
                    var url =
                        "?routeListHistory=" + dataTable.row($('.selected')).data().routeListIdExternal;
                    url = encodeURI(url);
                    window.open(url);
                }
            },
            {

                text: "Заявки на этом МЛ",
                extend: 'selectedSingle',
                action: function (e, dt, node, config) {
                    var url =
                        "?routeListId=" +
                        dataTable.row($('.selected')).data().routeListID;
                    url = encodeURI(url);
                    window.open(url);
                }
            },
            {
                text: "Добавить в рейс",
                extend: 'edit',
                editor: freightAssigner
            },
            {

                text: "Объеденить в новый рейс",
                extend: 'edit',
                editor: freightCreator
                // action: function (e, dt, node, config) {
                //     console.log(dataTable.rows('.selected').data().map(x => x.routeListID));
                //     $.post()
                //     // alert( table.rows.length +' row(s) selected' );
                //
                //     // var url =
                //     //     "?routeListId=" +
                //     //     dataTable.row($('.selected')).data().routeListID;
                //     // url = encodeURI(url);
                //     // window.open(url);
                // }
            }

        ]
    });



    $.post("content/getData.php",
        {status: "getCompanies", format: "json"},
        function (companiesData) {

            // var options = [];
            let selectizeOptions = [];
            companiesData = JSON.parse(companiesData);

            companiesData.forEach(function (entry) {

                // var option = "<option value=" + entry.id + ">" + entry.name + "</option>";
                // options.push(option);
                // console.log("id:"+entry.id+" name:"+entry.name+"\n");
                let selectizeOption = {text: entry.name, value: entry.id};
                selectizeOptions.push(selectizeOption);
            });

            let driverInput = freightCreator.field('driverId');
            let vehicleInput1 = freightCreator.field('vehicleId');
            let vehicleInput2 = freightCreator.field('vehicle2Id');
            let vehicleInput3 = freightCreator.field('vehicle3Id');
            let transportCompanyInput = freightCreator.field('transportCompanyId');
            driverInput.input().on('change', function (e, d) {
                if (!$(this).val().length) return;
            });
            // $('#driverInput').selectize({
            //     sortField: "text",
            //     maxItems:1,
            //     onChange: function (value) {
            //         if (!value.length) return;
            //     }
            // });
            driverInput.inst().disable();


            vehicleInput1.input().on('change', function () {

                let value = $(this).val();
                // console.log(value);
                if (!value.length) return;
                driverInput.inst().disable();
                $.post(
                    'content/getData.php',
                    {
                        status: 'getDrivers',
                        vehicleId: Number(value)
                    }, function (driversData) {
                        let driversOptions = [];
                        // console.log(driversData);
                        driversData = JSON.parse(driversData);
                        driversData.forEach(function (entry) {
                            let selectizeOption = {text: entry.full_name, value: entry.id};
                            driversOptions.push(selectizeOption);
                        });
                        driverInput.inst().clear();
                        driverInput.inst().clearOptions();
                        // console.log(JSON.stringify(selectizeOptions));
                        driverInput.inst().load(function (callback) {
                            callback(driversOptions)
                        });
                        driverInput.inst().enable();
                    }
                )
            });

            // $('#vehicleNumberInput').selectize({
            //     sortField: "text",
            //     maxItems:1,
            //     onChange: function(value){
            //         if (!value.length) return;
            //         driverInput.disable();
            //         $.post(
            //             'content/getData.php',
            //             {
            //                 status:'getDrivers',
            //                 vehicleId: Number(value)
            //             }, function (driversData) {
            //                 var driversOptions = [];
            //                 driversData = JSON.parse(driversData);
            //
            //                 driversData.forEach(function (entry) {
            //                     var selectizeOption = {text: entry.full_name, value: entry.id};
            //                     driversOptions.push(selectizeOption);
            //                 });
            //                 driverInput.clear();
            //                 driverInput.clearOptions();
            //                 driverInput.load(function(callback) {
            //                     callback(driversOptions)
            //                 });
            //                 driverInput.enable();
            //             })
            //
            //     }
            // });
            // var vehicleInput = $('#vehicleNumberInput')[0].selectize;
            vehicleInput1.inst().disable();
            vehicleInput2.inst().disable();
            vehicleInput3.inst().disable();

            transportCompanyInput.input().on('change', function () {
                let value = $(this).val();
                if (!value.length) return;
                vehicleInput1.inst().disable();
                vehicleInput2.inst().disable();
                vehicleInput3.inst().disable();
                $.post(
                    'content/getData.php',
                    {
                        status: 'getVehicles',
                        companyId: Number(value)
                    }, function (vehiclesData) {
                        let vehicleOptions = [];
                        vehiclesData = JSON.parse(vehiclesData);
                        vehiclesData.forEach(function (entry) {
                            let selectizeOption = {text: entry.license_number + " / " + entry.model, value: entry.id};
                            vehicleOptions.push(selectizeOption);
                        });
                        vehicleInput1.inst().clear();
                        vehicleInput1.inst().clearOptions();
                        vehicleInput1.inst().load(function (callback) {
                            callback(vehicleOptions)
                        });
                        vehicleInput2.inst().clear();
                        vehicleInput2.inst().clearOptions();
                        vehicleInput2.inst().load(function (callback) {
                            callback(vehicleOptions)
                        });
                        vehicleInput3.inst().clear();
                        vehicleInput3.inst().clearOptions();
                        vehicleInput3.inst().load(function (callback) {
                            callback(vehicleOptions)
                        });
                        vehicleInput1.inst().enable();
                        vehicleInput2.inst().enable();
                        vehicleInput3.inst().enable();
                        driverInput.inst().clear();
                        driverInput.inst().clearOptions();
                        driverInput.inst().disable();
                    }
                )
            });
            // $('#companyInput').selectize({
            //     placeholder     : "Нажмите, чтобы изменить",
            //     sortField: "text",
            //     maxItems:1,
            //     onChange: function(value){
            //         if (!value.length) return;
            //         vehicleInput.disable();
            //         $.post(
            //             'content/getData.php',
            //             {
            //                 status:'getVehicles',
            //                 companyId: Number(value)
            //             }, function (vehiclesData) {
            //                 var vehicleOptions = [];
            //                 console.log(vehiclesData);
            //                 vehiclesData = JSON.parse(vehiclesData);
            //
            //                 vehiclesData.forEach(function (entry) {
            //                     var selectizeOption = {text: entry.license_number+" / "+entry.model, value: entry.id};
            //                     vehicleOptions.push(selectizeOption);
            //                 });
            //                 vehicleInput.clear();
            //                 vehicleInput.clearOptions();
            //                 vehicleInput.load(function(callback) {
            //                     callback(vehicleOptions)
            //                 });
            //                 vehicleInput.enable();
            //
            //                 driverInput.clear();
            //                 driverInput.clearOptions();
            //                 driverInput.disable();
            //             })
            //
            //     }
            // });
            // var transportCompanyInput = $('#companyInput')[0].selectize;
            // var clientSelectize = usersEditor.field('clientID').inst();

            // transportCompanyInput.u();
            // transportCompanyInput.selectize.clearOptions();
            transportCompanyInput.inst().load(function (callback) {
                callback(selectizeOptions);
            });
        }
    );

    $.post("content/getData.php",{
        status: "getAllFreightIdPairs", format: "json"
    }, function (data) {
        let options = [];
        console.log(data);
        data = JSON.parse(data);
        data.forEach(function (entry) {
            let selectizeOption = {text: entry.text, value: entry.freight_id};
            options.push(selectizeOption);
        });
        freightAssigner.field('freightId').inst().load(function (callback) {
            callback(options)
        });
    });



    $.post(
        "content/getData.php",
        {
            status: "getAllRouteIdDirectionPairs", format: "json"
        },
        function (data) {
            // console.log(data);
            let options = [];
            console.log(data);
            data = JSON.parse(data);
            data.forEach(function (entry) {
                let selectizeOption = {text: entry.directionName, value: entry.routeID};
                options.push(selectizeOption);
            });
            freightCreator.field('routeId').inst().load(function (callback) {
                callback(options)
            });

        }
    );


});