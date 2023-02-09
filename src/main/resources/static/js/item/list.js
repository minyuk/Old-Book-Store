const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList(null, null);
});

$("#name, #seller, #saleStatus, #createdDate, #viewCount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){
    if(e_id != null){
        if(e_id != null){
            urlParams.set("sort", e_id+","+e_class);
        }
    }

    $.ajax({
        url: "/api/items?"+ urlParams,
        type: "get",
        success: function(data){
            $(".head .categoryName").text(data.category);

            if(urlParams.get("keyword") != null){
                replaceSearch();
            }

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.pagination.content));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}

function addTR(posts){
    var result = "";
    posts.forEach(function(post){
        result = result + `
            <tr>
                <th scope="row">${post.id}</th>
                <td>${post.category}</td>
                <td style="text-overflow: ellipsis;">
                    <a href="/item/${post.id}">${post.name}</a><span> (<span>${post.commentCount}</span>)</span>
                </td>
                <td>${post.saleStatus}</td>
                <td>${post.seller}</td>
                <td>${post.createdDate}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}



function replaceSearch(){
    let search = urlParams.get("keyword");
    let searchValue = search;

    document.querySelector("#searchValue").value = searchValue;
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

    window.location.href = baseUrl + "?" + url;
}


$("#searchValue").on("keyup", function(event){
    if(event.keyCode == 13){
        searchFormSubmit();
    }
});