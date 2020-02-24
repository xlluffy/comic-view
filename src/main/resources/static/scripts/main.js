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

function isMobile() {
    return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
}

// username的额外匹配规则...
function checkName(input) {
    const value = input.val();
    const length = value.length;
    let count = 0;
    for (let i = 0; i < length; i++) {
        if (value.charCodeAt(i) < 256) {
            count++;
        } else {
            count += 2;
        }
    }
    return count >= 4 && count <= 16;
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

/**
 * 求最长公共子序列
 * @param str1
 * @param str2
 * @return [] 最长子序列在str1中的位置
 */
function lcs(str1, str2) {
    // TODO: 优化trace部分
    let dp = [];
    for (let i = 0; i <= str1.length; i++) {
        let line = [];
        for (let i = 0; i <= str2.length; i++) {
            line.push(0)
        }
        dp.push(line);
    }
    for (let i = 1; i <= str1.length; i++) {
        for (let j = 1; j <= str2.length; j++) {
            if (str1[i-1] === str2[j-1]) {
                dp[i][j] = dp[i - 1][j-1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1])
            }
        }
    }
    let i = str1.length;
    let j = str2.length;
    let trace = [];
    while (i >= 1 && j >=1) {
        if (str1[i-1] === str2[j-1]) {
            trace.push(str1[i-1]);
            i--;
            j--
        } else {
            if (dp[i][j-1] > dp[i-1][j]) {
                j--
            } else i--
        }
    }
    trace.reverse();
    let result = [];
    let k = 0;
    let t = 0;
    while (k < trace.length) {
        if (str1[t] === trace[k]) {
            k++;
            result[t] = true;
        } else result[t] = false;
        t++;
    }
    return result;
}

let searchTips = {
    tips: null,
    data: [],
    localTipsData: localStorage.searchTips === undefined ? [] : JSON.parse(localStorage.searchTips),
    show: function() {
        if (!this.focus) {
            this.focus = true;
            this.inputTxt = '';
            $(document).on('mousedown.a', ((e) => {
                if (!$.contains(this.parent.get(0), e.target)) {
                    this.hide();
                }
            }));
            this.search(this.initTips);
            this.tips.show(300);
        }
    },

    hide: function() {
        this.focus = false;
        $(document).off('mousedown.a');
        this.nowTip = null;
        this.tips.hide()
    },

    tipsClickEvent: function() {
        let data = $(this).text();
        if (data.substr(0, 5) === '历史记录:') {
            data = data.slice(6)
        }
        let arr = searchTips.localTipsData;
        const index = arr.indexOf(data);
        if (index !== -1) {
            if (arr.length > 1) {
                arr[index] = arr[0];
                arr[0] = data;
            }
        } else {
            if (arr.length >= 20) { // 最多存储20条记录
                arr.pop();
            }
            arr.unshift(data);
        }
        localStorage.searchTips = JSON.stringify(arr);
        location.href = $(this).attr('data-href');
    },

    initTips: function() {
        // 远程提示
        let remoteTips = $('<ul id="remote-tips"></ul>');
        let length = Math.min(6, this.data.length);
        for (let i = 0; i < length; i++) {
            if (this.isComic) {
                remoteTips.append($('<li class="search-item pt-2 pl-2" data-href=\"/comic/' + this.data[i].id + '\">'
                    + this.data[i].title + '</li>'))
            } else {
                remoteTips.append($('<li class="search-item pt-2 pl-2" data-href=\"/esComic/search?author=' + this.data[i] + '\">'
                    + this.data[i] + '</li>'));
            }
        }
        // 本地记录
        length = Math.min(5, this.localTipsData.length);
        let localTips = $('<ul id="local-tips"></ul>');
        for (let i = 0; i < length; i++) {
            localTips.append($('<li class="search-item pt-2 pl-2" data-href=\"/esComic/search?keyword=' + this.localTipsData[i] + '\">'
                            + this.localTipsData[i] + '</li>'))
        }
        this.tips.empty();
        this.tips.append('<div class="row text-muted pt-2 ml-2 mr-2 align-self-end border-bottom">热搜</div>').append(remoteTips)
            .append('<div class="row text-muted ml-2 mr-2 align-self-end border-bottom">本地记录</div>').append(localTips);

        $('#remote-tips, #local-tips').on('click', 'li', this.tipsClickEvent);
    },

    realTimeTips: function() {
        if (this.inputTxt === "") {
            this.initTips();
            return
        }
        this.tips.empty();
        let tips = $('<ul id="real-time-tips"></ul>');
        let count = 0;
        for (let i = 0; i < this.localTipsData.length && count < 3; i++) {
            let j = 0;
            for (; j < this.inputTxt.length; j++) {
                if (this.inputTxt[j] !== this.localTipsData[i][j]) {
                    break
                }
            }
            if (j === this.inputTxt.length) {
                count++;
                tips.append($('<li class="search-item pt-2 pl-2" data-href=\"/esComic/search?keyword=' + this.localTipsData[i] + '\">'
                    + '历史记录: <b class="text-primary">'
                    + this.inputTxt + '</b>' + this.localTipsData[i].substring(j) + '</li>'))
            }
        }
        const length = Math.min(10 - count, this.data.length);
        for (let i = 0; i < length; i++) {
            let tip;
            let title;
            if (this.isComic) {
                tip = '<li class="search-item pt-2 pl-2" data-href=\"/comic/' + this.data[i].id + '\">';
                title = this.data[i].title;
            } else {
                tip = '<li class="search-item pt-2 pl-2" data-href=\"/esComic/search?author=' + this.data[i] + '\">';
                title = this.data[i];
            }
            let trace = lcs(title.toLowerCase(), this.inputTxt);
            for (let j = 0; j < title.length; j++) {
                if (trace[j]) {
                    tip += '<b class="text-primary">' + title[j] + '</b>';
                } else {
                    tip += title[j];
                }
            }
            tips.append($(tip + '</a></li>'))
        }
        this.tips.append(tips);
        $('#real-time-tips').on('click', 'li', this.tipsClickEvent);
    },

    search: function(callback) {
        let data = this.inputTxt === '' ? {} : {keyword: this.inputTxt};
        $.get('/esComic/search/simple', data, (msg) => {
            if (msg.code === 200) {
                this.isComic = msg.data.keyword;
                this.data = msg.data.data;
                callback.apply(this);
            }
        });
    },

    create: function(input, data, options) {
        this.tips = $('<div id="search-tips" class="mt-1"></div>').css({width: input.outerWidth()});
        this.hide();
        input.focus(() => {
            this.show();
        });

        input.one('focus', () => {
            this.parent = input.parent();
            this.parent.append(this.tips);
        });

        let cpLock = false;
        input.on('compositionstart', function() {
            cpLock = true;
        });
        input.on('compositionend', () => {
            cpLock = false;
            if (this.nowTip != null) {
                this.nowTip.removeClass('hover');
                this.nowTip = null;
            }
            let inputTxt = input.val().trim();
            if (Math.abs(inputTxt.length - this.inputTxt.length) > 0) {
                this.inputTxt = inputTxt;
                this.search(this.realTimeTips)
            }
        });
        input.on('input', () => {
            if (!cpLock) {
                if (this.nowTip != null) {
                    this.nowTip.removeClass('hover');
                    this.nowTip = null;
                }
                setTimeout(() => {
                    let inputTxt = input.val().trim();
                    if (Math.abs(inputTxt.length - this.inputTxt.length) > 0) {
                        this.inputTxt = inputTxt;
                        this.search(this.realTimeTips)
                    }
                }, 500)
            }
        });
        input.on('keydown', (e) => {
            // TODO: 切换加入localTips
            switch (e.keyCode) {
                case 9: // tab, 直接触发down...
                    e.preventDefault();
                    let e1 = $.Event('keydown', {keyCode: 40});
                    input.trigger(e1);
                    break;
                case 27: // esc
                    this.hide();
                    input.blur();
                    break;
                case 13: // enter
                    const inputTxt = input.val().trim();
                    if (inputTxt.length > 0) {
                        if (this.nowTip != null) {
                            this.nowTip.click();
                        } else {
                            location.href = '/esComic/search?keyword=' + inputTxt
                        }
                    }
                    break;
                case 38: // up
                    if (this.nowTip && this.nowTip.length > 0) {
                        this.nowTip.removeClass('hover');
                    }
                    this.nowTip = this.nowTip.prev('li');
                    this.nowTip.addClass('hover');
                    input.val(this.nowTip.text());
                    break;
                case 40: // down
                    if (this.nowTip == null || this.nowTip.length === 0) {
                        this.nowTip = this.tips.find('li').eq(0);
                    } else {
                        this.nowTip.removeClass('hover');
                        this.nowTip = this.nowTip.next('li')
                    }
                    this.nowTip.addClass('hover');
                    input.val(this.nowTip.text());
                    break;
            }
        })
    },
};

