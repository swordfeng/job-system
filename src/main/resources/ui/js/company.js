$(document).ready(() => {
	$('#myMain').fadeIn(500);
});

function mySubmit() {
	let myForm = {
		username : document.cookie.substring(9,document.cookie.length),
		name: $('#name').val(),
		address: $('#address').val(),
		num: $('#num').val(),
		skills: $('#skills').val().split(',').toString(),
		education: $('#education').val()
	}
	console.log(myForm)
	$.post('api/submitJob', myForm, (res) => {
		if (res.status) {
			alert('success')
		} else {
			alert(res.error)
		}
	})
}