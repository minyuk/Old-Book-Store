const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList(null, null);
});

$("#name, #saleStatus, #createdDate, #viewCount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){
    if(e_id != null){
        urlParams.set("sort", e_id+","+e_class);
    }

    $.ajax({
        url: "/api/items/my?"+urlParams,
        type: "get",
        success: function(data){

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.content));
            $(".pagination").append(addPagination(data));
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
                <td>${post.createdDate}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}
