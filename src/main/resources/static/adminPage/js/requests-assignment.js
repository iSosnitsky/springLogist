$(document).ready(function () {
    let warehouseSelect;
    $.post(
        "content/getData.php",
        {
            status: "getWarehouses", format: "json"
        },
        function (data) {
            let options = [];
            // console.log(data);
            data = JSON.parse(data);
            data.forEach(function (entry) {
                var selectizeOption = {text: entry.pointName, value: entry.pointID};
                options.push(selectizeOption);
            });
            warehouseSelect = $("#warehouseId").selectize(
                {
                    sortField: "text",
                    diacritics: true,
                    maxItems: 1,
                    options: options,
                    dropdownParent: 'body',
                    onChange: function (values) {
                        if (values) {
                            $("#assignRequests").removeAttr("disabled");
                            // console.log(JSON.stringify(values));
                        } else {
                            $("#assignRequests").attr("disabled", "disabled");
                        }
                    }
                }

            );
        }
    );



// function assignRequests() {
//     console.log(JSON.stringify(warehouseSelect[0].selectize.getValue()));
//     $("#assignRequests").attr("disabled", "disabled");
//     $("#assignRequests").val("Заявки распределяются, подождите");
//     $.post(
//         "content/getData.php",
//         {
//             status: "assignRequests", format: "json", warehouseId: warehouseSelect[0].selectize.getValue()
//         },
//         function (data) {
//             // console.log(JSON.stringify(data));
//             $("#assignRequests").removeAttr("disabled");
//             $("#assignRequests").setValue("Распределить");
//         }
//     );
// }

    $("#assignRequests").on("click", function () {
        $("#assignRequests").attr("disabled", "disabled").val("Заявки распределяются, подождите");
        $.post(
            "content/getData.php",
            {
                status: "assignRequests", format: "json", warehouseId: warehouseSelect[0].selectize.getValue()
            },
            function (data) {
                // console.log(data);
                if (data!='0') {
                    //TODO: put it back
                    // localStorage.setItem("generatedRouteLists", data);
                    // generateLinks();
                    generateLinks(data);
                } else {
                    var $html='<li>Не удалось подобрать маршрутные листы</li>';
                    $('#autoInsertedRouteListLinks').html($html);
                }
                $("#assignRequests").removeAttr("disabled").val("Распределить");

            }
        );
    });


    function generateLinks(routeListsData) {
        if(routeListsData!="0"){
            routeListsData = JSON.parse(routeListsData);
            // console.log(JSON.stringify(routeListsData));
            // console.log(routeListsData[0].routeListId);
            if (routeListsData!==null && routeListsData!==''){
                console.log(routeListsData);

                $("#routeListsLinks").show();
                $('#autoInsertedRouteListLinks').html('');
                $html='';
                if(routeListsData==0){
                    $html+='<li>Не удалось подобрать маршрутные листы</li>';
                } else {
                    sum = 0;
                    maxSum = 0;
                    routeListsData.forEach(function (item) {
                        $html+=
                            "<li><a href=/?routeListId="+item.routeListId+" >"+item.routeListNumber +"</a>"+((item.maxCapacity<=item.currentCapacity) ? " МЛ загружен на 100% возможно переполнение <br>" : "")+"</li>"
                        sum+=precisionRound(item.currentCost,2);
                        maxSum+=+precisionRound(item.maxCost,2);
                    });
                    $html+="<li>Общая стоимость :<b style='color: #67b168'>"+sum+"</b> <br>Общая стоимость без оптимизации:<b style='color: #a94442'>"+maxSum+"</b></li>"
                }

                $('#autoInsertedRouteListLinks').html($html);
            }
        } else {
            var $html='<li>Не удалось подобрать маршрутные листы</li>';
            $('#autoInsertedRouteListLinks').html($html);
        }
    }

    //TODO: Put it back
    // function generateLinks() {
    //     routeListsData = localStorage.getItem("generatedRouteLists");
    //     if(routeListsData!="0"){
    //         routeListsData = JSON.parse(routeListsData);
    //         // console.log(JSON.stringify(routeListsData));
    //         // console.log(routeListsData[0].routeListId);
    //         if (routeListsData!==null && routeListsData!==''){
    //             console.log(routeListsData);
    //
    //             $("#routeListsLinks").show();
    //             $('#autoInsertedRouteListLinks').html('');
    //             $html='';
    //             if(routeListsData==0){
    //                 $html+='<li>Не удалось подобрать маршрутные листы</li>';
    //             } else {
    //                 sum = 0;
    //                 maxSum = 0;
    //                 routeListsData.forEach(function (item) {
    //                     $html+=
    //                         "<li><a href=/?routeListId="+item.routeListId+" >"+item.routeListNumber +"</a>"+((item.maxCapacity<=item.currentCapacity) ? " МЛ загружен на 100% возможно переполнение <br>" : "")+"</li>"
    //                     sum+=precisionRound(item.currentCost,2);
    //                     maxSum+=+precisionRound(item.maxCost,2);
    //                 });
    //                 $html+="<li>Общая стоимость :<b style='color: #67b168'>"+sum+"</b> <br>Общая стоимость без оптимизации:<b style='color: #a94442'>"+maxSum+"</b></li>"
    //             }
    //
    //             $('#autoInsertedRouteListLinks').html($html);
    //         }
    //     } else {
    //         var $html='<li>Не удалось подобрать маршрутные листы</li>';
    //         $('#autoInsertedRouteListLinks').html($html);
    //     }
    // }

    //TODO: put it back
    // generateLinks();

    function precisionRound(number, precision) {
        var shift = function (number, precision, reverseShift) {
            if (reverseShift) {
                precision = -precision;
            }
            numArray = ("" + number).split("e");
            return +(numArray[0] + "e" + (numArray[1] ? (+numArray[1] + precision) : precision));
        };
        return shift(Math.round(shift(number, precision, false)), precision, true);
    }
});


