$(document).ready(function() {
    var token = localStorage.getItem('jwt');
    // Function to fetch roles
    function fetchRoles() {
        $.ajax({
            url: 'http://localhost:9999/role',  // The API endpoint
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + token   
            },
    
            success: function(response) {
                if(response.statusCode === 200) {
                    let roles = response.data;
                    let roleDropdown = $('#id_role');
                    roleDropdown.empty();  // Clear existing options
                    roleDropdown.append('<option value="">-- Select Role --</option>');  // Add default option
                    $.each(roles, function(index, role) {
                        roleDropdown.append('<option value="' + role.id + '">' + role.name + '</option>');
                    });
                } else {
                    console.error('Failed to fetch roles');
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }

    // Call the function to fetch roles when the page loads
    fetchRoles();
});
