$(document).ready(function () {
    refreshTransactionWidget();
    var dataTable = $('#transactionsTable').DataTable({
        processing: true,
        search: {
            caseInsensitive: true
        },
        order: [[3,"desc"]],
        fixedColumns: {
            leftColumns: 1
        },
        ajax: {
            url: "content/getData.php", // json datasource
            type: "post",  // method  , by default get
            data: {"status": "getAllTransactions"}
        },
        columnDefs: [
            {"name": "packet_id", "data": "packet_id", "targets": 0},
            {"name": "server", "data": "server", "targets": 1},
            {"name": "status", "data": "status", "targets": 2},
            {"name": "date", "data": "date", "targets": 3}
        ],
        language: {
            url: '/localization/dataTablesRus.json'
        },
        dom: 'Bfrtip',
        // responsive: true,

        buttons: [
            {
                text: "Обновить <i class=\"fa fa-refresh\"  aria-hidden=\"true\">",
                action: function (e, dt, node, config) {
                    this.text("Обновить <i class=\"fa fa-refresh fa-spin\"  aria-hidden=\"true\">");
                    let that = this;
                    this.disable();
                    dataTable.ajax.reload(function () {
                        that.text("Обновить <i class=\"fa fa-refresh\"  aria-hidden=\"true\">");
                        that.enable();
                    }, false);
                }
            }
        ]
    });

});

function refreshTransactionWidget() {
    $('#transactionWidget').find('.fa-refresh').addClass("fa-spin");
    $.post(
        "content/getData.php",
        {status: "getLastTransaction", format: "json"},
        /*Returns last recorded transaction:
        data: {
        entry_id: int,
        packet_id: int,
        date: date,
        server: int,
        status: OK/ERROR (enum
        }
         */
        function (data) {
            data=JSON.parse(data);
            fillTransactionWidget(data);
            console.log(data);
        }
    );
}


function fillTransactionWidget(lastTransactionData) {
 $('#transactionStatus').html(lastTransactionData.status==='OK' ? "<i class=\"fa fa-check\" style='color:green' aria-hidden=\"true\"></i> " + " ОК "+"<i class=\"fa fa-refresh\" onclick='refreshTransactionWidget()' aria-hidden=\"true\"></i>" : "<i class=\"fa fa-times\" style=\"color:red\" aria-hidden=\"true\"></i>"+" Ошибка "+"<i class=\"fa fa-refresh\"  ' onclick='refreshTransactionWidget()' aria-hidden=\"true\"></i>");
 $('#lastTransactionServer').html(lastTransactionData.server);
 $('#lastTransactionTime').html(lastTransactionData.date);
 $('#lastTransactionPacket').html(lastTransactionData.packet_id);
}

