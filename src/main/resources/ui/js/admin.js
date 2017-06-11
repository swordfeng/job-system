$(document).ready(() => {
	$('#myMain').fadeIn(500, () => {
		refresh()
	});
});

function pass() {
	$.pos('', {}, (res) => {
		if (res.status) {
			alert('success')
		} else {
			alert(res.error)
		}
		refresh()
	})
}

function unpass() {
	$.post('', {}, (res) => {
		if (res.status) {
			alert('success')
		} else {
			alert(res.error)
		}
		refresh()
	})
}

function refresh() {
	$.get('api/getFirst', (res) => {
		if (res.status) {
			$('#name').html(res.names)
			$('#num').html(res.num)
			$('#skills').html(res.skills)
			$('#education').html(res.education)
			$('#address').html(res.address)
		} else {
			alert(status.error)
		}
	})
}