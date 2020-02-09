$(document).ready(function() {
    let username = $('#username');
    let nickName = $('#nickName');

    $('#register').on('submit', function(event) {
        if (!this.checkValidity() || !checkName(username) || !checkName(nickName)) {
            event.preventDefault();
            event.stopPropagation();
        }
        this.classList.add('was-validated');
    });

    username.on('change', function() {
        if (this.checkValidity() && checkName(username)) {
            $.post('/hasUsername', {username: this.value}, (msg) => {
                if (msg.code === 200) {
                    if (msg.data) {
                        $(this).next('div').text('用户名已存在');
                        this.classList.add('is-invalid');
                    } else {
                        this.classList.remove('is-invalid');
                    }
                }
            })
        } else {
            $(this).next('div').text('由4-16个字符内的字母中文或数字组成');
            this.classList.add('is-invalid')
        }
    });

    $('#email').on('change', function() {
        if (this.checkValidity()) {
            $.post('/hasEmail', {email: this.value}, (msg) => {
                if (msg.code === 200) {
                    if (msg.data) {
                        $(this).next('div').text('邮箱已注册');
                        this.classList.add('is-invalid');
                    } else {
                        this.classList.remove('is-invalid');
                    }
                }
            })
        } else {
            $(this).next('div').text('邮箱格式不合法');
            this.classList.add('is-invalid')
        }
    });

    nickName.on('change', function() {
        if (this.checkValidity() && checkName(nickName)) {
            this.classList.remove('is-invalid')
        } else {
            this.classList.add('is-invalid');
        }
    });

    $('#password').on('change', function() {
        if (this.checkValidity()) {
            this.classList.remove('is-invalid')
        } else {
            this.classList.add('is-invalid');
        }
    });

    $('#repeat-password').on('change', function () {
        if (this.checkValidity()) {
            if (this.value !== $('#password').val()) {
                $(this).next('div').text('两次输入密码不一致');
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        } else {
            $(this).next('div').text('由长度6-16内的字母、数字或特殊字符组成');
            this.classList.add('is-invalid');
        }
    });
});
