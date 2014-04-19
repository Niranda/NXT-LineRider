/* CONTENT-BOX HANDLER */
var cboxIsClosed = true;
var cboxLoadedID = false;
var cboxWidth = $("#contentHolder").css("width");
var cboxLeft = $("#contentHolder").css("left");


function cboxToggle(ID) {
	if (cboxIsClosed == true) {		// is closed, so: open cBox
		$("#" + ID).addClass("navActive");
	
		cboxOpen(ID);
		return false;				// do NOT follow the link...
	}
	else {							// cBox is open...
		if (ID == cboxLoadedID || ID == "cboxClose") {
			cboxClose();
		}
		else {
			cboxChange(ID);
			$("#" + ID).addClass("navActive");
		}
		
		return false;				// do NOT follow the link...
	}
}

function cboxOpen(ID) {
	cboxIsClosed = false;
	cboxLoad(ID);
	
	$("#contentHolder").css("display", "block");
	$("#content").css("display", "none");
	
	$("#contentHolder").animate({
		width: 800
	}, 1000, function() {
		$("#content").fadeIn("slow");
	});
}

function cboxClose() {
	cboxIsClosed = true;
	cboxLoadedID = false;
	
	$(".navActive").removeClass("navActive");

	$("#content").fadeOut("slow", function() {
		$("#contentHolder").animate({
			width: 0
		}, 1000, function() {
			$("#contentHolder").css("display", "none");
			$("#content").css("display", "none");
		});
	});
}

function cboxLoad(ID) {
	cboxLoadedID = ID;
	var srcBoxID = "content_" + ID;
	
	$("#content").html("");
	$("#content").html( $("#" + srcBoxID).html() );
}

function cboxChange(ID) {
	$(".navActive").removeClass("navActive");
	
	$("#content").fadeOut("slow", function() {
		cboxLoad(ID);
		$("#content").fadeIn("slow");
	});
}