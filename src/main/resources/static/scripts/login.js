$(document).ready(function() {
    $('.form-signin').on('submit', function(event) {
        if (!this.checkValidity() || !checkName($('#username'))) {
            event.preventDefault();
            event.stopPropagation();
        }
        this.classList.add('was-validated');
    });

    $('#username, #nickName').on('change', function() {
        if (this.checkValidity() && checkName($(this))) {
            this.classList.remove('is-invalid')
        } else {
            this.classList.add('is-invalid');
        }
    });

    $('#password').on('change', function() {
        if (!this.checkValidity()) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid')
        }
    }) ;
});
