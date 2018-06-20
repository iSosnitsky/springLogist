$(document).ready(function () {
    var transportCompaniesEditor = new $.fn.dataTable.Editor( {
        ajax: {
            create:{
                type:'POST',
                contentType:'application/json',
                url: 'api/transportCompanies',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    transportCompaniesTable.draw("page");
                    transportCompaniesEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType: 'application/json',
                type: 'PATCH',
                url: 'api/transportCompanies/_id_',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    transportCompaniesTable.draw("page");
                    transportCompaniesEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/transportCompanies/_id_',
                data: function (d) {
                    return '';
                }
            }
        },
        table: '#transportCompaniesTable',
        idSrc: 'id',


        fields: [
            { label: 'Полное название', name: 'name', type: 'text'},
            { label: 'Название', name: 'shortName', type: 'text', fieldInfo: "Не рекомендуется вводить сокращения ООО, ИП и т.д."},
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
                name: 'kpp',
                type: 'mask',
                mask: "000000000",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "123456789"
            },
            {
                label: 'БИК',
                name: 'bik',
                type: 'mask',
                mask: "000000000",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "123456789"
            },
            { label: 'Корр. счет',  name: 'corAccount', type: 'text'},
            { label: 'Р/с',  name: 'curAccount', type: 'text'},
            { label: 'Название банка',  name: 'bankName', type: 'text'},
            { label: 'Адрес',  name: 'legalAddress', type: 'text'},
            { label: 'Почтовый адрес',  name: 'postAddress', type: 'text'},
            { label: 'keywords',  name: 'keywords', type: 'text'},
            { label: 'ФИО Директора',  name: 'directorFullname', type: 'text'},
            { label: 'ФИО глав.бухгалтера',  name: 'chiefAccFullname', type: 'text'}
        ]
    });

    transportCompaniesEditor.on('postRemove', function (e, data, action) {
        $("#vehiclesTable").DataTable().columns().draw();
        $("#driversTable").DataTable().columns().draw();
    });

    var transportCompaniesTable =  $("#transportCompaniesTable").DataTable({
        processing: true,
        searchDelay: 800,
        serverSide: true,
        ajax: {
            contentType: 'application/json',
            processing: true,
            url: "data/transportCompanies", // json datasource
            type: "post",  // method  , by default get
            data: function(d) {
                return JSON.stringify(d);
            },
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
                editor: transportCompaniesEditor
            },
            {
                extend: "edit",
                editor: transportCompaniesEditor
            },
            {
                extend: "remove",
                editor: transportCompaniesEditor,
                formMessage: function (e, dt) {
                    return "Вы уверены, что вы хотите удалить эту компанию?</br> Все ТС и водители, привязанные к данной компании, так же будут удалены."
                }
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0, visible: false},
            {"name": "name", "data": "name", "targets": 1},
            {"name": "shortName", "data": "shortName", "targets": 2},
            {"name": "inn", "data": "inn", "targets": 3},
            {"name": "kpp", "data": "kpp", "targets": 4},
            {"name": "bik", "data": "bik", "targets": 5},
            {"name": "corAccount", "data": "corAccount", "targets": 6},
            {"name": "curAccount", "data": "curAccount", "targets": 7},
            {"name": "bankName", "data": "bankName", "targets": 8},
            {"name": "legalAddress", "data": "legalAddress", "targets": 9},
            {"name": "postAddress", "data": "postAddress", "targets": 10},
            {"name": "keywords", "data": "keywords", "targets": 11},
            {"name": "directorFullname", "data": "directorFullname", "targets": 12},
            {"name": "chiefAccFullname", "data": "chiefAccFullname", "targets": 13}
        ]
    });
});