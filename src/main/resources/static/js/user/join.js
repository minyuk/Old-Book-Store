
const fields = ["email", "nickname", "password"];

function registerSubmit() {

    var email = $("#email").val();
    var nickname = $("#nickname").val();
    var password = $("#password").val();

    fetch("/api/users", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({
            "email":email,
            "nickname":nickname,
            "password":password,
        }),
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Something went wrong');
    })
    .then((responseJson) => {
        window.location.replace('/login')
    })
    .catch((error) => {
        console.log(error)
    })

}



$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});