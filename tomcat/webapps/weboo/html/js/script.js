/* Author:Virendra Rajput
   Twitter: @bkvirendra
   Tuesday, June, 2012
*/

$(document).ready( function() {
	$("#movie").click( function() {
	var divMovie = ""
		$.ajax({
			url: "search.jsp",
			type: "GET",
			data: { q:"movie" },
			dataType: "json",
			success: function (data) {
				$("#home").hide();
				jQuery.each(data, function(i, val) {
					//console.log(val);
					divMovie = "<div id='boxes'><img style='padding: 10px 10px 10px 10px; float:left;' src="+val.img+"><p><a id="+val.imdbid+" href='javascript:void();'>"+val.name+"</a><br /><span id='genre'>"+val.genre+"</span><br /><span id='cast'>Cast :"+val.cast+"<br />Directed By :"+val.director+"</span><br /><span id='story'>"+val.story+"</span></p></div><br />";
					$(".hero-unit").append(divMovie);
				});
			}
		});
	});
	
	$("a").click(function(event) {
		//alert(event.target.id);
	});
	
	$("#music").click( function() {
	var divMusic = ""
		$.ajax({
			url: "search.jsp",
			type: "GET",
			data: { q:"music" },
			dataType: "json",
			success: function (data) {
				$("#home").hide();
				jQuery.each(data, function(i, val) {
					console.log(val);
					divMusic = "<div id='boxes'><p>"+val.SongName+"</a><br /><span id='story'>Album Name :"+val.AlbumName+"<br />Artist :"+val.ArtistName+"</span></p></div><br />";
					$(".hero-unit").append(divMusic);
				});
			}
		});
	});
});