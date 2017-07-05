$(document).ready(function () {
    window.localStorage.removeItem("USER_STATUSES");

    var $loginInput = $("#loginInput");
    var $passwordInput = $("#passwordInput");
    var $loginButton = $("#loginButton");

    $passwordInput.add($loginInput).keypress(function(e) {
        if(e.which == 13) {
            $loginButton.click();
        }
    });

    $loginButton.click(function () {

        var login = $loginInput.val();

        var password = $passwordInput.val();

        // Checking for blank fields.
        if (login == "" || password == "") {
            var $inputs = $($loginInput, $passwordInput); // get array of inputs
            $inputs.addClass("login_error");
            $("#loginErrorContainer").text("Пожалуйста заполните все поля");
        } else {
            $.post("content/getData.php", {login: login, password: password},
                function (data) {
                    var result = JSON.parse(data);

                    function handleInvalidLogin() {
                        $('input[type="text"]').css({"border": "2px solid red", "box-shadow": "0 0 3px red"});
                        $('input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        data = JSON.parse(data);
                        alert(data.responseCode);
                    }

                    function handleValidLogin() {
                        $("form")[0].reset();
                        $('input[type="text"],input[type="password"]').css({
                            "border": "2px solid #00F5FF",
                            "box-shadow": "0 0 5px #00F5FF"
                        });
                        window.localStorage.setItem("USER_STATUSES", JSON.stringify(result.statuses));
                        // document.location = history.go(-1);
                        // window.open('url','_parent');
                        // window.history.back()
                        // console.log(JSON.stringify(history));
                        document.location = '/'+window.location.search;
                    }

                    if (result.responseCode === "Ошибка авторизации - неверные имя пользователя или пароль") {
                        handleInvalidLogin();
                    } else if (result.responseCode === "") {
                        handleValidLogin();
                    } else {
                        alert(data);
                    }
                });
        }
    });
});
