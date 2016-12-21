jQuery(function($){
	'use strict';

	// -------------------------------------------------------------
	//   Non Item Based Navigation
	// -------------------------------------------------------------
	(function () {
		var $frame = $('#nonitembased');
		var $wrap  = $frame.parent();

		// Call Sly on frame
		$frame.sly({
			speed: 300,
			easing: 'easeOutExpo',
			pagesBar: $wrap.find('.pages'),
			activatePageOn: 'click',
			scrollBar: $wrap.find('.scrollbar'),
			scrollBy: 100,
			dragHandle: 1,
			dynamicHandle: 1,
			clickBar: 1,

			// Buttons
			forward: $wrap.find('.forward'),
			backward: $wrap.find('.backward'),
			prevPage: $wrap.find('.prevPage'),
			nextPage: $wrap.find('.nextPage')
		});

		// To Start button
		$wrap.find('.toStart').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the start of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toStart', item);
		});

		// To Center button
		$wrap.find('.toCenter').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the center of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toCenter', item);
		});

		// To End button
		$wrap.find('.toEnd').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the end of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toEnd', item);
		});
	}());

	// -------------------------------------------------------------
	//   Smart Navigation
	// -------------------------------------------------------------
	(function () {
		var $frame  = $('#smart');
		var $slidee = $frame.children('ul').eq(0);
		var $wrap   = $frame.parent();

		// Call Sly on frame
		$frame.sly({
			itemNav: 'basic',
			smart: 1,
			activateOn: 'click',
			mouseDragging: 1,
			touchDragging: 1,
			releaseSwing: 1,
			startAt: 3,
			scrollBar: $wrap.find('.scrollbar'),
			scrollBy: 1,
			pagesBar: $wrap.find('.pages'),
			activatePageOn: 'click',
			speed: 300,
			elasticBounds: 1,
			easing: 'easeOutExpo',
			dragHandle: 1,
			dynamicHandle: 1,
			clickBar: 1,

			// Buttons
			forward: $wrap.find('.forward'),
			backward: $wrap.find('.backward'),
			prev: $wrap.find('.prev'),
			next: $wrap.find('.next'),
			prevPage: $wrap.find('.prevPage'),
			nextPage: $wrap.find('.nextPage')
		});

		// To Start button
		$wrap.find('.toStart').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the start of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toStart', item);
		});

		// To Center button
		$wrap.find('.toCenter').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the center of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toCenter', item);
		});

		// To End button
		$wrap.find('.toEnd').on('click', function () {
			var item = $(this).data('item');
			// Animate a particular item to the end of the frame.
			// If no item is provided, the whole content will be animated.
			$frame.sly('toEnd', item);
		});

		// Add item
		$wrap.find('.add').on('click', function () {
			$frame.sly('add', '<li>' + $slidee.children().length + '</li>');
		});

		// Remove item
		$wrap.find('.remove').on('click', function () {
			$frame.sly('remove', -1);
		});
	}());


	// -------------------------------------------------------------
	//   Cycle By Items
	// -------------------------------------------------------------
	(function () {
	 var $btn  = $('.btn-default');
		var $frame1 = $('#slot1');
		var $frame2 = $('#slot2');
		var $frame3 = $('#slot3');
		var $wrap  = $frame1.parent();

		var option = {
			itemNav: 'forceCentered',
			smart: 1,
			activateOn: 'click',
			mouseDragging: 1,
			touchDragging: 1,
			releaseSwing: 1,
			startAt: 6,
			scrollBar: $wrap.find('.scrollbar'),
			scrollBy: 1,
			speed: 350,
			elasticBounds: 1,
			easing: 'easeOutExpo',
			dragHandle: 1,
			dynamicHandle: 1,
			clickBar: 1,

			// Cycling
			cycleBy: 'items',
			cycleInterval: 120,

		}
		// Call Sly on frame
		$frame1.sly(option);
		$frame2.sly(option);
		$frame3.sly(option);
		$frame1.sly('pause');
	  $frame2.sly('pause');
	  $frame3.sly('pause');
	  $frame1.sly('activate', 6 , true );
	  $frame2.sly('activate', 6 , true );
	  $frame3.sly('activate', 6 , true );
		// Pause button
		// $wrap.find('.pause').on('click', function () {
		// 	$frame.sly('pause');
		// });
		//
		// // Resume button
		// $wrap.find('.resume').on('click', function () {
		// 	$frame.sly('resume');
		// });

		// Toggle button
		// $wrap.find('.toggle').on('click', function () {
		// 	$frame.sly('toggle');
		// });
	}());

});
