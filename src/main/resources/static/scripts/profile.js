// profile部分
let nickName = $('#nickName');
let note = $('#note');
const originNickName = nickName.attr('placeholder');
const originNote = note.val();

function checkNickName() {
    if (nickName.val() === '' || (nickName[0].checkValidity() && checkName(nickName))) {
        nickName.removeClass('is-invalid');
        return true;
    } else {
        nickName.addClass('is-invalid');
        return false;
    }
}

function valid(button, feedback) {
    button.addClass('is-valid');
    button.removeClass('is-invalid');
    feedback.addClass('valid-feedback');
    feedback.removeClass('invalid-feedback')
}

function invalid(button, feedback) {
    button.addClass('is-invalid');
    button.removeClass('is-valid');
    feedback.addClass('invalid-feedback');
    feedback.removeClass('valid-feedback')
}

$('form.profile').submit(function (e) {

nickName.change(() => checkNickName());
    e.preventDefault();
    if (!checkNickName() || !note[0].checkValidity()) {
        e.stopPropagation();
    } else {
        let formData = {};
        if (nickName.val() !== '' && nickName.val() !== originNickName) {
            formData['nickName'] = nickName.val();
        }
        const newNote = note.val();
        if (originNote !== newNote) {
            formData['note'] = newNote;
        }
        if (formData['nickName'] === undefined && formData['note'] === undefined)
            return;
        $.ajax({
            'url': '/user/profile/update',
            'type': 'post',
            'data': JSON.stringify(formData),
            dataType: "json",
            contentType: "application/json",
            'success': function (msg) {
                let submit = $('#profile-submit');
                let feedback = $('#submit-feedback');
                if (msg.code === 200) {
                    valid(submit, feedback)
                } else {
                    invalid(submit, feedback)
                }
                feedback.text(msg.message);
            }
        });
    }
});

let oldEmail = $('#old-email');
let newEmail = $('#new-email');
let oldPwd = $('#old-password');
let newPwd = $('#new-password');
let repeatPwd = $('#repeat-password');

function checkValidity(selector) {
    if (selector[0].checkValidity()) {
        selector.removeClass('is-invalid');
        return true;
    } else {
        selector.addClass('is-invalid');
        return false;
    }
}

function checkRepeatPwd() {
    if (repeatPwd[0].checkValidity()) {
        if (newPwd.val() === repeatPwd.val()) {
            repeatPwd.removeClass('is-invalid');
            return true;
        }
        repeatPwd.addClass('is-invalid');
        repeatPwd.next('div').text('重复密码与新密码不同')
    } else {
        repeatPwd.addClass('is-invalid');
        repeatPwd.next('div').text('密码格式错误')
    }
    return false;
}

[oldEmail, newEmail, oldPwd, newPwd].map(
    selector => selector.change(() => checkValidity(selector))
);

repeatPwd.change(() => checkRepeatPwd());

$('#email-form').submit(function(e) {
    e.preventDefault();
    if (!(checkValidity(oldEmail) * checkValidity(newEmail))) {
        return
    }
    const oldEmailTxt = oldEmail.val();
    const newEmailTxt = newEmail.val();
    let submit = $('#email-submit');
    let feedback = $('#email-feedback');
    if (oldEmailTxt !== newEmailTxt) {
        $.post('/user/profile/updateEmail', {'oldEmail': oldEmailTxt, 'newEmail': newEmailTxt},
            function (msg) {
                if (msg.code === 200) {
                    valid(submit, feedback)
                } else {
                    invalid(submit, feedback)
                }
                feedback.text(msg.message);
            })
    } else {
        invalid(submit, feedback);
        feedback.text('两次邮箱地址相同');
    }
});

$('#password-form').submit(function(e) {
    e.preventDefault();
    if (!(checkValidity(oldPwd) * checkValidity(newPwd) * checkRepeatPwd())) {
        return
    }
    const oldPwdTxt = oldPwd.val();
    const newPwdTxt = newPwd.val();
    let submit = $('#password-submit');
    let feedback = $('#password-feedback');
    if (oldPwdTxt !== newPwdTxt) {
        $.post('/user/profile/updatePwd', {'oldPwd': oldPwdTxt, 'newPwd': newPwdTxt},
            function (msg) {
                if (msg.code === 200) {
                    alert('密码修改成功，请重新登陆');
                    location.href = '/login'
                } else {
                    invalid(submit, feedback);
                    feedback.text(msg.message)
                }
            })
    } else {
        invalid(submit, feedback);
        feedback.text('两次密码相同')
    }
});
