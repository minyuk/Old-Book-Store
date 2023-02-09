const categories = ["it", "development", "novel", "cartoon"];
const cardCount = 10;
const hostAndPort = window.location.host;

$(document).ready(function(){
    saveList();
});

function saveList(){
    $.ajax({
        url: "/api/items/index",
        type: "get",
        success: function(categoryList){
            for(var key in categoryList){
                replaceSlide(key, categoryList[key]);
            }
            initCardSlider();
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/");
        }
    });
}

function replaceSlide(category, postList){

    var result = ``;
    for(var i=0; i<cardCount; i++){
        var name = "아직 게시글이 없어요😥";
        var price = "";
        var imgsrc = "https://plchldr.co/i/245x180";
        var postlink = "#";

        if(i < postList.length){    //게시글이 10개미만이면 swiper 오류생김! 게시글이 있는 것만 내용 추가해준다
            name = postList[i].name;
            price = postList[i].price +"원";
            var fileName = postList[i].fileName;
            if(fileName.length != 0){
                imgsrc = "/api/image/"+fileName;
            }
            postlink = "/item/"+postList[i].id;
        }

        result = result + `
        <a class="swiper-slide" href="${postlink}">
            <img class="swiper-lazy" src="${imgsrc}">
            <div class="card-body">
                <span>${name}</span><br>
                <span>${price}</span>
            </div>
        </a>
        `;
    }

    $("."+category+" .card-slider .swiper .swiper-wrapper").append(result);
}




function searchFormSubmit(){
    event.preventDefault(); //submit시 queryString이 모두 사라지게되는 것 방지
    //https://ejolie.dev/posts/form-submission-algorithm 참고

    let value = document.querySelector("#searchValue").value;

    let url = new URLSearchParams(location.search);
    url.set("keyword", value);
    url.set("page", 1);

    if(value == null || value == ""){
        url.delete("keyword");
    }

    window.location.href = "/item/list" + "?" + url;
}
