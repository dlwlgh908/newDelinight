$(function () {


    $(document).on("click",".cartAdd", function () {

        let selectedMenu = $(this).data("menunum")
        $.ajax({
            url:"/cart/addToCart",
            type:"POST",
            data : {"menuNum":selectedMenu},
            success : function (result) {
                alert(result)
            },
            error:function () {
                alert("에러발생")
            }

        })

    })

})