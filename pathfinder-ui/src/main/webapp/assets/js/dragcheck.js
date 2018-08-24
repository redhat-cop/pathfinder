/**
 * A jQuery plugin for making a sortable/draggable multi-checkbox.
 */
jQuery( window ).load(function() {

	// The li that wraps this whole section of the customizer. Sort of like a fieldset.
	var el = jQuery( '.customize-control-checkbox_group' );

	jQuery( el ).lxbAfCheckboxGroup();

});

(function ( $ ) {

	$.fn.lxbAfCheckboxGroup = function( options ) {

		/**
		 * Create an HTML <select> menu for choosing some value related to this checkbox.
		 * 
		 * @param  {string}  type         What type of options we want: linkCategories, customMenu, etc...
		 * @param  {boolean} visible      Should the menu be visible?  If the checkbox is not checked, the select menu should not be visible.
		 * @param  {string}  hiddenValArr The JSON currently being stored for this fieldset.
		 * @return {string}  The HTML for a <select> menu.
		 */
		function dropdown( type, visible, hiddenValArr ) {

			// Create a select menu.
			var select = jQuery( '<select>' ).addClass( 'lxbAfCheckboxGroup-select' ).attr( 'data-type', type );

			// Maybe hide it.
			if( ! visible ) {
				jQuery( select ).hide();
			}

			// Start it with an empty option.
			var emptyOpt = jQuery( '<option>' ).val( '' ).appendTo( select );

			// Grab our php variables.
			var localize = lxbAfCustomizeLocalize;

			// Grab the current value for this menu.
			var current = '';
			if( hiddenValArr != null ) {
				
				//Is the checkbox for this item checked?
				if( hiddenValArr[ '###' + type + '###' ] != null ) {
				
					// If so, determine which option is selected.
					var current = hiddenValArr[ '###' + type + '###' ];
				}
			}

			// Get a list of <option>'s for the <select>.
			var choices = localize[type];
			
			// For each <option>...
			jQuery( choices ).each( function( index, value ) {
			
				// The value.
				var slug = value.slug;
				
				// The label.
				var name = value.name;
				
				// Build the <option>.
				var option = jQuery( '<option/>', {
					html: name,
					'value' : slug
				});
				
				// Maybe make it sticky.
				if( slug == current ) {
					jQuery( option ).attr( 'selected', 'selected' );
				}

				// Add it to the select menu.
				jQuery( option ).appendTo( select );
				
			});

			var out = jQuery( select );

			return out;

		}

		/**
		 * A function to make a draggable checkbox input.
		 * 
		 * @param  {string}   key          The name of the option controlled by this checkbox.
		 * @param  {string}   hiddenValArr The JSON currently being stored in this fieldset.
		 * @param  {string}   fieldsetID   The HTML ID for this fieldset.
		 * @param  {boolean}  checked      Whether or not this checkbox hould be checked.
		 * @return {string}   The HTML for a draggable checkbox input.
		 */
		function checkboxListItem( key, hiddenValArr, fieldsetID,  checked ) {

			var out = '';

			// If the key is empty, skip it.
			if( key === '' ) { return out; }

			// If we are doing an empty checkbox, make sure it's not already on the list.
			if( ! checked ) {

				// If there is actually some value in the hidden input...
				if( hiddenValArr !== null ) {

					// If the checkbox for this item is already checked, skip it.
					if( hiddenValArr[key] == true ) {
						return out;

					// Else, if it's a non empty string, skip it.
					} else if( ( typeof hiddenValArr[key] == 'string' ) && hiddenValArr[key] != '' ){
						return out;
					}
				}
			}

			// This might end up holding an HTML <select> menu.
			var select = '';
			
			// The might hold the current value of the select menu.
			var selected = false;

			// Does this key contain a magic hash code?
			if( key.indexOf( '###' ) != -1 ) {

				// If so, remove the magic hash codes in order to arrive at what type of <select> it is.
				var type = key.replace( new RegExp( '#', 'g' ), '' );
				
				// Grab the select menu. We'll append it later.
				select = dropdown( type, checked, hiddenValArr );

				// Grab the current value of the select menu.
				selected = jQuery( select ).val();
				
			}

			// Create the checkbox.
			var input = jQuery( '<input/>', {
				'id'    : fieldsetID + key,
				'name'  : key,
				'type'  : 'checkbox',
				'value' : key
			});

			// If the checkbox is checked and has a select menu, record the value of the select menu in a data attribute.
			if( checked && selected ) {
				jQuery( input ).attr( 'data-which', selected );
			}

			// Wrap a label around the input.
			var label = jQuery( '<label/>', {
				html  : key,
				'for' : fieldsetID + key
			});
			jQuery( input ).prependTo( label );
			
			// Make a list item to wrap the whole darn thing.
			var out = jQuery( '<li/>' );

			// Add the label to the list item.
			jQuery( label ).appendTo( out );
			
			// Add the select menu to the list item.
			jQuery( select ).appendTo( out );

			return out;

		}

		/**
		 * Create the list of checkboxes.
		 * 
		 * @param  {string}   fieldsetID       The ID for this fieldset.
		 * @param  {object}   hidden           The input that stores the value of the checkboxes.
		 */
		function create( fieldsetID, hidden ) {

			// The JSON string in the hidden value.
			var hiddenVal = hidden.val();

			// Grab the hidden value as an object.
			if( typeof hiddenVal !== 'object' ) {
				var hiddenValArr = jQuery.parseJSON( hiddenVal );
			} else {
				var hiddenValArr = hiddenVal;
			}

			/**
			 * The availabe options for this fieldset, stored in the DOM as a
			 * data attribute on the input that stores the JSON.
			 */ 
			var choices    = hidden.data( 'choices' );
			choices        = JSON.stringify( choices );
			var choicesArr = JSON.parse( choices );

			// Start a list to hold the checkboxes, which we'll draw shortly.
			var sortable = jQuery( '<ul>' ).addClass( 'ui-sortable' ).insertBefore( hidden );	

			// Make the checkboxes sortable.
			jQuery( sortable ).sortable({
			
				// Once an item is dragged and sorted, update the preview and save the JSON.
				stop: function( event, ui ) {
					update( hidden  );
				}
			});

			// Loop through the current values and output checked checkboxes.
			if( ! jQuery.isEmptyObject( hiddenValArr ) ) {
				
				// For each of the current values...
				jQuery.each( hiddenValArr, function( key, value ) {
					
					// Draw a checkbox with the dropdown menu visible.
					var listItem = checkboxListItem( key, hiddenValArr, fieldsetID,  true );

					// Add it to the DOM and check the box.
					jQuery( listItem ).appendTo( sortable ).find( 'input' ).prop( 'checked', true );

				});
				

			}

			// For all the available choices on this fieldset...
			jQuery.each( choicesArr, function( key, value ) {
				
				// Draw a checkbox with the dropdown menu hidden.
				var listItem = checkboxListItem( key, hiddenValArr, fieldsetID,  false );

				// Add it to the DOM.
				jQuery( listItem ).appendTo( sortable ).find( 'input' );
	
			});

		}

		/**
		 * Update the value for this fieldset, perhaps when a checkbox is checked or sorted.
		 * 
		 * @param  {object} hidden The hidden form field that stores the values for this fieldset.
		 */
		function update( hidden ) {

			var out = {};

			// Grab the name of the hidden field.
			var hiddenName = jQuery( hidden ).attr( 'name' );

			// Grab all the checkboxes in this fieldset.
			var checkboxes = jQuery( hidden ).closest( 'li' ).find( '[type="checkbox"]:checked' );

			// Grab all the select menus in this fieldset.
			var selects = jQuery( hidden ).closest( 'li' ).find( 'select' );

			// For each checkbox...
			jQuery( checkboxes ).each( function( index, value ) {

				// Grab the value.
				var val = jQuery( value ).val();

				// Let's see if this checkbox carries a select menu as well.
				var which = jQuery( value ).attr( 'data-which' );

				// If it has a select menu, that acts as the value for the checkbox.
				if( typeof which != 'undefined' ) {
					out[val] = which;
				
				// If not, the checkbox is just boolean true.
				} else {
					out[val] = true;
				
				}

			});

			// Turn the output into JSON.
			var outStr  = JSON.stringify( out );
			var outJSON = JSON.parse( outStr );

			// Update the hidden field.
			jQuery( hidden ).val( outStr );
			
			// Grab the core WP customization object and update it.
			var api = wp.customize;
			api.instance( hiddenName ).set( outStr );

			// Show or hide the select menus based on whether the parent menu item is checked.
			jQuery( selects ).each( function( index, value ) {

				// Find out if the checkbox is checked.  If so, reveal the select menu.
				var checkbox = jQuery( value ).closest( 'li' ).find( '[type="checkbox"]' );
				if( jQuery( checkbox ).is( ':checked' ) ) {

					jQuery( value ).slideDown();

				// Else, hide the select menu.
				} else {

					jQuery( value ).slideUp();

				}

			});

		}

		/**
		 * The main part of the jQuery plugin that actually returns the selected items.
		 * 
		 * @return {object} The items that this plugin applies to.
		 */
		return this.each(function() {

			var that = this;

			// The ID for this section of the customizer.
			var fieldsetID = jQuery( that ).attr( 'id' );

			// The input that stores JSON as the checkboxes toggle and move.
			var hidden = jQuery( that ).find( '[type="text"]' );

			// Okay! Create the list of checkboxes!
			create( fieldsetID, hidden );

			// Whenever the checkboxes are checked, update the json and redraw the preview.
			jQuery( that ).on( 'change', '[type="checkbox"]', function( event ) { 
				update( hidden );
			});

			/**
			 * Whenever the dropdowns are selected, trigger a change on the
			 * corresponding checkbox.
			 */ 
			jQuery( that ).on( 'change', 'select', function( event ) { 

				/**
				 * What type of dropdown is this?  Maybe to get links by category, or get a custom menu?
				 */
				var type = jQuery( this ).attr( 'data-type' );
				
				// Find the corresponding checkbox.
				var checkbox = jQuery( this ).closest( 'li' ).find( '[type="checkbox"]:checked' );

				// Find out which <option> is selected.
				var selected = jQuery( this ).val();

				// Trigger a change on that checkbox.
				jQuery( checkbox ).attr( 'data-which', selected ).trigger( 'change' );

			});

			return that;

		});

	}

}( jQuery ));