function hideSecret() {
    $('.secret').each(function () {
        let email = $(this).text();
        let pos = email.search('@');
        let host = email.substring(0, pos);
        $(this).text(host[0] + '...' + host[host.length - 1] + email.substring(pos));
    });
}

$(document).ready(function () {
    // auth();
    pagination.init();
    textSummary();
    hideSecret();
    // login();
    $('[data-toggle="tooltip"]').tooltip();

    $('.breadcrumb-item:last-child').addClass('active').attr('aria-current','page')
                                    .html(function() {return $(this).text()});
    $('.comic-info').click(function() {

    });

    if (!isMobile()) {
        $('.sidebar img, .card img, .record img, .page-img').attr('src', '/tmp-cover.png');
    }

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

    /*$('.record-delete').click(function() {
    })*/
    $('.btn-favourite').click(function(e) {
        e.preventDefault();
        let selector = $(this).find('img');
        let comicId = $(this).attr('name');
        if (selector.attr('src') === '/images/icons/heart.svg') {
            $.post('/comic/' + comicId + '/addFavourite', {}, function(msg) {
                if (msg.code === 200) {
                    selector.attr('src', '/images/icons/heart-fill.svg');
                    selector.attr('title', '取消订阅')
                }
            });
        } else {
            $.post('/comic/' + comicId + '/deleteFavourite', {}, function(msg) {
                if (msg.code === 200) {
                    selector.attr('src', '/images/icons/heart.svg');
                    selector.attr('title', '订阅')
                }
            });
        }
    });

    $('.add-roles').change(function() {
        $.post('/admin/account/addRole', {'userId': $(this).attr('name'),
            'role': $(this).find('option:selected').val()},
            function (msg) {
                if (msg.code === 200) {
                    console.log('添加权限成功');
                    location.reload();
                }
            })
    });

    $('.remove-roles').change(function() {
        $.post('/admin/account/removeRole', {'userId': $(this).attr('name'),
                'role': $(this).find('option:selected').val()},
            function (msg) {
                if (msg.code === 200) {
                    console.log('移除权限成功');
                    location.reload();
                }
            })
    });

    $('.status-checkbox').click(function() {
        let selector = $(this);
        if (selector.attr('checked') === 'checked') {
            $.post('/admin/account/disabledUser', {'userId': selector.val()}, function () {
                selector.removeAttr('checked')
            })
        } else {
            $.post('/admin/account/enabledUser', {'userId': selector.val()});
            selector.attr('checked', 'checked');
        }
    });

    // $('.delete-user').click(function() {});

    searchTips.create($('#search'));
});
