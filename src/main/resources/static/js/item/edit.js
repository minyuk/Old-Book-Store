const baseUrl = window.location.pathname;           //posts    //posts/novel
var id = baseUrl.replace("/item/edit/", "");

//detail.js와 유사
$(document).ready(function(){
    loadPost();
});

//detail.js와 같음
function loadPost(){
    $.ajax({
        url: "/api/items/"+id,
        type: "get",
        success: function(data){
            replacePost(data);
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/item/list");
        }
    });
}


function replacePost(post){
    document.querySelector("#name").value = post.name;
    document.querySelectorAll("#category option").forEach(function(option){
        if(option.innerHTML == post.category){
            option.selected = true;
        }
    });
    document.querySelector("#bookTitle").value = post.bookTitle;
    document.querySelector("#bookAuthor").value = post.bookAuthor;
    document.querySelector("#price").value = post.price;
    document.querySelector("#stock").value = post.stock;
    document.querySelectorAll("#saleStatus option").forEach(function(option){
        if(option.innerHTML == post.saleStatus){
            option.selected = true;
        }
    });
    document.querySelector("#contents").value = post.contents;

    files.init(post.files);
}

//write.js와 유사
const fields = ["name", "category", "bookTitle", "bookAuthor", "price", "stock", "saleStatus", "contents", "fileList"];
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
    //console.log(files);
    files.map.forEach(function(v, k){
        formData.append("saveFileList", v);
    });
    //기존 이미지는 v에 downloadImage가 저장되어있지만 컨트롤러에서 받을 땐 Multipart타입이 아니기때문에 null로 표시된다.

    //write.js에서 이부분만 추가됨
    //console.log(files.removeArr);
    formData.append("removeFileList", new Blob([JSON.stringify(files.removeArr)] , {type: "application/json"}));

    //data json으로 저장
    var data = {
        "name" : $.trim($("#name").val()),
        "category" : $.trim($("#category option:selected").val()),
        "bookTitle" : $.trim($("#bookTitle").val()),
        "bookAuthor" : $.trim($("#bookAuthor").val()),
        "price" : $.trim($("#price").val()),
        "stock" : $.trim($("#stock").val()),
        "saleStatus" : $.trim($("#saleStatus option:selected").val()),
        "contents" : $.trim($("#contents").val())
    }
    formData.append("jsonData", new Blob([JSON.stringify(data)] , {type: "application/json"}));


    $.ajax({
        url: "/api/items/"+id,
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        enctype : 'multipart/form-data',
        success: function(data){
            validUtil.successProcess();
            window.location.replace("/item/"+id);
        },
        error: function(error){
            validUtil.errorProcess(error.responseJSON);
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

