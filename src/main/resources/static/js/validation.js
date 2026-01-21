function validateForm(button) {
    const form = button.closest('form');
    if (!form) return;

    let isValid = true;
    const inputs = form.querySelectorAll('input[required], select[required]');

    inputs.forEach(input => {
        // Clear previous error
        removeError(input);

        // Check validity using HTML5 API first
        if (!input.checkValidity()) {
            showError(input, input.validationMessage);
            isValid = false;
        }

        // Custom check for whitespace-only names
        if (input.type === 'text' && input.value.trim() === '') {
            showError(input, 'This field cannot be empty or just whitespace.');
            isValid = false;
        }

        // Extra email format check
        if (input.type === 'email') {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(input.value)) {
                showError(input, 'Please enter a valid email address.');
                isValid = false;
            }
        }
    });

    if (isValid) {
        console.log('Validation passed, submitting form...');
        form.submit();
    } else {
        console.log('Validation failed.');
    }
}

function showError(input, message) {
    input.classList.add('is-invalid');
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-feedback';
    errorDiv.innerText = message;
    input.parentElement.appendChild(errorDiv);
}

function removeError(input) {
    input.classList.remove('is-invalid');
    const feedback = input.parentElement.querySelector('.error-feedback');
    if (feedback) {
        feedback.remove();
    }
}

// Optional: Still clear errors on input for better UX
document.addEventListener('input', function (event) {
    if (event.target.matches('input, select')) {
        removeError(event.target);
    }
});

function validateUserForm(button) {
	const form = button.closest('form');
    if (!form) return;
    var username=document.getElementById("username").value;
    console.log(username);
    if(username==''){
    	alert("username cannot be empty");
    	return;
    }

    console.log('Validation passed, submitting form...');
   	form.submit();
    
}
