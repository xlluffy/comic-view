function resizeImage(image, rate) {
    image.width /= rate;
    image.height /= rate;
}

function createCard(container, chapter) {
    let card = $("<div class='col-2 card mt-3 ml-4'></div>");
    let card_img_top = null;
    if (all_records[chapter.title] !== undefined) {
        card_img_top = $("<a href='/comic/{0}'> " +
            "<img src='/static/{0}/{1}{2}' alt='/{0}/{1}{2}' id='{0}'"
                .format(chapter.title, Number(all_records[chapter.page]).format(3), chapter.suffix))
    }
}

function createCards() {
    let container = $("#cards");
    for (let x in chapters) {
        createCard(container, chapter);
    }
}

$(document).ready(function () {
    // $("#img-recent").load(function() {
    //     resizeImage($("#img-recent"), 5);
    // });
});
