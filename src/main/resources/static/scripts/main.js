String.prototype.format = function (args) {
    let result = this;
    if (arguments.length > 0) {
        if (arguments.length === 1 && typeof (args) == "object") {
            for (let key in args) {
                if (args[key] !== undefined) {
                    let reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (let i = 0; i < arguments.length; i++) {
                if (arguments[i] !== undefined) {
                    let reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

Number.prototype.format = function (args) {
    let temp = this.toString();
    let count = temp.length;
    let prefix = "";
    for (let i = 0; i < args - count; i++) {
        prefix += "0";
    }
    return prefix + temp;
};

function getRequest(href) {
    let url = href == null ? location.search.substr(1) : href.substr(href.indexOf('?') + 1);
    let requestPairs = url.split('&');
    let request = [];
    for (let i = 0; i < requestPairs.length; i++) {
        let requestPair = requestPairs[i].split('=');
        request[requestPair[0]] = requestPair[1];
    }
    return request;
}

function splitCookies() {
    let cookies = document.cookie.split(";");
    let cookieMap = {};
    for (let x in cookies) {
        let i = cookies[x].indexOf("=");
        cookieMap[cookies[x].substring(0, i)] = cookies[x].substring(i + 1, cookies[x].length)
    }
    return cookieMap
}

function login() {
    $('.form-signin').submit(function (event) {
        event.preventDefault();
        $.post('/admin/loginAuth', $('.form-signin').serialize(), function (response) {
            if (response.code === 200) {
                localStorage.setItem('Authorization', response.data.tokenHead + ' ' + response.data.token);
                location.href = '/comic/index';
            }
        })
    })
}

function auth() {
    let cookies = splitCookies();
    if (cookies['Authorization'] != null && cookies['Authorization'] !== undefined) {
        $('.account').show();
        $('#login').hide();
    } else {
        $('.account').hide();
        $('#login').show();
    }
}

let pagination = {
    pageNum: 1,
    pageSize: 20,
    pageItems: $('.page-item'),

    /**
     * 调整分页栏的previous, next按钮的href和disabled，以及标记当前按钮为active,
     */
    init: function() {
        if (this.pageItems.length === 0)
            return;

        let requests = getRequest(null);
        if (requests['pageNum'] !== undefined) {
            this.pageNum = Math.min(parseInt(requests['pageNum']), this.pageItems.length - 2)
        }
        if (requests['pageSize'] !== undefined) {
            this.pageSize = parseInt(requests['pageSize'])
        } else {
            this.pageSize = parseInt(getRequest[this.pageItems.eq(1).find('a').attr('href')])
        }
    },

    removeTailOptional: function(comicCount) {
        let pageTotal = this.pageItems.length - 2; // 假定所有调用均合法
        if (pageTotal > Math.ceil(comicCount / this.pageSize)) {
            if (this.pageNum === pageTotal) {
                // this.pageItems.eq(0).find('a').click();
                location.href = location.pathname + '?pageNum=' + (this.pageNum - 1) + '&pageSize=' + this.pageSize;
                return
            } else if (this.pageNum === pageTotal - 1) {
                this.pageItems.eq(-1).addClass('disabled')
            }
            this.pageItems.eq(-2).remove();
        }
    }
};

function deleteComicEvent(e) {
    e.preventDefault();
    let selector = $(this);
    $.ajax( {
        url: selector.attr('href'),
        type: 'delete',
        success: function(msg) {
            if (msg.code === 200) {
                let id = $('.delete-comic:last').attr('href').match(/\d+/)[0];
                // 获取next chapter.
                $.get('/comic/nextComic?id=' + id, function (msg) {
                    if (msg.code === 200) {
                        let now = selector.parent().parent();
                        let nowIndex = parseInt(now.find('th').text());
                        let nextAll = now.nextAll();
                        now.fadeOut(400, () => { now.remove() });
                        nextAll.each(function () {
                            $(this).find('th').text(nowIndex++);
                        });

                        if (msg.data.comic != null) {
                            // 将next chapter写入最后一行
                            nextAll.eq(-1).after('<tr class="comic-info">' +
                                '<th scope="row">' + nowIndex + '</th>\n' +
                                '<td><a href="/admin/comic/' + msg.data.comic.id + '">' + msg.data.comic.title + '</a></td>\n' +
                                '<td>' + msg.data.chapterCount + '</td>\n' +
                                '<td>' + msg.data.comic.author + '</td>\n' +
                                '<td><a href="/admin/comic/' + msg.data.comic.id + '/addList">本地章节</a></td>\n' +
                                '<td><a href="/admin/comic/add/' + msg.data.comic.title + '">更新</a></td>\n' +
                                '<td><a href="/admin/comic/delete?id=' + msg.data.comic.id + '" class="delete-comic">删除</a></td>\n' +
                                '</tr>');
                                $('.delete-comic:last').bind('click', deleteComicEvent);
                        }
                        pagination.removeTailOptional(msg.data.comicCount);
                    }
                });
            }
        }
    })
}

/**
 * 处理文本摘要类'text-summary'
 */
function textSummary() {
    $('.text-summary').each(function () {
        let selector = $(this);
        let text = selector.text();
        if (text.length > 10) {
            text = text.substr(0, 5) + '...' + text.substr(text.length - 3)
        }
        selector.text(text)
    })
}

$(document).ready(function () {
    // auth();
    pagination.init();
    textSummary();
    // login();
    $('[data-toggle="tooltip"]').tooltip();
    $('.breadcrumb-item:last-child').addClass('active').attr('aria-current','page')
                                    .html(function() {return $(this).text()});
    $('.comic-info').click(function() {

    });

    $('.delete-comic').bind('click', deleteComicEvent);
    $('.add-chapter, .add-comic').click(function(e) {
        e.preventDefault();
        let selector = $(this);
        $.ajax(selector.attr('href'), {
            type: 'put',
            success: function(msg) {
                if (msg.code === 200) {
                    selector.parent().addClass('text-muted').text('已添加')
                }
            }
        })
    });
});

