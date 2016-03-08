var $multi = $('#multi');
var $immediate = $('#immediate');

$multi.bind('change', function(event) {
	$immediate.empty();
	if ($multi[0].checked) {
		var input = tag('p', {}, [
		      'Show feedback after each question ',
		      tag('input', {'type': 'checkbox', 'name': 'immediate', 'value': 'true'}, [])
		]);
		$immediate.append(input);
	}
});