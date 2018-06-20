$(document).ready(function () {
    var routeEditor = new $.fn.dataTable.Editor({
        ajax: {
            create: {
                type: 'POST',
                contentType:'application/json',
                url:  'api/routes',
                data: function (d) {
                    let newdata;
                    $ .each (d.data, function (key, value) {
                        delete value["cost"];
                        delete value["costPerBox"];
                        delete value["costPerHour"];
                        delete value["costPerPoint"];
                        newdata = JSON.stringify (value);
                    });
                    return newdata;
                },
                success: function (response) {
                    routeDataTable.draw();
                    routeEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType:'application/json',
                type: 'PATCH',
                url:  'api/routes/_id_',
                data: function (d) {
                    let newdata;
                    $ .each (d.data, function (key, value) {
                        delete value["cost"];
                        delete value["costPerBox"];
                        delete value["costPerHour"];
                        delete value["costPerPoint"];
                        newdata = JSON.stringify (value);
                    });
                    return newdata;
                },
                success: function (response) {
                    routeDataTable.draw("page");
                    routeEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            }
            ,
            remove: {
                type: 'DELETE',
                contentType:'application/json',
                url:  'api/routes/_id_',
                data: function (d) {
                    return '';
                }
            }
        },
        table: '#routeTable',
        idSrc: 'id',


        language: {
            decimal: "."
        },

        fields: [
            {label: 'Название маршрута', name: 'routeName', type: 'text'},
            {
                label: 'Тариф', name: 'tariff', type: 'selectize', options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Стоимость за маршрут',
                name: 'cost',
                type: 'readonly'
                // type: 'mask',
                // mask: '0999999999.99',
                // maskOptions: {
                //     translation: {
                //         'P': {
                //             pattern: /\./,
                //             fallback: '.'
                //         }
                //     }
                // },
                // placeholder: "1000.00"
            },
            {
                label: 'Стоимость за точку',
                name: 'costPerPoint',
                type: 'readonly',
                // mask: '0999999999P99',
                // maskOptions: {
                //     translation: {
                //         'P': {
                //             pattern: /\./,
                //             fallback: '.'
                //         }
                //     }
                // },
                // placeholder: "1000.00"
            },
            {
                label: 'Стоимость за час',
                name: 'costPerHour',
                type: 'readonly',
                // mask: '0999999999P99',
                // maskOptions: {
                //     translation: {
                //         'P': {
                //             pattern: /\./,
                //             fallback: '.'
                //         }
                //     }
                // },
                // placeholder: "1000.00"
            },
            {
                label: 'Стоимость за коробку',
                name: 'costPerBox',
                type: 'readonly',
                // mask: '0999999999P99',
                // maskOptions: {
                //     translation: {
                //         'P': {
                //             pattern: /\./,
                //             fallback: '.'
                //         }
                //     }
                // },
                // placeholder: "1000.00"
            }

        ]
    });

    routeEditor.on('preSubmit', function (e, data, action) {
        data.status = 'routeEditingOnly';
    });

    var routeDataTable = $("#routeTable").DataTable({
            processing: true,
            serverSide: true,
        searchDelay: 800,
            ajax: {
                contentType: 'application/json',
                processing: true,
                data: function(d) {
                    return JSON.stringify(d);
                },
                url: "data/routes", // json datasource
                type: "post"  // method  , by default get
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
                    editor: routeEditor
                },
                {
                    extend: "edit",
                    editor: routeEditor
                },
                {
                    extend: "remove",
                    editor: routeEditor
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "id", "data": "id", "targets": 0, visible: false,"defaultContent":""},
                {"name": "routeName", "data": "routeName", "targets": 1,"defaultContent":""},
                {"name": "tariff", "data": "tariff.tariffName", targets: 2,"defaultContent":""},
                {"name": "cost", "data": "tariff.cost", targets: 3,"defaultContent":"",render: function(data){return (data) ? data+"₽" : ""}},
                {"name": "costPerPoint", "data": "tariff.costPerPoint", "targets": 4,"defaultContent":"",render: function(data){return (data) ? data+"₽" : ""}},
                {"name": "costPerHour", "data": "tariff.costPerHour", "targets": 5,"defaultContent":"",render: function(data){return (data) ? data+"₽" : ""}},
                {"name": "costPerBox", "data": "tariff.costPerBox", "targets":6,"defaultContent":"",render: function(data){return (data) ? data+"₽" : ""}},
            ]
        }
    );



    Highcharts.chart('routesChart', {

        title: {
            text: 'Стоимость перевозки за 1КГ'
        },

        subtitle: {
            text: 'Самый популярный маршрут за прошлый год (Владимир-город)'
        },

        yAxis: {
            title: {
                text: 'Стоимость за 1КГ'
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },

        plotOptions: {
            series: {
                label: {
                    connectorAllowed: false
                }
            }
        },

        xAxis: {
            categories: [
                '04/2017',
                '05/2017',
                '06/2017',
                '07/2017',
                '08/2017',
                '09/2017',
                '10/2017',
                '11/2017',
                '12/2017',
                '01/2018',
                '02/2018',
                '03/2018',
                '04/2018'

            ],
            crosshair: true
        },

        series: [{
            name: 'Загружено из 1С',
            data: [511, 531, 526, 580, 579, 575, 603, 584]
        }, {
            name: 'Распределено вручную',
            data: [498, 513, 516, 520, 531, 511, 548, 536]
        }, {
            name: 'Распределено автоматически',
            data: [478, 455, 482, 493, 477, 489, 501, 502]
        }],

        responsive: {
            rules: [{
                condition: {
                    maxWidth: 500
                },
                chartOptions: {
                    legend: {
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    }
                }
            }]
        }

    });


    routeEditor.field('tariff').input().on('keyup', function (e, d) {
        var namePart = $(this).val();
        $.get( "api/tariffs/search/findTop10ByTariffNameContaining/?tariffName="+namePart,
            function (data) {
                console.log(data);
                var tariffOptions = [];
                // data = JSON.parse(data);
                data._embedded.tariffs.forEach(function (entry) {
                    var selectizeOption = {"label": entry.tariffName, "cost":entry.cost, "costPerBox":entry.costPerBox, "costPerPoint":entry.costPerPoint, "costPerHour":entry.costPerHour, "value": entry._links.self.href};
                    tariffOptions.push(selectizeOption);
                });

                var selectize = routeEditor.field('tariff').inst();
                selectize.clear();
                selectize.clearOptions();
                selectize.load(function (callback) {
                    callback(tariffOptions);
                });
            }
        );
    });

    routeEditor.field('tariff').input().on('change', function (e, e) {
        var selectize = routeEditor.field('tariff').inst();
        value = $.map(selectize.items, function (value) {
            return selectize.options[value];
        })[0];
        if(value!=undefined){
        routeEditor
            .set('cost',value.cost)
            .set('costPerBox',value.costPerBox)
            .set('costPerHour',value.costPerHour)
            .set('costPerPoint',value.costPerPoint);
        } else {
            // routeEditor
            //     .set('cost',"")
            //     .set('costPerBox',"")
            //     .set('costPerHour',"")
            //     .set('costPerPoint',"");
        }
        // routeEditor.field('cost').input.val()
    })
});