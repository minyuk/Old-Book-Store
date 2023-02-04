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
        url: "/api/comments/myList?"+urlParams,
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



function addTR(comments){
    var result = "";
    comments.forEach(function(comment){
        result = result + `
            <tr>
                <th scope="row">${comment.itemId}</th>
                <td>${comment.saleStatus}</td>
                <td>
                    <a href="/item/${comment.itemId}">${comment.itemName}</a>
                </td>
                <td>${comment.contents}</td>
                <td>${comment.createdDate}</td>
            </tr>
            `;
    });
    return result;
}
