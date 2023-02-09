const fields = ["name", "category", "bookTitle", "bookAuthor", "price", "stock", "contents", "fileList"];
const validUtil = new ValidUtil(fields);

function editFormSubmit(){
    //이미지 체크 (slider.js)
    var imgCheck = files.sizeMinCheck();
    if(imgCheck == false){
        return;
    }

    var form = $("#editForm")[0];
    var formData = new FormData(form);

    //(slider.js) 파일 저장
    files.map.forEach(function(v, k){
        formData.append("fileList", v);     //fileList란 이름으로 file목록 저장
    });

    //data json으로 저장
    var data = {
        "name" : $.trim($("#name").val()),
        "category" : $.trim($("#category option:selected").val()),
        "bookTitle" : $.trim($("#bookTitle").val()),
        "bookAuthor" : $.trim($("#bookAuthor").val()),
        "price" : $.trim($("#price").val()),
        "stock" : $.trim($("#stock").val()),
        "contents" : $.trim($("#contents").val())
    }
    formData.append("jsonData", new Blob([JSON.stringify(data)] , {type: "application/json"}));     //jsonData란 이름으로 게시글 내용 저장

    $.ajax({
        url: "/api/items",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        enctype : 'multipart/form-data',
        success: function(data){
//            validUtil.successProcess();
            window.location.replace('/item/'+data);
        },
        error: function(error){
            validUtil.errorProcess(error.responseJSON);
            //console.log(error.responseJSON);
            //console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });

}


$("#name, #category, #bookTitle, #bookAuthor, #price, #stock, #contents").on("click", function(){
    if($(this).hasClass("is-invalid")){
        $(this).removeClass("is-invalid");

        var thisid = $(this).attr("id");
        $("#"+thisid+"Help").removeClass("ani");
    }
});