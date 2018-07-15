var requestIDExternal;

function showPretension(pretensionID, reqIdExt, cathegory, pretensionSum, pretensionComment, positionNumber) {

    $('#editPretensionSum').val(pretensionSum);
    $('#editPretensionCathegory').val(decodeURI(cathegory)).trigger('change');
    $('#editPretensionComment').val(decodeURI(pretensionComment));
    $('#editPretensionPositionNumber').val(decodeURI(positionNumber));
    $('#updatePretension').off('click').show().on('click', function () {
        $(this).button('loading');
        updatePretension(pretensionID, reqIdExt, function () {
            $('#updatePretension').button('reset');
        })
    });
    $('#deletePretension').off('click').show().on('click', function () {
        $(this).button('loading');
        deletePretension(pretensionID, reqIdExt, function () {
            $('#deletePretension').button('reset');
        });
    });
}

function loadPretensions() {
    $.get("api/pretensions/search/findByRequestIdExternal?requestIdExternal=" + requestIDExternal, function (data) {
        parsedData = data._embedded.pretensions;

        $(".pretensionLink").remove();

        parsedData.forEach(function (pretension, i, parsedData) {
            if (pretension.sum === undefined) {
                pretension.sum = 0;
            }

            $('#pretensionLinks').append(
                // "<br>",
                $('<button>', {
                    text: 'Претензия №' + pretension.pretensionID,
                    click: function () {
                        showPretension(
                            pretension.pretensionID,
                            pretension.requestIDExternal,
                            pretension.pretensionCathegory,
                            parseFloat(pretension.sum),
                            encodeURI(pretension.pretensionComment),
                            pretension.positionNumber)
                    }
                }).addClass("pretensionLink btn-link")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#editPretensionModal")
            );
        });

    })
}

function deletePretension(pretensionID, reqIdExt, callback) {
    if (confirm('Вы действительно хотите удалить претензию?')) {
        $.post("content/getData.php", {
            status: 'deletePretension',
            pretensionID: pretensionID,
            requestIDExternal: reqIdExt
        }, function (data) {
            if (data == 'true') {
                alert("Претензия успешно удалена");
                $('#editPretensionModal').modal('toggle');
                loadPretensions();
            }
            if (callback) {
                callback();
            }
        })
    }
}

function updatePretension(pretensionID, reqIdExt, callback) {
    $.post("content/getData.php", {
        status: 'updatePretension',
        commentRequired: $('#editPretensionComment').prop('required'),
        pretensionID: pretensionID,
        requestIDExternal: reqIdExt,
        pretensionCathegory: $('#editPretensionCathegory').val(),
        pretensionSum: $('#editPretensionSum').val(),
        pretensionComment: $('#editPretensionComment').val(),
        pretensionPositionNumber: $('#editPretensionPositionNumber').val()
    }, function (data) {
        console.log(data);
        if (JSON.parse(data) == true) {
            alert('Претензия успешно обновлена');
            $('#editPretensionModal').modal('toggle');
            loadPretensions();
        }
        if (callback) {
            callback();
        }
    });

}

const $_GET = {};
var myMap;


document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
    function decode(s) {
        return decodeURIComponent(s.split("+").join(" "));
    }

    $_GET[decode(arguments[1])] = decode(arguments[2]);
});

$(document).ready(function () {

    console.log("fgsfds");
    $("#editPretensionModal").on('hidden.bs.modal', function () {
        $('#editPretensionCathegory').val('');
        $('#editPretensionSum').val('');
        $('#editPretensionComment').val('');
        $('#editPretensionPositionNumber').val('').trigger('change');
    });


    $(document).on('change', '.pretensionCathegory', function () {
        var pretensionSum = $(this).closest('.modal-body').find('.pretensionSum');
        var pretensionComment = $(this).closest('.modal-body').find('.pretensionComment');
        var pretensionCommentLabel = $(this).closest('.modal-body').find("label[for = pretensionComment]");
        switch ($(this).val()) {
            case 'Пересорт': {
                pretensionSum
                    .prop('disabled', true)
                    .attr('placeholder', '')
                    .val('');

                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий');
                break;
            }
            case 'Брак': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .attr('placeholder', 'Пожалуйста, опишите повреждение/неисправность')
                    .prop('required', true);
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий<span style="color:#c22929;">*</span>');
                break;
            }
            case 'Недовоз': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий<br><span style="color:#c22929;">Пожалуйста, напишите, что именно не довезли и в каком количестве</span>');
                break;
            }
            case 'Отказ': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', 'Пожалуйста, укажите причину отказа');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий');
                break;

            }
            case 'Перевоз': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .attr('placeholder', 'Укажите куда нужно переадресовать заявку.')
                    .prop('required', true);
                pretensionCommentLabel.html('Комментарий<span style="color:#c22929;">*</span>');
                break;
            }


        }
    });

    if ($_GET['pretensionModal'] === "1") {
        $('#pretensionModal').modal();
        $('#submitPretension').button("<i class='fa fa-circle-o-notch fa-spin'></i> Загрузка...").prop("disabled", true);
        ;
    }


});

