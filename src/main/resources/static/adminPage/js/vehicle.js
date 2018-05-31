$(document).ready(function () {
    let vehiclesEditor = new $.fn.dataTable.Editor({
        ajax: {
            create:{
                type:'POST',
                contentType:'application/json',
                url: 'api/vehicles',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    vehiclesTable.draw("page");
                    vehiclesEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType: 'application/json',
                type: 'PATCH',
                url: 'api/vehicles/_id_',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        if(value.transportCompany=="") delete value.transportCompany;
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    vehiclesTable.draw("page");
                    vehiclesEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/vehicles/_id_',
                data: function (d) {
                    return '';
                },

            }
        },
        table: '#vehiclesTable',
        idSrc: 'id',
        fields: [
            {
                label: 'Транспортная компания', name: 'transportCompany', type: 'selectize',
                options: [], opts: {
                diacritics: true,
                searchField: 'label',
                labelField: 'label',
                dropdownParent: "body"
            }
            },
            {label: 'Номер лицензии', name: 'licenseNumber', type: 'text'},
            {label: 'Модель', name: 'model', type: 'text'},
            {label: 'Грузоподъемность, кг.', name: 'carryingCapacity', type: 'text'},
            {label: 'Объем, м<sup>3</sup>', name: 'volume', type: 'text'},
            {
                label: 'Тип погрузки',
                name: 'loadingType',
                type: 'selectize',
                options: [{label: "Задняя", value: "BACK"}, {label: "Верхняя", value: "TOP"}, {
                    label: "Боковая",
                    value: "SIDE"
                }]
            },
            {label: 'Количество паллет', name: 'palletsQuantity', type: 'mask', mask: "#"},
            {
                label: 'Тип ТС',
                name: 'type',
                type: 'selectize',
                options: [{label: "Тент", value: "TENT"}, {label: "Термос", value: "THERMOS"}, {
                    label: "Рефрижератор",
                    value: "REFRIGERATOR"
                }]
            },
            {label: 'Wialon ID', name: 'wialonId', type: 'text'}
            ,
            {
                label: 'Принадлежность',
                name: 'isRented',
                type: 'selectize',
                options: [{label: "Наемная", value: 1}, {label: "Собственная", value: 0}]
            }
        ]
    });


    loadingType = {
        "TOP":"Верхняя",
        "SIDE": "Боковая",
        "BACK": "Задняя",
        null:""
    };
    vehicleType = {
        "TENT":"Тент",
        "THERMOS":"Термос",
        "REFRIGERATOR":"Рефрижератор",
        null:""
    };

    let vehiclesTable = $("#vehiclesTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            contentType: 'application/json',
            processing: true,
            url: "data/vehicles", // json datasource
            type: "post",  // method  , by default get
            data: function(d) {
                return JSON.stringify(d);
            },
        },
        dom: 'Bfrtip',
        language: {
            url: '/localization/dataTablesRus.json'
        },
        select: {
            style: 'single'
        },
        "buttons": [
            {
                extend: "create",
                editor: vehiclesEditor
            },
            {
                extend: "edit",
                editor: vehiclesEditor
            },
            {
                extend: "remove",
                editor: vehiclesEditor,
                formMessage: function (e, dt) {
                    return "Вы уверены, что вы хотите удалить это ТС?</br> Все водители, привязанные к этому ТС, так же будут удалены."
                }
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0},
            {"name": "transportCompanyName", "data": "transportCompany.name", "targets": 1},
            {"name": "licenseNumber", "data": "licenseNumber", "targets": 2},
            {"name": "model", "data": "model", "targets": 3},
            {"name": "carryingCapacity", "data": "carryingCapacity", "targets": 4},
            {"name": "volume", "data": "volume", "targets": 5},
            {"name": "loadingType", "data": "loadingType", "targets": 6,
                "render": function ( data, type, row ) {
                    return loadingType[data];
                },
            },
            {"name": "palletsQuantity", "data": "palletsQuantity", "targets": 7},
            {"name": "type", "data": "type", "targets": 8,
                "render": function ( data, type, row ) {
                    return vehicleType[data];
                },
            },
            {"name": "wialonId", "data": "wialonId", "targets": 9},
            {"name": "isRented", "data": "isRented", "targets": 10,
                "render": function ( data, type, row ) {
                    return data=="1" ? "Наемная" : "Собственная";
                },}
        ]
    });



    vehiclesEditor.field('transportCompany').input().on('keyup', function (e, d) {
        var namePart = $(this).val();
        $.get( "api/transportCompanies/search/findTop10ByNameContaining/?companyName="+namePart,
            function (data) {
                console.log(data);
                var transportCompanyOptions = [];
                // data = JSON.parse(data);
                data._embedded.transportCompanies.forEach(function (entry) {
                    var selectizeOption = {"label": entry.name+"/"+entry.shortName, "value": entry._links.self.href};
                    transportCompanyOptions.push(selectizeOption);
                });

                var selectize = vehiclesEditor.field('transportCompany').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(transportCompanyOptions);
                });
            }
        );
    });




});