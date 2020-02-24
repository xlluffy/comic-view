const comicId = location.pathname.substring(location.pathname.lastIndexOf('/') + 1);
let commentsSelector = $('.comments');

function formatDate(dateString) {
    let date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.getHours() + ':' +date.getMinutes()
}

function appendReplyBefore(selector, reply) {
    let replyText;
    if (reply.reply == null) {
        replyText = '<span>' + reply.text + '</span>';
    } else {
        replyText = '<span>回复<a href="/user/{0}/favourite">{1}</a><span>&nbsp;:{2}</span>'
            .format(reply.user.id, reply.user.username, reply.text);
    }
    selector.before($(('<div class="reply">' +
        '<div class="user-face-reply pr-2"><img src="{0}" alt="{0}"></div>' +
        '<div>' +
        '<div class="mb-1">'+
        '   <span class="pr-2"><a href="/user/{1}/favourite" class="username">{2}</a></span>' +
        replyText + '</div>' +
        '<div class="text-muted reply-info">' +
        '   <span>{3}</span>' +
        '   <span class="ml-3 btn-reply-wrap" data="{&quot;commentId&quot;: {4}, &quot;replyId&quot;:{5}}" href="#comment{4}">回复</span>' +
        '   </div></div></div>')
        .format(reply.user.icon, reply.user.id, reply.user.username, formatDate(reply.createTime), reply.comment.id, reply.id)));
}

function appendComment(comment) {
    commentsSelector.prepend($(('<div class="comment pr-4 border-bottom">' +
        '<div class="user-face pr-4"><img src="{0}" alt="{0}"></div>' +
        '<div class="comment-details">' +
        '    <div class="mb-2"><a href="/user/{1}/favourite" class="username">{2}</a></div>' +
        '    <div class="mb-1">{3}</div>' +
        '    <div class="text-muted reply-info">' +
        '        <span>{4}</span>' +
        '        <span class="ml-3 btn-reply-wrap" data="{&quot;commentId&quot:{5}}" href="#comment{5}">回复</span>' +
        '<div class="comment-area" id="comment{5}"></div>' +
        '</div></div></div>')
        .format(comment.user.icon, comment.user.id, comment.user.username,
            comment.text, formatDate(comment.createTime), comment.id)));

}

$('.btn-comment').click(function() {
    let textarea = $(this).prev('textarea');
    if (!textarea[0].checkValidity())
        return;
    $.ajax('/comment', {
        type: 'put',
        data: JSON.stringify({comic: {id: comicId}, text: textarea.val()}),
        dataType: "json",
        contentType: "application/json",
        success: function(msg) {
          if (msg.code === 200) {
              appendComment(msg.data)
          } else {
          }
        }
    })
});

commentsSelector.on('click', '.btn-reply-wrap', function() {
    // TODO: 回复评论中的回复时提供提示
    $($(this).attr('href')).empty().append('<div class="user-face pr-4">' +
        '  <img src="{0}" alt="{1}">'.format(user.icon, user.icon) +
        '        </div>' +
        '        <label for="comment" class="sr-only">评论</label>' +
        '        <textarea class="form-control comment-text mr-4" placeholder="写下你的评论..."' +
        '        required maxlength="500" rows="2" cols="8" autocomplete="off"></textarea>')
        .append($('<button class="btn btn-primary btn-reply align-self-center">评论</button>').attr('data', $(this).attr('data')))
        .find('textarea').focus();
});

$('.comment-area').on('click', '.btn-reply', function() {
    let textarea = $(this).prev('textarea');
    if (!textarea[0].checkValidity())
        return;
    const data = JSON.parse($(this).attr('data'));
    $.ajax('/comment/reply', {
        type: 'put',
        data: JSON.stringify({reply: data["replyId"] === undefined ? null : {id: data['replyId']},
        comment: {id: data['commentId']}, text: textarea.val()}),
            dataType: "json",
            contentType: "application/json",
            success: function(msg) {
                if (msg.code === 200) {
                    let commentArea = textarea.parent();
                    let pag = commentArea.prev('.list-inline');
                    if (pag.length === 0) {
                        appendReplyBefore(commentArea, msg.data);
                    } else {
                        appendReplyBefore(pag, msg.data);
                    }
                } else {
                }
            }
    })
});

$('.pag-reply').click(function () {
    let selector = $(this).parent();
    const data = JSON.parse($(this).attr('data'));
    if (selector.hasClass('active') )
        return;

    $.get('/comment/' + data['id'] + '/replies',
        {pageNum: data['pageNum'], pageSize: data['pageSize']},
        function (msg) {
            if (msg.code === 200) {
                selector.addClass('active')
                  .siblings('.active').removeClass('active');
                let pagReply = selector.parent();
                let details = pagReply.parent();
                details.children('.reply').remove();

                const replies = msg.data.list;
                const length = replies.length;
                for (let i = 0; i < length; i++) {
                    appendReplyBefore(pagReply, replies[i])
                }
                $(document).scrollTop(details.children('.reply')[0].offsetTop);

                  // 调整上一页和下一页
                let pagList = pagReply.children();
                if (msg.data.pageNum === 1) {
                    pagList.eq(0).addClass('disabled');
                } else {
                    pagList.eq(0).removeClass('disabled');
                }
                if (msg.data.pageNum === msg.data.pages) {
                    pagList.eq(-1).addClass('disabled');
                } else {
                    pagList.eq(-1).removeClass('disabled');
                }
            }
        })
});

$('.pag-reply-prev').click(function() {
    const prevPag = $(this).parent();
    if (prevPag.hasClass('disabled'))
        return;
    prevPag.siblings('.active').prev().children().click();
});

$('.pag-reply-next').click(function() {
    const nextPag = $(this).parent();
    if (nextPag.hasClass('disabled'))
        return;
    nextPag.siblings('.active').next().children().click();
});
