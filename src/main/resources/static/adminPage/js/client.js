$(document).ready(function () {

    var clientEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#clientsTable',
        idSrc: 'clientID',

        fields: [
            // {label: 'clientID', name: 'clientID', type: 'text'},
            {label: 'Имя', name: 'clientName', type: 'text'},
            {label: 'ИНН', name: 'INN', type: 'text'},
            {label: 'КПП', name: 'KPP', type: 'text'},
            {label: 'Кор. Счет', name: 'corAccount', type: 'text'},
            {label: 'Кур. Счет', name: 'curAccount', type: 'text'},
            {label: 'БИК', name: 'BIK', type: 'text'},
            {label: 'Номер контракта', name: 'contractNumber', type: 'text'}
        ]
    });

    clientEditor.on('preSubmit', function (e, data, action) {
        data.status = 'clientEditing';
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


    // example data for exchange with server
    //var exampleData = [{userID: 1, userName:"wefwfe", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
    //    email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRoleRusName:"Диспетчер", pointName:"point1"}];
    $.post('content/getData.php', {
        'status': 'getClientsData'
    }, function (data) {
        console.log(JSON.parse(data));
    });

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

    var $clientsDataTable = $("#clientsTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getClientsData"}
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
                {"name": "INN", "data": "INN", "targets": 2},
                {"name": "KPP", "data": "KPP", "targets": 3},
                {"name": "corAccount", "data": "corAccount", "targets": 4},
                {"name": "curAccount", "data": "curAccount", "targets": 5},
                {"name": "BIK", "data": "BIK", "targets": 6},
                {"name": "contractNumber", "data": "contractNumber", "targets": 7}
            ]
        }
    );
});
