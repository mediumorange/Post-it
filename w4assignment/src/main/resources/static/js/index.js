let myUserId
let myUserKey
let myUserName

$(document).ready(function () {
    getPosts();
    getInfo();
})

// Header에 토큰 넣어두기
function header() {
    const token = $.cookie("accessToken");
    const retoken = $.cookie("refreshToken");
    retoken != null ?
        $.ajaxSetup({
            headers: {"Authorization": "Bearer " + token}
        }) : window.location.href="/users/login";
}

function getInfo() {
    header();
    $.ajax({
        type: "GET",
        url: `/users/me`,
        contentType: "application/json",
        success: function (response) {
            myUserId = response["userId"];
            myUserKey = response["userKey"];
            myUserName = response["userName"];
        },
        error: function (error) {
            reissue(error) ? getInfo()
                : window.location.replace("users/login");
        }
    })
}

// Access Token 재발급
function reissue(error) {
    let isTrue;
    let accessToken = $.cookie("accessToken");
    let refreshToken = $.cookie("refreshToken");
    let data = {"accessToken": accessToken, "refreshToken": refreshToken};
    $.ajax({
        type: "POST",
        url: "/users/reissue",
        contentType: "application/json", // JSON 형식으로 전달함을 알리기
        data: JSON.stringify(data),
        success: function (response, testStatus, request) {
            const jwtAccessToken = request.getResponseHeader("Access-Token");
            if (jwtAccessToken) {
                $.cookie("accessToken", jwtAccessToken, {path: "/"});
                $.ajaxSetup({
                    headers: {
                        "Authorization": "Bearer " + jwtAccessToken
                    }
                });
                isTrue = true;
            } else {
                isTrue = false;
            }
        }
    });
    return Boolean(isTrue);
}

function isValidContents(contents) {
    if (contents === '') {
        alert('제대로 입력해주세요');
        return false;
    }
    return true;
}

/////////////////////////////////////////////////////////////////////////////////////

// 기존 포스트 목록 가져오기
function getPosts() {
    $.ajax({
        type: 'GET',
        url: '/posts',
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let post = response[i];
                let postKey = post['postKey'];
                let userKey = post['userKey'];
                let userName;
                $.ajax({
                    type: "GET",
                    url: `/users/${userKey}`,
                    success: function (response) {
                        userName = response["userName"];
                    }
                });
                let postTitle = post['postTitle'];
                let modifiedAt = post['modifiedAt'];
                addPostList(postKey, userKey, userName, postTitle, modifiedAt);
            }
        }
    })
}

// 포스트 목록 붙이기
function addPostList(postKey, userKey, userName, postTitle, modifiedAt) {
    let temphtml = `<div class="item">
							<div class="content" onclick='$(".details").addClass("on"); getDetails("${postKey}")'>
								<div class="usernameDate">
									<div id="${postKey}-userName" class="userName">${userName}</div>
									<div id="${postKey}-date" class="date">${modifiedAt}</div>
								</div>
								<p id="${postKey}-postTitle" class="postTitle">${postTitle}</p>
								</div>
							</div>`;
    $('.postList').append(temphtml);
}

/////////////////////////////////////////////////////////////////////////////////////

// 포스트 생성
function writePost() {
    header();
    let postTitle = $('textarea[name="postTitle"]').val();
    let postContent = $('textarea[name="postContent"]').val();
    if (isValidContents(postContent) === false) {
        return;
    }
    let data = {'postTitle': postTitle, 'postContent': postContent};

    $.ajax({
        type: "POST",
        url: "/posts/post",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.reload();
        },
        error: function (error) {
            reissue(error) ? writePost()
                : alert("다시 로그인해주세요");
        }
    });
}

// 각 포스트 값 가져오기
function getDetails(postKey) {
    header();
    $.ajax({
        type: 'GET',
        url: `/posts/${postKey}`,
        success: function (response) {
            let postKey = response["postKey"];
            let userKey = response["userKey"];
            let userName;
            $.ajax({
                type: "GET",
                url: `/users/${userKey}`,
                success: function (response) {
                    userName = response["userName"];
                }
            });
            let postTitle = response["postTitle"];
            let postContent = response["postContent"];
            addDetails(postKey, userKey, userName, postTitle, postContent);
            addEdit(postKey, userKey);
        }
    })
}

// 포스트 상세 모달창
function addDetails(postKey, userKey, userName, postTitle, postContent) {
    $(`.detailsTitle`).text(postTitle);
    postContent = postContent.replaceAll("\n", "<br>");
    postContent = postContent.replaceAll(" ", "&nbsp;");
    $(`.detailsContent`).html(postContent);
    $(`.detailsUsername`).text(userName);
}

// 작성자 확인
function rightUser(userKey) {
    let isRight;
    $(".detailsEdit").click(function() {
        console.log(myUserKey);
        console.log(userKey);
        if (myUserKey === userKey) {
            isRight = true;
        } else {
            alert("작성자만 이용 가능합니다");
            isRight = false;
        }
    })
    return Boolean(isRight);
}


// 기존 내용 수정 모달창에 전달
function addEdit(postKey, userKey) {
    if (rightUser(userKey)) {
        let postTitle = $(`#${postKey}-postTitle`).text();
        let postContent = $(`.detailsMemo`).html();
        postContent = postContent.replaceAll("<br>", "\n");
        postContent = postContent.replaceAll("&nbsp;", " ");
        let userName = $(`#${postKey}-userName`).text();
        $(`.editTitleArea`).val(postTitle);
        $(`.editMemoArea`).val(postContent);
        $(`.usernameFixed`).text(userName);
        submitEdit(postKey);
        deletePost(postKey);
        $(".editing").addClass("on");
    }
}

// 포스트 수정
function submitEdit(postKey) {
    $(".editButton").click(function () {
        let postTitle = $(`.editTitleArea`).val();
        let postContent = $(`.editMemoArea`).val();
        if (isValidContents(postContent) === false) {
            return;
        }
        let data = {'postTitle': postTitle, 'postContent': postContent};

        $.ajax({
            type: "PATCH",
            url: `/posts/${postKey}/${myUserKey}`,
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                alert('메시지 변경에 성공하였습니다.');
                window.location.reload();
            },
            error: function (error) {
                reissue(error) ? submitEdit()
                    : alert("다시 로그인해주세요");
            }
        });
    })
}

// 포스트 삭제
function deletePost(postKey) {
    $(".delete").click(function () {
        let deleteCheck = confirm("삭제하시겠습니까?");
        if (deleteCheck) {
            $.ajax({
                type: "DELETE",
                url: `/posts/${postKey}/${myUserKey}`,
                success: function (response) {
                    alert('메시지 삭제에 성공하였습니다.');
                    window.location.reload();
                },
                error: function (error) {
                    reissue(error) ? deletePost()
                        : alert("다시 로그인해주세요");
                }
            });
        }
    })
}