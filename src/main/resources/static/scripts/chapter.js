let now_page = 0;
let step = 20;
let lastSyncId = 0;

function syncRecord(page) {
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

function image_click() {
    if (confirm("是否保存浏览记录")) {
        syncRecord(this.parentNode.id);
    }
}

function appendPage(container, id) {
    let page = $("<div class='page' id='{0}'>".format(id));
    let img = $("<img src='/{0}/{1}/{2}{3}' alt='/{0}/{1}/{2}{3}' class='page-img border border-light rounded mt-2 mb-2' "
            .format(chapter.comic.title, chapter.title, id.format(3), chapter.suffix) +
        "ondblclick='image_click()'/>");
    let footer = $("<span class='footer text-secondary'>{0}/{1}</span>".format(id, chapter.pages));
    page.append(img).append("<br />").append(footer);
    container.append(page);
}

function appendPages(container, end) {
    end = step * (Math.floor((end - 1) / step) + 1);
    end = end > chapter.pages ? chapter.pages : end;
    while (now_page < end) {
        appendPage(container, ++now_page);
    }
    // console.log("now_page = " + now_page)
}

function getRequest() {
    let url = location.search.substr(1);
    let requestPairs = url.split('&');
    let request = [];
    for (let i = 0; i < requestPairs.length; i++) {
        let requestPair = requestPairs[i].split('=');
        request[requestPair[0]] = requestPair[1];
    }
    return request;
}

function documentLoad(container, request) {
    let page = parseInt(request["page"]);
    if (!isNaN(page)) {
        appendPages(container, page);
        location.href = location.pathname + location.search + '#' + page;
    } else {
        appendPages(container, 20);
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

function isActive(page, windowHeight) {
    let clientRect = page.getBoundingClientRect();
    return windowHeight >= clientRect.top && clientRect.bottom >= 0;
}

function getFirstActivePage() {
    let windowHeight = $(window).height();
    let pages = $(".page");
    for (let x in pages) {
        if (isActive(pages[x], windowHeight)) {
            return pages[x];
        }
    }
}

function autoSyncRecord() {
    let page = getFirstActivePage();
    if (page.id !== lastSyncId) {
        syncRecord(page.id);
    }
    setTimeout('autoSyncRecord()', 30 * 1000);
}

function scroll1() {
    let documentClientHeight = document.documentElement.clientHeight || window.innerHeight;
    let htmlElementClientTop = document.getElementById("#10").getBoundingClientRect().top;
    if (documentClientHeight >= htmlElementClientTop) {
        alert("")
    }
}

$(document).ready(function () {
    let container = $("#main_pages");
    let page_footer = $("#page_footer");

    let load_complete = false;

    documentLoad(container, getRequest());
    autoSyncRecord();

    clock();

    $("#menu_icon").mouseenter(function () {
        $("#menu_content").slideDown("fast");
    });

    $("#menu").mouseleave(function () {
        $("#menu_content").slideUp("fast");
    });

    $(window).scroll(function () {
        let scrollTop = $(this).scrollTop();
        let windowHeight = $(this).height();
        let documentHeight = $(document).height();
        if (scrollTop + windowHeight + 10 >= documentHeight) {
            if (now_page < chapter.pages) {
                appendPages(container, now_page + step);
                // syncRecord(now_page - step + 1);
            } else if (!load_complete) {
                page_footer.removeAttr("hidden");
                load_complete = true;
                // syncRecord(chapter.pages);
            }
        }
    });

    $("#btn-goto").click(function () {
        let n = parseInt($("#input-btn").val());
        if (!isNaN(n)) {
            if (n > now_page) {
                appendPages(container, n);
                n = n >= chapter.pages ? chapter.pages : n;
                syncRecord(n);
            }
            // setTimeout(function(){
            //     location.href = location.pathname + location.search + '#' + n;
            // }, 200);
            location.href = location.pathname + location.search + '#' + n;
        }
    });
});
