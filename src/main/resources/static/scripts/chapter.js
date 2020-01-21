let requests = getRequest();

let lastSyncId = isNaN(requests['page']) ? 0 : parseInt(requests['page']);
let currentPage = lastSyncId === 0 ? 1 : lastSyncId;

function syncRecordPage(page) {
    // $.post("/record/update",
    //     JSON.stringify({"chapter": chapter, "page": String(page), "suffix": chapter.suffix}));
    $.ajax({
        url: "/record/update",
        type: "post",
        data: JSON.stringify({"chapter": chapter, "page": String(page), "suffix": chapter.suffix}),
        dataType: "json",
        contentType: "application/json"
    });
    lastSyncId = page;
}

function syncRecord(readStyle) {
    let page = readStyle.getCurrentPage();
    if (page !== lastSyncId) {
        syncRecordPage(page);
    }
}

/**
 * 自动同步记录，实现了onbeforeunload事件后似乎必要性不高...
 */
function autoSyncRecord() {
    syncRecord();
    setTimeout('autoSyncRecord()', 30 * 1000);
}

/**
 * 跳转到指定页面
 * @param pageNum
 */
function scrollToPage(pageNum) {
    // $(document).scrollTop($('.page')[pageNum - 1].offsetTop);
    if (pageNum === 1) {
        $(document).scrollTop(0)
    } else {
        $(document).scrollTop($('#main-pages div:nth-child({0})'.format(pageNum))[0].offsetTop);
    }
}

function clock() {
    let now = new Date();
    let h = now.getHours();
    let m = now.getMinutes();
    // let s = now.getSeconds();
    $("#clock").html("{0}:{1}".format(h, m.format(2)));
    setTimeout('clock()', 1000 * 10);
}

