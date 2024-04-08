$(document).ready(function() {

    $("#converterForm").submit(function(event) {
        event.preventDefault();
        var formData = $(this).serialize();
        $.ajax({
            url: "/submit",
            type: "POST",
            data: formData,
            success: function(response) {
                $("#result").text(response);
            },
            error: function(xhr, status, error) {
                console.error('Request failed: ' + error);
            }
        });
    });
});