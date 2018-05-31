$(document).ready(function () {

    var clientEditor = new $.fn.dataTable.Editor({
        ajax: {
            create:{
                type:'POST',
                contentType:'application/json',
                url: 'api/clients',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                }
            },
            edit: {
                contentType: 'application/json',
                type: 'PATCH',
                url: 'api/clients/_id_',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    clientsDataTable.draw("page");
                    clientEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/clients/_id_',
                data: function (d) {
                    return '';
                },
                success: function (response) {
                    clientsDataTable.draw("page");
                    clientEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            }
        },
        contentType: 'application/json',
        table: '#clientsTable',
        idSrc: 'clientID',


        fields: [
            // {label: 'clientID', name: 'clientID', type: 'text'},
            {label: 'Имя', name: 'clientName', type: 'text'},
            {label: 'ИНН', name: 'inn', type: 'text'},
            {label: 'КПП', name: 'kpp', type: 'text'},
            {label: 'Кор. Счет', name: 'corAccount', type: 'text'},
            {label: 'Кур. Счет', name: 'curAccount', type: 'text'},
            {label: 'БИК', name: 'bik', type: 'text'},
            {label: 'Номер контракта', name: 'contractNumber', type: 'text'}
        ]
    });


    // BIK: "34896208375"
    // INN: "1234567890"
    // KPP: "23674529375734562"
    // clientID: "1"
    // clientName: "ИП Иванов"
    // contractNumber: "erguheru"
    // corAccount: "corAcccc"
    // curAccount: "curAxccccc"

    // set current selected value to pointName and userRoleRusName




    var dataExample = {
        data: [
            {
                driverId: 1,
                driverName: "Вася",
                driverPhone: "88005553535",
                driverfgsfds: "Проще позвонить, чем у кого-то занимать"
            },
            {
                driverId: 2,
                driverName: "петя",
                driverPhone: "88005553535",
                driverfgsfds: "Проще позвонить, чем у кого-то занимать"
            }
            ]
    };

    var clientsDataTable = $("#clientsTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                contentType: 'application/json',
                processing: true,
                url: "data/clients", // json datasource
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
                    editor: clientEditor,
                    text: 'Добавить клиента'
                },
                {
                    extend: "edit",
                    editor: clientEditor,
                    text: "Изменить"
                },
                {
                    extend: "remove",
                    editor: clientEditor,
                    text: 'Удалить клиента'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "clientID", "data": "clientID", "targets": 0, visible: false},
                {"name": "clientName", "data": "clientName", "targets": 1},
                {"name": "inn", "data": "inn", "targets": 2},
                {"name": "kpp", "data": "kpp", "targets": 3},
                {"name": "corAccount", "data": "corAccount", "targets": 4},
                {"name": "curAccount", "data": "curAccount", "targets": 5},
                {"name": "bik", "data": "bik", "targets": 6},
                {"name": "contractNumber", "data": "contractNumber", "targets": 7}
            ]
        }
    );
});
