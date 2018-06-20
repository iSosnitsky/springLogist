var usersEditor;
$(document).ready(function () {

    usersEditor = new $.fn.dataTable.Editor({
        ajax: {
            create: {
                type: 'POST',
                contentType:'application/json',
                url:  'api/users',
                data: function (d) {
                    let newdata;

                    $ .each (d.data, function (key, value) {
                        value.salt = calcMD5("fgsfds").slice(0,16);
                        value.dataSource = "ADMIN_PAGE";
                        value.passAndSalt = calcMD5(value.password+calcMD5(value.salt));
                        delete value["password"];
                        newdata = JSON.stringify (value) ;
                    });
                    return newdata;
                },
                success: function (response) {
                    usersDataTable.draw();
                    usersEditor.close();
                    // alert(response.responseText);
                },
                error: function (jqXHR, exception) {
                    alert(response.responseText);
                }
            },
            edit: {
                contentType:'application/json',
                type: 'PATCH',
                url:  'api/users/_id_',
                data: function (d) {
                    let newdata;
                    $ .each (d.data, function (key, value) {
                        if(value.password!="dummy"){
                            value.salt = calcMD5("fgsfds").slice(0,16);
                            value.passAndSalt = calcMD5(value.password+calcMD5(value.salt));
                            delete value["password"];
                        } else {
                            delete value["password"];
                        }
                        newdata = JSON.stringify (value);
                    });
                    console.log(newdata);
                    return newdata;
                },
                success: function (response) {
                    usersDataTable.draw();
                    usersEditor.close();
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
                url:  'api/users/_id_',
                data: function (d) {
                    return '';
                }
            }
        },
        table: '#usersTable',
        idSrc: 'userID',


        fields: [
            {label: 'ФИО', name: 'userName', type: 'text'},
            {label: 'Логин', name: 'login', type: 'text'},
            {label: 'Должность', name: 'position', type: 'text'},
            {
                label: 'Номер телефона',
                name: 'phoneNumber',
                type: 'mask',
                mask: "(000) 000-00-00",
                maskOptions: {clearIfNotMatch: true},
                placeholder: "(999) 999-99-99"
            },
            {label: 'Почта', name: 'email', type: 'text', visible: false},
            {label: 'Пароль', name: 'password', type: 'password'},
            {
                label: 'Роль', name: 'userRole', type: 'selectize', options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Пункт', name: 'point', type: 'selectize', options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'ИНН Клиента',
                name: 'client',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Транспортная компания',
                name: 'transportCompany',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            }
        ]
    });
    usersEditor.on( 'preSubmit', function (e,data, action) {
            data = data.data;
            console.log(data);
            if(action=='create'){

            }
            console.log(data);
    } );

    usersEditor.on('postSubmit', function(e, json, data, action){
        var rowData = $.extend( {}, json );

        json.data = [ rowData ];
        console.log(json);
    });

    var userRoleSelectize = usersEditor.field('userRole').inst();

    userRoleSelectize.clear();
    userRoleSelectize.clearOptions();
    userRoleSelectize.load(function (callback) {
        callback(userRoleOptions);
    });

    // set current selected value to point and userRole
    usersEditor.on('open', function (e, mode, action) {
        usersEditor.field('point').disable();
        usersEditor.field('client').disable();
        usersEditor.field('transportComany').disable();
        if (action === "edit") {
            selectCurrentUserRole();
        }
    });

    // transform password to md5
    usersEditor.on('preSubmit', function (e, data, action) {
        data.status = 'userEditing';
        if(usersEditor.field(''))
        if (action === 'create' || action === 'edit') {
            for (i in data.data) {
                data.data[i].password = calcMD5(data.data[i].password);
            }
        }
    });

    usersEditor.field('client').input().on('keyup', function (e, d) {
        var clientINNPart = $(this).val();
        $.get( "api/clients/search/findTop15ByClientNameContaining?name="+clientINNPart,
            function (clientsData) {
                var selectizeOptions = [];
                console.log(clientsData);
                clientsData._embedded.clients.forEach(function (entry) {
                    var selectizeOption = {
                        "label": "ИНН: " + entry.inn + ", имя: " + entry.clientName,
                        "value": entry._links.self.href
                    };
                    selectizeOptions.push(selectizeOption);
                });
                let clientSelectize = usersEditor.field('client').inst();

                clientSelectize.clear();
                clientSelectize.clearOptions();
                clientSelectize.load(function (callback) {
                    callback(selectizeOptions);
                });
            }
        );
    });




    usersEditor.field('point').input().on('keyup', function (e, d) {
        var pointPart = $(this).val();
        $.get( "api/points/search/findTop10ByPointNameContaining/?pointName="+pointPart,
            function (data) {
            console.log(data);
                var options = [];

                var selectizePointsOptions = [];
                // data = JSON.parse(data);
                data._embedded.points.forEach(function (entry) {
                    var selectizeOption = {"label": entry.pointName, "value": entry._links.self.href};
                    selectizePointsOptions.push(selectizeOption);
                });

                var selectize2 = usersEditor.field('point').inst();
                selectize2.clear();
                selectize2.clearOptions();
                selectize2.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );
    });


    usersEditor.field('userRole').input().on('change', function (e, d) {
        if (d && d.editorSet) return;

        var currentRole = $(this).val();
        enableColumnsByRole(currentRole);
    });

    // example data for exchange with server
    //var exampleData = [{userID: 1, userName:"wefwfe", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
    //    email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRole:"Диспетчер", point:"point1"}];


    var usersDataTable = $("#usersTable").DataTable({
            processing: true,
            serverSide: true,
            searchDelay: 800,
            ajax: {
                contentType: 'application/json',
                processing: true,
                data: function(d) {
                    return JSON.stringify(d);
                },
                url: "data/users", // json datasource
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
                    editor: usersEditor
                },
                {
                    extend: "edit",
                    editor: usersEditor
                },
                {
                    extend: "remove",
                    editor: usersEditor
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "userID", "data": "userID", "targets": 0, visible: false},
                {"name": "userName", "data": "userName", "targets": 1},
                {"name": "login", "data": "login", "targets": 2},
                {"name": "position", "data": "position", "targets": 3},
                {"name": "phoneNumber", "data": "phoneNumber", "targets": 4},
                {"name": "email", "data": "email", "targets": 5},
                {"name": "password", "data": "password", "targets": 6, visible: false,"defaultContent":"dummy"},
                {"name": "userRole", "data": "userRole", "targets": 7
                    ,"render": function ( data, type, row, meta ) {
                        return userRoles[data];
                    }
                },
                {"name": "point", "data": "point.pointName", "targets": 8, "defaultContent":""},
                {"name": "client", "data": "client.clientName", "targets": 9, visible: false, "defaultContent":""},
                {"name": "transportCompany", "data": "transportCompany.name", "targets": 10, visible: true,"defaultContent":""}
            ]
        }
    );
    {
        function selectCurrentUserRole() {
            var userRoleRusName = usersDataTable.row($('#usersTable .selected')[0]).data()['userRole'];
            var selectizeInstance = usersEditor.field('userRole').inst();
            var role = selectizeInstance.search(userRoleRusName);
            selectizeInstance.setValue(role.items[0].id, true);
            enableColumnsByRole(role.items[0].id)
        }

        function enableColumnsByRole(currentRole) {
            if (currentRole === "CLIENT_MANAGER") {
                usersEditor.field('point').disable();
                usersEditor.field('point').set('');
                usersEditor.field('client').enable();
                usersEditor.field('client').set('');
                usersEditor.field('transportCompany').disable();
                usersEditor.field('transportCompany').set('');
            }

            if (currentRole === "TEMP_REMOVED") {
                usersEditor.field('point').enable();
                usersEditor.field('point').set('');
                usersEditor.field('client').enable();
                usersEditor.field('client').set('');
                usersEditor.field('transportCompany').disable();
                usersEditor.field('transportCompany').set('');
            }

            if (currentRole === "ADMIN" || currentRole === "MARKET_AGENT") {
                usersEditor.field('point').disable();
                usersEditor.field('point').set('');
                usersEditor.field('client').disable();
                usersEditor.field('client').set('');
                usersEditor.field('transportCompany').disable();
                usersEditor.field('transportCompany').set('');
            }

            if (currentRole === "DISPATCHER" || currentRole === "W_DISPATCHER") {
                usersEditor.field('point').enable();
                usersEditor.field('point').set('');
                usersEditor.field('client').disable();
                usersEditor.field('client').set('');
                usersEditor.field('transportCompany').disable();
                usersEditor.field('transportCompany').set('');
            }

            if (currentRole ==="TRANSPORT_COMPANY"){
                usersEditor.field('transportCompany').enable();
                usersEditor.field('transportCompany').set('');
                usersEditor.field('client').disable();
                usersEditor.field('client').set('');
                usersEditor.field('point').disable();
                usersEditor.field('point').set('');
            }
        }
    }

    usersEditor.field('transportCompany').input().on('keyup', function (e, d) {
        var namePart = $(this).val();
        $.get( "api/transportCompanies/search/findTop10ByNameContaining/?companyName="+namePart,
            function (data) {
                console.log(data);
                var options = [];

                var selectizeTransportCompaniesOptions = [];
                // data = JSON.parse(data);
                data._embedded.transportCompanies.forEach(function (entry) {
                    var selectizeOption = {"label": entry.name, "value": entry._links.self.href};
                    selectizeTransportCompaniesOptions.push(selectizeOption);
                });

                var selectize2 = usersEditor.field('transportCompany').inst();
                selectize2.clear();
                selectize2.clearOptions();
                selectize2.load(function (callback) {
                    callback(selectizeTransportCompaniesOptions);
                });
            }
        );
    })

});
