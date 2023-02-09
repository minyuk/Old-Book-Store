var id = $("#id").text();

function nickNameUpdate(){
    var data = { nickname: $('#nickname').val() };
    var newNickname = $("#nickname").val();

    $.ajax({
        type:"patch",
        url:"/api/users/nickname",
        contentType: 'application/json',
        data:JSON.stringify(data),
        success: function(lists){
            $("#nickname").val(newNickname);
            $(".navbar .login-position a span").text(newNickname);
            alert("닉네임이 수정되었습니다.");
        },
        error: function(error){
            alert(JSON.stringify(error["responseJSON"].message));
        }
    });
}


//공백제거
$("#nickname").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});
