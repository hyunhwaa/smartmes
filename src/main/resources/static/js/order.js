function confirmed(orderNo){
    console.log("1111111111");
    console.log(orderNo);
    $.ajax({
        url:'updateStatus',
        type: 'post',
        data: {"order_no" : orderNo},
        success: function(){
            alert("확정 되었습니다")
            location.href ="order";
        },
        error: function(){
            alert("에러");
        }
    });
}

function saveOrder(){
    //수주일
    let orderDate = document.getElementById("orderDate").value;
    // let orderDate = new Date(oderDateInput).toISOString().slice(0, 16); // ISO 8601 형식으로 변환
    //제품ID
    let select = document.getElementById("selectedBox");
    let productId = select.options[select.selectedIndex].value;
    //거래처
    let companySelect = document.getElementById("company");
    let company = companySelect.options[companySelect.selectedIndex].value;
    //개수
    let orderQty = document.getElementById("orderQuantity").value;

    // 납품 예정일
    let deliveryDate = document.getElementById("deliveryDate").value;
    // let deliveryDate = new Date(deliveryDateInput).toISOString().slice(0, 16); // ISO 8601 형식으로 변환
    console.log(orderDate);
    console.log(productId);
    console.log(orderQty);
    console.log(deliveryDate);


    $.ajax({
        url:'/mes/addOrder',
        type: 'post',
        data:{
            "orderDate" : orderDate,
            "companyId" : company,
            "productId" : productId,
            "orderQuantity" : orderQty,
            "deliveryDate" : deliveryDate
        },
        success: function (){
            location.href = "order";
        },
        error: function (){
            alert("에러");
        }

    });

}