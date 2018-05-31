$(document).ready(function () {
    var driversEditor = new $.fn.dataTable.Editor({
        ajax: {
            create:{
                type:'POST',
                contentType:'application/json',
                url: 'api/drivers',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    driversDataTable.draw("page");
                    driversEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType: 'application/json',
                type: 'PATCH',
                url: 'api/drivers/_id_',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        if(value.vehicle=="") delete value.vehicle;
                        if(value.transportCompany=="") delete value.transportCompany;
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    driversDataTable.draw("page");
                    driversEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/drivers/_id_',
                data: function (d) {
                    return '';
                },

            }
        },
        table: '#driversTable',
        idSrc: 'id',
        fields: [
            {label: 'Транспортное Средство', name: 'vehicle', type: 'selectize',
                options: [], opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
            }
            },
            {label: 'Транспортная компания', name: 'transportCompany', type: 'selectize',
                options: [], opts: {
                diacritics: true,
                searchField: 'label',
                labelField: 'label',
                dropdownParent: "body"
            }
            },
            {label: 'Полное имя', name: 'fullName', type: 'text'},
            {label: 'Паспорт', name: 'passport', type: 'text'},
            {
                label: 'Номер телефона',
                name: 'phone',
                type: 'mask',
                mask: "(000) 000-00-00",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "(999) 999-99-99"
            },
            {label: 'Лицензия', name: 'license', type: 'text'}
        ]
    });


    driversEditor.on('postRemove', function (e, data, action) {
        $("#vehiclesTable").DataTable().columns().draw();
    });

    driversDataTable = $("#driversTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            contentType: 'application/json',
            processing: true,
            url: "data/drivers", // json datasource
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
                editor: driversEditor
            },
            {
                extend: "edit",
                editor: driversEditor
            },
            {
                extend: "remove",
                editor: driversEditor
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0, visible: false},
            {"name": "vehicleLincenseNumber", "data": "vehicle.licenseNumber", "targets": 1},
            {"name": "transportCompanyName", "data": "transportCompany.name", "targets": 2},
            {"name": "fullName", "data": "fullName", "targets": 3},
            {"name": "passport", "data": "passport", "targets": 4},
            {"name": "phone", "data": "phone", "targets": 5},
            {"name": "license", "data": "license", "targets": 6}
        ]
    });


    driversEditor.field('transportCompany').input().on('keyup', function (e, d) {
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

                var selectize = driversEditor.field('transportCompany').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(transportCompanyOptions);
                });
            }
        );
    });

    driversEditor.field('vehicle').input().on('keyup', function (e, d) {
        var namePart = $(this).val();
        $.get( "api/vehicles/search/findTop10ByLicenseNumberContaining/?licenseNumber="+namePart,
            function (data) {
                console.log(data);
                var vehicleOptions = [];
                data._embedded.vehicles.forEach(function (entry) {
                    var selectizeOption = {"label": entry.licenseNumber+"/"+entry.model, "value": entry._links.self.href};
                    vehicleOptions.push(selectizeOption);
                });

                var selectize = driversEditor.field('vehicle').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(vehicleOptions);
                });
            }
        );
    });
});