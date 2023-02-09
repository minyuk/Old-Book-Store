function registerSubmit() {
    var data = {
        email: $('#email').val(),
        nickname: $('#nickname').val(),
        password: $('#password').val(),
        passwordConfirm : $('#passwordConfirm').val()
    };

    $.ajax({
        type: 'POST',
        url: '/api/users',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(){
        alert('회원가입이 완료되었습니다.');
        window.location.href = '/login';
    }).fail(function(error) {
        alert(JSON.stringify(error["responseJSON"].message));
    });
}

$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});