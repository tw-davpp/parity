$(function () {
    $("#button").button();
    $(document).tooltip();

    var clicks = 0;
    $('.link').bind("click", function () {
        clicks++;
        alert(clicks);
    });
});