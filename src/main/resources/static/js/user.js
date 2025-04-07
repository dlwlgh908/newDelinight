$(document).ready(function () {
    $("#userForm").submit(function (event) {
        event.preventDefault(); // 기본 제출 방지

        let isValid = true;

        // 정규식 패턴
        const usernamePattern = /^[a-zA-Z가-힣0-9]{2,12}$/; // 한글, 영문, 숫자 허용 (2~12자)
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // 이메일 형식
        const phonePattern = /^010-\d{4}-\d{4}$/; // 010-1234-5678 형식
        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/; // 최소 8자, 숫자+문자 포함

        // 사용자명 검사
        let username = $(".username").val();
        if (!usernamePattern.test(username)) {
            $("#usernameError").text("사용자명은 2~12자, 한글/영문/숫자만 가능합니다.");
            isValid = false;
        } else {
            $("#usernameError").text("✅").removeClass("error").addClass("success");
        }

        // 이메일 검사
        let email = $(".email").val();
        if (!emailPattern.test(userEmail)) {
            $("#emailError").text("유효한 이메일을 입력하세요.");
            isValid = false;
        } else {
            $("#emailError").text("✅").removeClass("error").addClass("success");
        }

        // 전화번호 검사
        let phone = $("#phone").val();
        if (!phonePattern.test(userPhone)) {
            $("#phoneError").text("전화번호 형식: 010-1234-5678");
            isValid = false;
        } else {
            $("#phoneError").text("✅").removeClass("error").addClass("success");
        }

        // 비밀번호 검사
        let password = $(".password").val();
        if (!passwordPattern.test(userPassword)) {
            $("#passwordError").text("비밀번호는 최소 8자, 숫자와 문자를 포함해야 합니다.");
            isValid = false;
        } else {
            $("#passwordError").text("✅").removeClass("error").addClass("success");
        }

        // 모든 검사를 통과하면 폼 제출
        if (isValid) {
            alert("회원가입 완료!");
            this.submit(); // 실제 폼 제출
        }
    });
});

$(function () {

    const usernamePattern = /^[a-zA-Z가-힣0-9]{2,12}$/; // 한글, 영문, 숫자 허용 (2~12자)
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // 이메일 형식
    const phonePattern = /^010-\d{4}-\d{4}$/; // 010-1234-5678 형식
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/; // 최소 8자, 숫자+문자 포함

    // 유효성 검사
    $(".singUpBtn").on("click" , function () {

        let check = true

        let singUpName = $(".username").val();
        if (usernamePattern.test(singUpName)){
            $("#singUpNameError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#singUpNameError").text("사용자 이름이 올바르지 않습니다.").removeClass("success").addClass("error");
            check = false
        }

        let singUpEmail = $(".singUpEmail").val();
        if (emailPattern.test(singUpEmail)){
            $("#singUpEmailError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#singUpEmailError").text("이메일 형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            check = false
        }



        let singUpPassword = $(".singUpPassword").val();
        if (passwordPattern.test(singUpPassword)){
            $("#singUpPasswordError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#singUpPasswordError").text("비밀번호는 최소 8자, 숫자와 문자를 포함해야 합니다.").removeClass("success").addClass("error");
            check = false
        }

        let singUpPhone = $(".singUpPhone").val();
        if (phonePattern.test(singUpPhone)){
            $("#singUpPhoneError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#singUpPhoneError").text("사용자 휴대폰번호가 올바르지 않습니다.").removeClass("success").addClass("error");
            check = false
        }


        // 모든 검사를 통과하면 폼 제출
        if (check) {
            alert("회원가입 완료!");
            $(".singUpForm").submit(); // 실제 폼 제출
        }




    })

    $(".sendBtn").on("click" , function () {

        let checked = true


        let sendPasswordId = $(".sendPasswordId").val();
        if (emailPattern.test(sendPasswordId)){
            $("#sendPasswordIdError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#sendPasswordIdError").text("아이디 형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            checked = false
        }

        let sendPasswordName = $(".sendPasswordName").val();
        if (usernamePattern.test(sendPasswordName)){
            $("#sendPasswordNameError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#sendPasswordNameError").text("아이디 형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            checked = false
        }

        if (checked) {
            alert("임시 비밀번호 발급 완료!");
            $(".sendForm").submit(); // 실제 폼 제출
        }

    });

    $(".changeBtn").on("click" , function () {

        let changCheck = true
        let sendPasswordId = $(".sendPasswordId").val().trim();
        let password = $("input[name='password']").val().trim();
        let passwordOne = $("input[name='passwordOne']").val().trim();
        let passwordTwo = $("input[name='passwordTwo']").val().trim();


        // 이메일 유효성 검사
        let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (emailPattern.test(sendPasswordId)) {
            $("#sendPasswordIdError").text("✅").addClass("success").removeClass("error");
        } else {
            $("#sendPasswordIdError").text("이메일 형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            changCheck = false;
        }

        // 기존 비밀번호 입력 여부 확인
        if (password === "") {
            $("#passwordError").text("기존 비밀번호를 입력하세요.").addClass("error").removeClass("success");
            changCheck = false;
        } else {
            $("#passwordError").text("✅").removeClass("error").addClass("success");
        }

        // 새 비밀번호 복잡성 검사 (8자 이상, 대소문자, 숫자, 특수문자 포함)
        let passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!passwordPattern.test(passwordOne)) {
            $("#passwordOneError").text("비밀번호는 8자 이상, 대소문자, 숫자, 특수문자를 포함해야 합니다.").addClass("error").removeClass("success");
            changCheck = false;
        } else {
            $("#passwordOneError").text("✅").addClass("success").removeClass("error");
        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (passwordOne !== passwordTwo) {
            $("#passwordTwoError").text("비밀번호가 일치하지 않습니다.").addClass("error").removeClass("success");
            changCheck = false;
        } else {
            $("#passwordTwoError").text("✅").addClass("success").removeClass("error");
        }


        if (changCheck) {
            alert("비밀번호 변경 완료!");
            $(".changForm").submit(); // 실제 폼 제출
        }


    })

    $(".updateBtn").on("click" , function () {

        let updateCheck = true
        let phonePattern = /^010-\d{4}-\d{4}$/; // 010-1234-5678 형식
        let addressPattern = /^[가-힣0-9a-zA-Z\s,\-\.]+$/;

        let phoneUpdate = $(".phoneUpdate").val();
        if (phonePattern.test(phoneUpdate)){
            $("#phoneUpdateError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#phoneUpdateError").text("휴대폰번호 형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            updateCheck = false
        }

        let addressUpdate = $(".addressUpdate").val();
        if (addressPattern.test(addressUpdate)){
            $("#addressUpdateError").text("✅").addClass("success").removeClass("error");
        }else{
            $("#addressUpdateError").text("주소형식이 올바르지 않습니다.").removeClass("success").addClass("error");
            updateCheck = false
        }


        if (updateCheck) {
            alert("회원정보 변경 완료!");
            $(".updateForm").submit(); // 실제 폼 제출
        }
    })
})


