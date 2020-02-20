$(document).ready(function() {
    const requests = getRequest(null);
    let pos = requests['categoryId'] === undefined ? 1 : parseInt(requests['categoryId']);
    $('#nav-category a').eq(pos - 1).css('color', 'blue')
});
