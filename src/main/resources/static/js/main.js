(function ($) {
	 "use strict";

	/*----------------------------
	 jQuery mainmenu
	------------------------------ */
		$(".dropdown-toggle").on("click", function() {
			$('.dropdown-menu').slideToggle(500);
		});

})(jQuery);