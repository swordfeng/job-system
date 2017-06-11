$(document).ready(() => {
	$('.page-header').fadeIn(500, () => {
		$('.form-horizontal').fadeIn(500);
	});
});


function myLogin() {
	let usr = $('#inputEmail').val()
	$.post('api/login', {username:usr, password:$('#inputPassword').val()}, (res) => {
		if (res.status) {
			alert('success')
			document.cookie = "username=" + usr;
			window.location = "company.html";
		} else {
			alert(res.error)
		}
	});
}

function myRegist() {
	$.post('api/register', {username:$('#inputEmail').val(), password:$('#inputPassword').val(), money:0}, (res) => {
		if (res.status) {
			alert('success')
		} else {
			alert(res.error)
		}
	});
}