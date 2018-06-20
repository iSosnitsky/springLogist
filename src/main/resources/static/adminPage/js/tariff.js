$(document).ready(function () {
    var tariffEditor = new $.fn.dataTable.Editor( {
        ajax: {
            create:{
                type:'POST',
                contentType:'application/json',
                url: 'api/tariffs',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    tariffTable.draw("page");
                    tariffEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType: 'application/json',
                type: 'PATCH',
                url: 'api/tariffs/_id_',
                data: function (d) {
                    let newdata;
                    $.each(d.data, function (key, value) {
                        newdata = JSON.stringify(value);
                    });
                    return newdata;
                },
                success: function (response) {
                    tariffTable.draw("page");
                    tariffEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/tariffs/_id_',
                data: function (d) {
                    return '';
                }
            }
        },
        table: '#tariffTable',
        idSrc: 'id',


        fields: [
            { label: 'Именование тарифа', name: 'tariffName', type: 'text'},
            { label: 'Компания', name: 'carrier', type: 'text'},
            {
                label: 'Итоговая вместимость <sup>Т</sup>',
                name: 'capacity',
                type: 'mask',
                mask: '09P99',
                maskOptions: {
                    translation: {
                        'P': {
                            pattern: /\./,
                            fallback: '.'
                        }
                    }
                },
                placeholder: "15.00"
            },
            {
                label: 'Стоимость маршрута',
                name: 'cost',
                type: 'mask',
                mask: '0999999999P99',
                maskOptions: {
                    translation: {
                        'P': {
                            pattern: /\./,
                            fallback: '.'
                        }
                    }
                },
                placeholder: "15000.00"
            },
            {
                label: 'Стоимость за пункт маршрута',
                name: 'costPerPoint',
                type: 'mask',
                mask: '0999999999P99',
                maskOptions: {
                    translation: {
                        'P': {
                            pattern: /\./,
                            fallback: '.'
                        }
                    }
                },
                placeholder: "1500.00"
            },
            {
                label: 'Стоимость за коробку',
                name: 'costPerBox',
                type: 'mask',
                mask: '0999999999P99',
                maskOptions: {
                    translation: {
                        'P': {
                            pattern: /\./,
                            fallback: '.'
                        }
                    }
                },
                placeholder: "500.00"
            },
            {
                label: 'Стоимость за час',
                name: 'costPerHour',
                type: 'mask',
                mask: '0999999999P99',
                maskOptions: {
                    translation: {
                        'P': {
                            pattern: /\./,
                            fallback: '.'
                        }
                    }
                },
                // maskOptions: {clearIfNotMatch: true},
                placeholder: "1000.00"
            }
        ]
    });


    var tariffTable =  $("#tariffTable").DataTable({
        processing: true,
        serverSide: true,
        searchDelay: 800,
        ajax: {
            contentType: 'application/json',
            processing: true,
            url: "data/tariffs", // json datasource
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
                editor: tariffEditor
            },
            {
                extend: "edit",
                editor: tariffEditor
            },
            {
                extend: "remove",
                editor: tariffEditor
            }
        ],
        "paging": 10,
        "columnDefs": [
            {"name": "id", "data": "id", "targets": 0, visible: false},
            {"name": "tariffName", "data": "tariffName", "targets": 1},
            {"name": "carrier", "data": "carrier", "targets": 2},
            {"name": "capacity", "data": "capacity", "targets": 3, render: function(data){return (data) ? data+" т." : ""}},
            {"name": "cost", "data": "cost", "targets": 4,render: function(data){return (data) ? data+"₽" : ""}},
            {"name": "costPerPoint", "data": "costPerPoint", "targets": 5,render: function(data){return (data) ? data+"₽" : ""}},
            {"name": "costPerHour", "data": "costPerHour", "targets": 6,render: function(data){return (data) ? data+"₽" : ""}},
            {"name": "costPerBox", "data": "costPerBox", "targets": 7,render: function(data){return (data) ? data+"₽" : ""}}
        ]
    });
});