// var requestIDExternal = '0';
$(window).on('load', function () {

    console.log("window loaded");
    // $('#deletePretension').hide();
    $('.pretensionSum').mask('0000000000.00', {reverse: true});
    // $('#editPretensionSum').mask('0000000000.00', {reverse: true});

    var timeoutID;


    if ($_GET['requestIDExternal']) {
        requestIDExternal = $_GET['requestIDExternal'];
    }


    function setHtml(html, callback) {
        document.write(html);
        if (window.jQuery) {
            callback();
        } else if (!(window.jQuery)) {

            timeoutID = window.setTimeout(function () {
                    callback()
                }
                , 2000
            );
        }
    }


    if ($_GET['clientId'] && $_GET['invoiceNumber']) {
        $.post("/api/requests/findFirstByClientIdExternalAndInvoiceNumber?clientIdExternal=" + $_GET['clientId'] + "&invoiceNumber=" + $_GET['invoiceNumber'],
            function (data) {
                console.log(data);
                requestIDExternal = data.requestIdExternal;
                setRequestInfo(data);
                removeLoadingScreen();
                loadPretensions();


            }
        )
    } else {
        // setNoData();
        // document.getElementsByClassName('loading')[0].innerHTML="У заявки отсутствует номер накладной";
        console.log("отсутствует то-то и то-то");
        console.log($_GET);
        removeLoadingScreen("noData");
        $(".loading").html("У заявки отсутствует номер накладной");
    }


    var $requestHistoryDialogTable;
    var requestHistoryDialogTable;

    function setRequestInfo(requestData) {
        $('#requestNumber').html(requestData.requestNumber);
        if (requestData.requestDate) $('#request-date').html(new Date(requestData.requestDate).toLocaleString());
        $('#invoice-number').html(requestData.invoiceNumber);
        if (requestData.invoiceDate) $('#invoice-date').html(new Date(requestData.invoiceDate).toLocaleString());
        $('#document-number').html(requestData.documentNumber);
        if (requestData.documentDate) $('#document-date').html(new Date(requestData.documentDate).toLocaleString());
        $('#organization').html(requestData.firma);
        $('#comments').html(requestData.commentForStatus);
        $('#box-quantity').html(requestData.boxQty);
        $('#client-INN').html(requestData.clientId.clientName + ' ' + requestData.clientId.inn);
        $('#sales-representative').html(requestData.marketAgentUserId.userName);
        if(requestData.destinationPointId) $('#arrival-point').html(requestData.destinationPointId.pointName);
        $('#departure-warehouse').html(requestData.warehousePoint.pointName);
        if (requestData.routeListId) $('#pallet-quantity').html(requestData.routeListId.palletsQty);

        setHistoryTable(requestData.requestHistory);

        $('#submitPretension').click(function () {
            var that = $('#submitPretension');
            that.button('loading');
            $.ajax("api/pretensions", {
                commentRequired: $('#pretensionComment').prop('required'),
                status: 'addPretension',
                pretensionCathegory: $('#pretensionCathegory').val(),
                pretensionStatus: 'OPENED',
                pretensionSum: $('#pretensionSum').val(),
                pretensionPositionNumber: $('#pretensionPositionNumber').val(),
                requestIDExternal: requestData.requestIDExternal,
                pretensionComment: $('#pretensionComment').val(),
                linkToRequest: window.location.href.replace('&pretensionModal=1', '').replace(/(md5=.{32}&)/, '').replace(/(&md5=.{32})/, ''),
                invoiceNumber: $_GET['invoiceNumber']
            }, function (data) {
                if (data == 'true') {
                    $('#pretensionModal').modal('toggle');
                    that.button('reset');
                    alert('Претензия успешно отправлена');
                    loadPretensions();
                } else {
                    that.button('reset');
                    alert(data);
                }
            })
        }).button("reset").prop("disabled", false);

        // loadRouteMap();
    }


    function setHistoryTable(requestHistoryData) {
        if (window.jQuery) {
            $requestHistoryDialogTable = $("#requestHistoryDialogTable");
            requestHistoryDialogTable = $requestHistoryDialogTable.DataTable({
                "dom": 'tp', // show only table with no decorations
                "paging": true, // yes pagination
                "order": [[0, "desc"]],
                "data": requestHistoryData,
                "columnDefs": [
                    {"name": "autoTimeMark", "data": "autoTimeMark", "targets": 0},
                    {
                        "name": "lastVisitedRoutePointId",
                        "data": "lastVisitedRoutePointId.pointName",
                        "targets": 1,
                        "defaultContent": "Не указан"
                    },
                    // {"name": "userNameThatChangedStatus", "data": "userNameThatChangedStatus", "targets": 2, visible: false},
                    {
                        "name": "requestStatusId", "data": "requestStatusId", "targets": 2, "render": function (data) {
                            return requestStatusTranslation[data];
                        }
                    },

                    // {"name": "routeListNumber", "data": "routeListNumber", "targets": 4, visible: false},
                    // {"name": "boxQty", "data": "boxQty", "targets": 5, visible: false}
                ],
                pageLength: 10,
            });
            (function showRequestHistory(data) {
                // data = JSON.parse(data);
                for (var i = 0; i < data.length; i++) {
                    data[i].fullName = data[i].userName;
                    delete data[i].userName;
                }
                requestHistoryDialogTable.rows().remove();
                requestHistoryDialogTable.rows.add(data).draw(false);
            })(requestHistoryData);


        } else {
            timeoutID = window.setTimeout(function () {
                setHistoryTable();
            }, 1000)
        }
    }

    function removeLoadingScreen(noData) {
        if (!noData) {
            $(".loading").remove();
            $(".left-table").show(300);
            $(".right-table").show(350);
        } else {
            $(".loading").html("У заявки отсутствует номер накладной");
        }


    }

    function loadRouteMap() {
        // $.post("content/getData.php", {
        //     status: 'getPointsCoordinatesForRequest',
        //     requestIDExternal: requestIDExternal
        // }, function (data) {


        if (typeof ymaps !== 'undefined') {
            ymaps.ready(initMap);
        }


        // if (data == 'true') {
        //     $('#pretensionModal').modal('toggle');
        //     that.button('reset');
        //     alert('Претензия успешно отправлена');
        //     loadPretensions();
        // } else {
        //     that.button('reset');
        //     alert(data);
        // }
        // })
    }

    function initMap() {
        // Создание экземпляра карты и его привязка к контейнеру с
        // заданным id ("map").
        // console.log(JSON.stringify(data));


        console.log(requestIDExternal);
        $.post("content/getData.php", {
            status: 'getPointsCoordinatesForRequest',
            requestIDExternal: requestIDExternal
        }, function (data) {
            data = JSON.parse(data);
            console.log(JSON.stringify(data));

            if (data.hasOwnProperty('destinationPoint')) {
                $('#map').show();
                myMap = new ymaps.Map('map', {
                    center: data.destinationPoint.geometry,
                    zoom: 10
                }, {
                    searchControlProvider: 'yandex#search'
                });

                routePoints = [];
                if (data.hasOwnProperty('warehousePoint')) {
                    routePoints.push({type: 'wayPoint', point: data.warehousePoint.geometry});
                    myMap.geoObjects.add(new ymaps.Placemark(data.warehousePoint.geometry, data.warehousePoint.properties));

                }
                // if (data.hasOwnProperty('lastVisitedPoint')) {
                //     routePoints.push({type:'viaPoint', point:data.lastVisitedPoint.geometry});
                // myMap.geoObjects.add(new ymaps.Placemark(data.lastVisitedPoint.geometry,data.lastVisitedPoint.properties));
                // }
                myMap.geoObjects.add(new ymaps.Placemark(data.destinationPoint.geometry, data.destinationPoint.properties, data.destinationPoint.options));
                routePoints.push({type: 'wayPoint', point: data.destinationPoint.geometry});


                ymaps.route(routePoints).then(
                    // ymaps.route([[55.755786, 37.117633], [55.155786, 37.617633]]).then(
                    function (mappedRoute) {
                        myMap.geoObjects.add(mappedRoute);
                    },
                    function (error) {



                        // alert('Возникла ошибка: ' + error.message);
                        // console.log(JSON.stringify(routePoints));
                        // var myPlacemark = new ymaps.Placemark(data[0]);
                        // routePoints.forEach(function (t, n, arr) {
                        //     console.log(JSON.stringify(t));
                        //     myMap.geoObjects.add(new ymaps.Placemark(t));
                        //     myMap.setCenter(t)
                        // });
                        // myMap.geoObjects.add(new ymaps.Placemark(data[0]));
                        // myMap.setCenter(data[0].point);
                    }
                );
                $.post("content/getData.php", {
                    status: 'getVehicleData',
                    requestIDExternal: requestIDExternal
                }, function (vehicleData) {

                    vehicleData = JSON.parse(vehicleData);
                    if (vehicleData.hasOwnProperty('vehiclePlacemark')) {
                        console.log(JSON.stringify(vehicleData.vehiclePlacemark.options));
                        vehiclePlacemark = new ymaps.Placemark(vehicleData.vehiclePlacemark.geometry, vehicleData.vehiclePlacemark.properties, vehicleData.vehiclePlacemark.options);
                        myMap.geoObjects.add(vehiclePlacemark);
                        myMap.setCenter(vehicleData.vehiclePlacemark.geometry);
                    }
                })

            }

        })


        // objectManager = new ymaps.ObjectManager({
        //     // Чтобы метки начали кластеризоваться, выставляем опцию.
        //     clusterize: true,
        //     // ObjectManager принимает те же опции, что и кластеризатор.
        //     gridSize: 32,
        //     clusterDisableClickZoom: true
        // });

        // myMap.geoObjects.add(objectManager);

        // $.post("content/getData.php",
        //     {status: "getAllPointsCoordinates", format: "json"},
        //     function (json) {
        //         console.log(json);
        //         objectManager.objects.options.set('preset', 'islands#greenDotIcon');
        //         objectManager.clusters.options.set('preset', 'islands#greenClusterIcons');
        //         objectManager.add(JSON.parse(json));
        //         // var geoObjects = ymaps.geoQuery(json)
        //         //     .addToMap(myMap)
        //         //     .applyBoundsToMap(myMap, {
        //         //         checkZoomRange: true
        //         //     });
        //
        //     }
        // );
    }

    var requestStatusTranslation = {
        "ADJUSTMENTS_MADE": "Сделаны корректировки, распечатаны документы",
        "APPROVED": "Утверждена к сборке",
        "APPROVING": "Выгружена на утверждение торговому представителю",
        "ARRIVED": "Накладная прибыла в пункт",
        "CHECK": "На контроле",
        "CHECK_BOXES": "Проверка коробок в зоне отгрузки",
        "CHECK_PASSED": "Контроль пройден",
        "COLLECTING": "Выдана на сборку",
        "CREATED": "заявка добавлена в БД",
        "CREDIT_LIMIT": "Кредитный лимит",
        "DELETED": "заявка удалена из БД",
        "DELIVERED": "Доставлено",
        "DEPARTURE": "В транзите",
        "ERROR": "Ошибка. Возвращение в пункт",
        "PACKAGING": "Упаковано",
        "RASH_CREATED": "Создана расходная накладная",
        "READY": "Проверка в зоне отгрузки/Готова к отправке",
        "RESERVED": "Резерв",
        "ROUTE_LIST_MADE": "Маршрутный лист подобран",
        "SAVED": "Заявка в состоянии черновика",
        "STOP_LIST": "Стоп-лист",
        "TRANSPORTATION": "Маршрутный лист закрыт, товар передан экспедитору на погрузку",
        "UNKNOWN": "неизвестный статус",
        "UPDATED": "Заявка обновлена из 1С"
    };

}());




