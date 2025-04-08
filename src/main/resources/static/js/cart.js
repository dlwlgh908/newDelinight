$(function () {

    showList();


    function showList() {
        $.ajax({

            url: "/cart/showList",
            type: "GET",
            success: function (result) {
                cartList(result)
            }

        })

    }

    $(document).on("click", ".plusQuantity", function () {


        let cartItemNum = $(this).closest("li").data("cartitemnum")
        $.ajax({

            url: "/cart/plusQuantity",
            type: "post",
            data: {"cartItemNum": cartItemNum},
            success: function (result) {
                alert(result)
                showList()
            },
            error: function () {
                alert("실패")
            }
        })
    })
    $(document).on("click", ".minusQuantity", function () {


        let cartItemNum = $(this).closest("li").data("cartitemnum")
        $.ajax({

            url: "/cart/minusQuantity",
            type: "post",
            data: {"cartItemNum": cartItemNum},
            success: function (result) {
                alert(result)
                showList()
            },
            error: function () {
                alert("실패")
            }
        })
    })

    function cartList(carts) {
        $(".cartList").empty();
        let totalPrice = 0;
        let groupedCarts = {};

        carts.forEach(function (cart) {
            let storeName = cart.menuDTO.storeDTO.name;
            if(!groupedCarts[storeName]){
                groupedCarts[storeName] = [];
            }
            groupedCarts[storeName].push(cart);
            totalPrice += (cart.menuDTO.price) * (cart.quantity)

        })
        Object.keys(groupedCarts).forEach(function (storeName){
            let storeSection = $('<div class = "storeSection"></div>');
            storeSection.append('<h3>'+storeName+'</h3>');
            let itemList = $('<ul class = "storeItemList"></ul>');

            groupedCarts[storeName].forEach(function (cart){
                let cartItem = '<li data-cartitemnum="' + cart.id + '" data-quantity="' + cart.quantity + '">' +
                    '<span class="menuName">' + cart.menuDTO.name + '</span>' +
                    '<span class="menuContent">' + cart.menuDTO.content + '</span>' +
                    '<span class="menuQuantity">' +
                    '<button type="button" class="minusQuantity">-</button>' + cart.quantity +
                    '<button type="button" class="plusQuantity">+</button>' +
                    '</span>' +
                    '<button type="button" data-cartitemnum="' + cart.id + '" class="cartItemDelete">삭제하기</button>' +
                    '</li>';
                itemList.append(cartItem);
            })
            storeSection.append(itemList);
            $(".cartList").append(storeSection); // 메인 리스트에 추가
        })
        $(".totalPrice").html(totalPrice + "원")

    }

    $(document).on("click", ".cartAdd", function () {

        let selectedMenu = $(this).data("menunum")
        $.ajax({
            url: "/cart/addToCart",
            type: "POST",
            data: {"menuNum": selectedMenu},
            success: function (result) {
                alert(result)
            }

        })

    })
    $(".clearBtn").on("click", function () {

        $.ajax({
            url: "/cart/clear",
            type: "POST",
            success: function () {
                showList()
            }


        })

    })

    $(document).on("click", ".cartItemDelete", function () {


        let selecetedCartItem = $(this).data("cartitemnum")
        alert(selecetedCartItem)
        $.ajax({
            url: "/cart/removeFromCart",
            type: "POST",
            data: {"cartItemNum": selecetedCartItem},
            success: function (result) {
                showList();
            }

        })
    })

    $(".orderBtn").on("click", function () {

        $.ajax({

            url: "/cart/cartToOrder",
            type: "post",
            success:function (result){
                location.reload();

            }

        })

    })
})