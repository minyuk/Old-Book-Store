const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList();
});


function loadList(){


    $.ajax({
        url: "/api/orders?"+urlParams,
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



function addTR(orders){

    var result = "";
    orders.forEach(function(order){
        var sum = 0;
        var content = ``;

        for(var i=0; i<order.orderItemResponseDtos.length; i++){
            var post = order.orderItemResponseDtos[i];
            var hr = `<hr>`;
            sum = sum + (post.itemPrice * post.count);

            if(i == order.orderItemResponseDtos.length -1){
                hr = "";
            }

            content = content + `
            <div><a href="/item/${post.itemId}">${post.name}</a></div>
            <div class="won">판매가격 : ${addComma(post.itemPrice)}</div>
            <div class="ea">구매수량 : ${post.count}</div>
            ${hr}
            `;
        }

        result = result + `
        <tr>
            <td class="enter"><a href="/order/${order.id}" style="text-decoration:none;">${order.id}</a></td>
            <td>
                주문일시 : <span>${order.orderDate}</span>
                <hr>
                ${content}
            </td>
            <td class="won">${addComma(sum)}</td>
        </tr>
        `;

    });


    return result;
}
