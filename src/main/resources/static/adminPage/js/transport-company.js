$(document).ready(function () {
    var transportCompaniesEditor = new $.fn.dataTable.Editor( {
        ajax: 'content/getData.php',
        table: '#transportCompaniesTable',
        idSrc: 'id',

        fields: [
            { label: 'Полное название', name: 'name', type: 'text'},
            { label: 'Название', name: 'short_name', type: 'text', fieldInfo: "Не рекомендуется вводить сокращения ООО, ИП и т.д."},
            {
                label: 'ИНН',
                name: 'inn',
                type: 'mask',
                mask: "0000000000",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "1234567890"
            },
            {
                label: 'КПП',
                name: 'KPP',
                type: 'mask',
                mask: "000000000",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "123456789"
            },
            {
                label: 'БИК',
                name: 'BIK',
                type: 'mask',
                mask: "000000000",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "123456789"
            },
            { label: 'Корр. счет',  name: 'cor_account', type: 'text'},
            { label: 'Р/с',  name: 'cur_account', type: 'text'},
            { label: 'Название банка',  name: 'bank_name', type: 'text'},
            { label: 'Адрес',  name: 'legal_address', type: 'text'},
            { label: 'Почтовый адрес',  name: 'post_address', type: 'text'},
            { label: 'keywords',  name: 'keywords', type: 'text'},
            { label: 'ФИО Директора',  name: 'director_fullname', type: 'text'},
            { label: 'ФИО глав.бухгалтера',  name: 'chief_acc_fullname', type: 'text'}
        ]
    });

    transportCompaniesEditor.on('preSubmit', function (e, data, action) {
        data.status = 'transportCompaniesEditing';
    });

    transportCompaniesEditor.on('postRemove', function (e, data, action) {
        $("#vehiclesTable").DataTable().columns().draw();
        $("#driversTable").DataTable().columns().draw();
    });

    var $transportCompaniesTable =  $("#transportCompaniesTable").DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getTransportCompaniesData"}
        },
        dom: 'Bfrtip',
        language: {
            url:'/localization/dataTablesRus.json'
        },
        select: {
            style: 'single'
        },
        "buttons": [
            {
                extend: "create",
                editor: transportCompaniesEditor,
                text: 'добавить запись'
            },
            {
                extend: "edit",
                editor: transportCompaniesEditor,
                text: 'изменить запись'
            },
            {
                extend: "remove",
                editor: transportCompaniesEditor,
                text: 'удалить запись',
                formMessage: function (e, dt) {
                    return "Вы уверены, что вы хотите удалить эту компанию?</br> Все ТС и водители, привязанные к данной компании, так же будут удалены."
                }
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0},
            {"name": "name", "data": "name", "targets": 1},
            {"name": "short_name", "data": "short_name", "targets": 2},
            {"name": "inn", "data": "inn", "targets": 3},
            {"name": "KPP", "data": "KPP", "targets": 4},
            {"name": "BIK", "data": "BIK", "targets": 5},
            {"name": "cor_account", "data": "cor_account", "targets": 6},
            {"name": "cur_account", "data": "cur_account", "targets": 7},
            {"name": "bank_name", "data": "bank_name", "targets": 8},
            {"name": "legal_address", "data": "legal_address", "targets": 9},
            {"name": "post_address", "data": "post_address", "targets": 10},
            {"name": "keywords", "data": "keywords", "targets": 11},
            {"name": "director_fullname", "data": "director_fullname", "targets": 12},
            {"name": "chief_acc_fullname", "data": "chief_acc_fullname", "targets": 13}
        ]
    });
});