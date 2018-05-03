$(document).ready(function () {
    var routeEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#routeTable',
        idSrc: 'routeID',

        language: {
            decimal: "."
        },

        fields: [
            {label: 'Название маршрута', name: 'routeName', type: 'text'},
            {
                label: 'Стоимость за маршрут',
                name: 'cost',
                type: 'mask',
                mask: '0999999999.99',
                // maskOptions: {
                //     translation: {
                //         'P': {
                //             pattern: /\./,
                //             fallback: '.'
                //         }
                //     }
                // },
                placeholder: "1000.00"
            },
            {
                label: 'Стоимость за точку',
                name: 'cost_per_point',
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
                placeholder: "1000.00"
            },
            {
                label: 'Стоимость за час',
                name: 'cost_per_hour',
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
                placeholder: "1000.00"
            },
            {
                label: 'Стоимость за коробку',
                name: 'cost_per_box',
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
                placeholder: "1000.00"
            }

        ]
    });

    routeEditor.on('preSubmit', function (e, data, action) {
        data.status = 'routeEditingOnly';
    });

    var $routeDataTable = $("#routeTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getRoutesData"}
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
                    editor: routeEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "edit",
                    editor: routeEditor,
                    text: 'изменить запись'
                },
                {
                    extend: "remove",
                    editor: routeEditor,
                    text: 'удалить запись'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "routeID", "data": "routeID", "targets": 0, visible: false},
                {"name": "routeName", "data": "routeName", "targets": 1},
                {"name": "tariffID", "data": "tariffID", "targets": 2, visible: false},
                {"name": "cost", "data": "cost", targets: 3},
                {"name": "cost_per_point", "data": "cost_per_point", "targets": 4},
                {"name": "cost_per_hour", "data": "cost_per_hour", "targets": 5},
                {"name": "cost_per_box", "data": "cost_per_box", "targets":6},
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
});