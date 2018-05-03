$(document).ready(function () {
    $.get("data/getAllUserRoles",
        // server returns array of pairs [{userRoleID:'ADMIN', userRoleRusName:'Администратор'}, {userRoleID:'W_DISPATCHER', userRoleRusName:'Диспетчер_склада'}]
        function (userRolesData) {
            var options = [];
            var selectizeOptions = [];
            userRolesData = JSON.parse(userRolesData);
            userRolesData.forEach(function (entry) {
                var option = "<option value=" + entry.userRoleID + ">" + entry.userRoleRusName + "</option>";
                options.push(option);
                var selectizeOption = {"label": entry.userRoleRusName, "value": entry.userRoleID};
                selectizeOptions.push(selectizeOption);
            });
            var userRoleSelectize = usersEditor.field('userRoleRusName').inst();

            userRoleSelectize.clear();
            userRoleSelectize.clearOptions();
            userRoleSelectize.load(function (callback) {
                callback(selectizeOptions);
            });
        });

    var usersEditor = new $.fn.dataTable.Editor({
        ajax: 'content/getData.php',
        table: '#usersTable',
        idSrc: 'userId',

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
                label: 'Роль', name: 'userRoleRusName', type: 'selectize', options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'Пункт', name: 'pointName', type: 'selectize', options: [],
                opts: {
                    diacritics: true,
                    searchField: 'label',
                    labelField: 'label',
                    dropdownParent: "body"
                }
            },
            {
                label: 'ИНН Клиента',
                name: 'clientID',
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
                name: 'transport_company_id',
                type: 'selectize',
                options: [],
                opts: {
                    diacritics: true,
                    searchField: 'text',
                    labelField: 'text',
                    dropdownParent: "body"
                }
            }
        ]
    });

    // set current selected value to pointName and userRoleRusName
    usersEditor.on('open', function (e, mode, action) {
        usersEditor.field('pointName').disable();
        usersEditor.field('clientID').disable();
        usersEditor.field('transport_company_id').disable();
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

    usersEditor.field('clientID').input().on('keyup', function (e, d) {
        var clientINNPart = $(this).val();
        $.post( "content/getData.php",
            {status: "getClientsByINN", format: "json", inn: clientINNPart},
            function (clientsData) {
                var options = [];
                var selectizeOptions = [];
                clientsData = JSON.parse(clientsData);
                clientsData.forEach(function (entry) {
                    var option = "<option value=" + entry.clientID + ">" + "ИНН: " + entry.INN + ", имя: " + entry.clientName + "</option>";
                    options.push(option);
                    var selectizeOption = {"label": "ИНН: " + entry.INN + ", имя: " + entry.clientName, "value": entry.clientID};
                    selectizeOptions.push(selectizeOption);
                });
                var clientSelectize = usersEditor.field('clientID').inst();

                clientSelectize.clear();
                clientSelectize.clearOptions();
                clientSelectize.load(function (callback) {
                    callback(selectizeOptions);
                });
            }
        );
    });




    usersEditor.field('pointName').input().on('keyup', function (e, d) {
        var pointNamePart = $(this).val();
        $.post( "content/getData.php",
            {status: "getPointsByName", format: "json", name: pointNamePart},
            function (data) {
                var options = [];

                var selectizePointsOptions = [];
                data = JSON.parse(data);
                data.forEach(function (entry) {
                    var option = "<option value=" + entry.pointID + ">" + entry.pointName + "</option>";
                    options.push(option);
                    var selectizeOption = {"label": entry.pointName, "value": entry.pointID};
                    selectizePointsOptions.push(selectizeOption);
                });

                var selectize2 = usersEditor.field('pointName').inst();
                selectize2.clear();
                selectize2.clearOptions();
                selectize2.load(function (callback) {
                    callback(selectizePointsOptions);
                });
            }
        );
    });


    usersEditor.field('userRoleRusName').input().on('change', function (e, d) {
        if (d && d.editorSet) return;

        var currentRole = $(this).val();
        enableColumnsByRole(currentRole);
    });

    // example data for exchange with server
    //var exampleData = [{userID: 1, userName:"wefwfe", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
    //    email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRoleRusName:"Диспетчер", pointName:"point1"}];


    var $usersDataTable = $("#usersTable").DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                url: "content/getData.php", // json datasource
                type: "post",  // method  , by default get
                data: {"status": "getUsersData"}
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
                    editor: usersEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "edit",
                    editor: usersEditor,
                    text: "изменить"
                },
                {
                    extend: "remove",
                    editor: usersEditor,
                    text: 'удалить запись'
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "userId", "data": "userId", "targets": 0, visible: false},
                {"name": "userName", "data": "userName", "targets": 1},
                {"name": "login", "data": "login", "targets": 2},
                {"name": "position", "data": "position", "targets": 3},
                {"name": "phoneNumber", "data": "phoneNumber", "targets": 4},
                {"name": "email", "data": "email", "targets": 5},
                {"name": "password", "data": "password", "targets": 6, visible: false},
                {"name": "userRoleRusName", "data": "userRoleRusName", "targets": 7},
                {"name": "pointName", "data": "pointName", "targets": 8},
                {"name": "clientID", "data": "clientID", "targets": 9, visible: false},
                {"name": "transport_company_id", "data": "transport_company_id", "targets": 10, visible: true}
            ]
        }
    );
    {
        function selectCurrentUserRole() {
            var userRoleRusName = $usersDataTable.row($('#usersTable .selected')[0]).data()['userRoleRusName'];
            var selectizeInstance = usersEditor.field('userRoleRusName').inst();
            var role = selectizeInstance.search(userRoleRusName);
            selectizeInstance.setValue(role.items[0].id, true);
            enableColumnsByRole(role.items[0].id)
        }

        function enableColumnsByRole(currentRole) {
            if (currentRole === "CLIENT_MANAGER") {
                usersEditor.field('pointName').disable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').enable();
                usersEditor.field('clientID').set('');
                usersEditor.field('transport_company_id').disable();
                usersEditor.field('transport_company_id').set('');
            }

            if (currentRole === "TEMP_REMOVED") {
                usersEditor.field('pointName').enable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').enable();
                usersEditor.field('clientID').set('');
                usersEditor.field('transport_company_id').disable();
                usersEditor.field('transport_company_id').set('');
            }

            if (currentRole === "ADMIN" || currentRole === "MARKET_AGENT") {
                usersEditor.field('pointName').disable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').disable();
                usersEditor.field('clientID').set('');
                usersEditor.field('transport_company_id').disable();
                usersEditor.field('transport_company_id').set('');
            }

            if (currentRole === "DISPATCHER" || currentRole === "W_DISPATCHER") {
                usersEditor.field('pointName').enable();
                usersEditor.field('pointName').set('');
                usersEditor.field('clientID').disable();
                usersEditor.field('clientID').set('');
                usersEditor.field('transport_company_id').disable();
                usersEditor.field('transport_company_id').set('');
            }

            if (currentRole ==="TRANSPORT_COMPANY"){
                usersEditor.field('transport_company_id').enable();
                usersEditor.field('transport_company_id').set('');
                usersEditor.field('clientID').disable();
                usersEditor.field('clientID').set('');
                usersEditor.field('pointName').disable();
                usersEditor.field('pointName').set('');
            }
        }
    }

    $.post("content/getData.php",
        {status: "getCompanyPairs", format: "json"},
        function (companiesData) {
            let selectizeOptions = [];
            // console.log(companiesData);
            companiesData = JSON.parse(companiesData);

            companiesData.forEach(function (entry) {
                text = entry.name;
                let selectizeOption = {text:text, value:entry.id};
                selectizeOptions.push(selectizeOption);
            });

            let transportCompanyInput = usersEditor.field('transport_company_id');
            transportCompanyInput.inst().load(function (callback) {
                callback(selectizeOptions);
                console.log(selectizeOptions);
            });
        }
    )
});
