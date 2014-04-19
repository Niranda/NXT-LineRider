/* BACKGROUND CYCLING */
function changeBackground(last) {
	var divid = '#bgHolder';		// the ID of the Div-Box
	var path = './img/onTour/';		// Path to the images
	var type = '.jpg';				// file-type of the images
	var start = 1;					// Start at Image 1
	var end = 32;					// End at Image 32
	
	var i = last;
	var opacity = $(divid).css('opacity');
	
	while (i == last) {				// to be sure that the next image is another one
		i = Math.floor((Math.random() * end) + start);
	}
	
	$(divid).fadeTo('slow', 0, function() {
		$(divid).css('background-image', 'url('+ path + i + type +')');
	}).fadeTo('slow', opacity);
	
	return i;
}