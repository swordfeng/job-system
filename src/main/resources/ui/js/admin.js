$(document).ready(() => {
	$('#myMain').fadeIn(500, () => {
		refresh()
	});
});

function pass() {
	$.post('api/pass', {}, (res) => {
		if (res.status) {
			alert('success')
		} else {
			alert(res.error)
		}
		refresh()
	})
}

function unpass() {
	$.post('api/reject', {}, (res) => {
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
			$('#name').html(res.name)
			$('#num').html(res.num)
			$('#skills').html(res.skills)
			$('#education').html(res.education)
			$('#address').html(res.address)
		} else {
			alert("审批队列空")
			$('#name').html('')
			$('#num').html('')
			$('#skills').html('')
			$('#education').html('')
			$('#address').html('')
		}
	})
}