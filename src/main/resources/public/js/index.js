//========================variables========================

const input_email = document.getElementById('email');
const input_password = document.getElementById('password');
const form = document.getElementById('form');
const url = '';

//========================functions========================

const submit = async event => {
    event.preventDefault();

    const { value: email } = input_email;
    const { value: password } = input_password;

    const user = { email, password };

    const petition = await fetch(`${url}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    });

    const response = await petition.json();

    if (response.status === 200) {
        window.location.href = '/security/helloService';
        return;
    }
    alert('Something went wrong');
};

//========================EventsListeners==================

setEventsListeners();

// Function that set all the events of the DOM
function setEventsListeners() {
    form?.addEventListener('submit', submit);
}