$(function () {
    let container = $("#main-pages");

    let dropdown = {
        step: 20,
        nowPage: 0,
        footer: $("#dropdown-footer"),
        menu: $('.dropDown'),

        getCurrentPage: function() {
            let windowHeight = $(window).height();
            let pages = $(".page");
            let isActive = (page, windowHeight) => {
                let clientRect = page.getBoundingClientRect();
                return windowHeight >= clientRect.top && clientRect.bottom > 10;
            };
            for (let x in pages) {
                if (isActive(pages[x], windowHeight)) {
                    return parseInt(x) + 1;
                }
            }
        },

        appendPage: function(id) {
            let page = $("<div class='page'>");
            let img = $("<img src='/{0}/{1}/{2}{3}' alt='/{0}/{1}/{2}{3}' class='page-img border border-light rounded mt-2 mb-2' /> "
                .format(chapter.comic.title, chapter.title, id.format(3), chapter.suffix));
            let footer = $("<span class='footer text-secondary'>{0}/{1}</span>".format(id, chapter.pages));
            page.append(img).append("<br />").append(footer);
            container.append(page);
        },

        /**
         * 批量添加pages
         */
        appendPages: function(end) {
            end = this.step * (Math.floor((end - 1) / this.step) + 1);
            end = end > chapter.pages ? chapter.pages : end;
            while (this.nowPage < end) {
                this.appendPage(++this.nowPage);
            }
        },


        hideMenu: function() {
            this.menu.hide()
        },

        /**
         * 页面载入时的准备工作，生成各个page，跳转到当前阅读位置，并且注册onbeforeunload事件
         */
        documentLoad: function() {
            this.footer.hide();
            if (currentPage === 0) {
                this.appendPages(20);
            } else {
                this.appendPages(currentPage);
            }
            $(function() {
                setTimeout('scrollToPage({0})'.format(currentPage), 200); // 略微延时跳转，等待页面加载
                // scrollToPage(page);
            });
            window.onbeforeunload = () => {
                syncRecord(this)
            };
            // 预加载step个page，直至加载完毕
            $(window).bind('scroll.a', () => {
                let scrollTop = $(window).scrollTop();
                let windowHeight = $(window).height();
                let documentHeight = $(document).height();
                if (scrollTop + windowHeight + 10 >= documentHeight) {
                    if (this.nowPage < chapter.pages) {
                        this.appendPages(this.nowPage + this.step);
                    } else {
                        this.footer.show();
                        $(window).unbind('scroll.a');
                    }
                }
            })
        },

        /**
         * 重新构造页面，并且修改本地存储: dropDown的值
          */
        rebuild: function() {
            this.nowPage = 0;
            this.menu.show();
            this.documentLoad();
            localStorage.dropDown = true
        },
        /**
         * 解除事件绑定、修改currentPage的值、清除页面、隐藏菜单和修改本地存储
         */
        destroy: function() {
            $(window).unbind('scroll.a');
            currentPage = this.getCurrentPage();
            container.empty();
            this.hideMenu();
            localStorage.dropDown = false
        }
    };

    let onePage = {
        select: $('.select-page'),
        menu: $(".onePage"),

        getCurrentPage: function() {
            return currentPage;
        },

        // 隐藏菜单
        hideMenu: function() {
            this.menu.hide()
        },

        // 跳转到指定页面
        gotoPage: function(page) {
            let imgPath = '/{0}/{1}/{2}{3}'.format(chapter.comic.title, chapter.title,
                page.format(3), chapter.suffix);
            $('.page img').attr({src: imgPath, alt: imgPath});
            this.select.children()[page - 1].selected = true;
            currentPage = page;
            $(document).scrollTop($('#one-page-header')[0].offsetTop)
        },

        // 前一页
        prePage: function() {
            if (currentPage > 1) {
                onePage.gotoPage(currentPage - 1)
            } else {
                let preChapter = $('.pre-chapter:first');
                if (preChapter.length > 0 && confirm('已经是第一页了，是否跳转到上一章节？')) {
                    preChapter.click();
                }
            }
        },

        // 下一页
        nextPage: function() {
            if (currentPage < chapter.pages) {
                onePage.gotoPage(currentPage + 1)
            } else {
                let nextChapter = $('.next-chapter:first');
                if (nextChapter.length > 0 && confirm('已经是本篇章最后一页了，是否跳转到下一章节？')) {
                    nextChapter.click();
                }
            }
        },

        /**
         * 页面载入时的准备工作，生成select>option标签（可由thymeleaf完成），生成当前页面，绑定按钮、鼠标点击、鼠标移动事件
         */
        documentLoad: function() {
            container.append($("<div class='page'><img class='page-img border border-light rounded mt-2 mb-2' /></div>"));

            for (let i = 1; i <= chapter.pages; ++i) {
                let node = $("<option>" + i + "</option>");
                this.select.append(node);
            }
            this.gotoPage(currentPage);

            this.select.change(function () {
                let page = $(this).find('option:selected').val();
                onePage.gotoPage(parseInt(page));
            });

            $('.pre-page').bind('click', this.prePage);
            $('.next-page').bind('click', this.nextPage);
            container.bind('click.a', (event) => {
                if (window.outerWidth > 5 * event.screenX / 2) {
                    this.prePage();
                } else if (window.outerWidth < 5 * event.screenX / 3){
                    this.nextPage();
                }
            });
            container.bind('mousemove.a', function(event) {
                if (window.outerWidth > 5 * event.screenX / 2 || window.outerWidth < 5 * event.screenX / 3) {
                    $(this).css('cursor', 'pointer')
                } else {
                    $(this).css('cursor', 'default')
                }
            });
            window.onbeforeunload = () => {
                syncRecord(this)
            };
        },

        rebuild: function() {
            this.menu.show();
            this.documentLoad()
        },

        /**
         * 解除已绑定事件，隐藏菜单，清除图片，相当于析构
         */
        destroy: function() {
            container.unbind('click.a');
            container.unbind('mousemove.a');
            this.hideMenu();
            container.empty()
        }
    };

    // 根据本地存储决定阅读方式，默认下拉式
    if (localStorage.dropDown == null || localStorage.dropDown === 'true') {
        $('#read-style').attr('checked', 'checked');
        onePage.hideMenu();
        dropdown.documentLoad()
    } else {
        $('#read-style').removeAttr('checked');
        dropdown.hideMenu();
        onePage.documentLoad()
    }

    // 切换阅读方式
    $("#read-style").click(function() {
        if ($(this).prop('checked')) {
            onePage.destroy();
            dropdown.rebuild()
        } else {
            dropdown.destroy();
            onePage.rebuild()
        }
    });
    $('#menu-read-style').click(() => {$('#read-style').click()});

    $("#menu-icon").mouseenter(function () {
        $("#menu-content").slideDown("fast");
    });
    $("#menu").mouseleave(function () {
        $("#menu-content").slideUp(800);
    });

    clock();

    $("#btn-goto").click(function () {
        let n = parseInt($("#input-btn").val());
        if (!isNaN(n)) {
            if (n > dropdown.nowPage) {
                dropdown.appendPages(n);
                n = n >= chapter.pages ? chapter.pages : n;
            }
            $(function () {
                scrollToPage(n)
            })
        }
    });
    // 开启自动同步，务必放在最后
    // setTimeout('autoSyncRecord()', 1000 * 10);
});
